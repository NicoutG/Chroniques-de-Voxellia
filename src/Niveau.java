import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.util.Observable;
import java.util.Vector;

/**
 * Classe Niveau permet de jouer à un niveau.
 */

public class Niveau extends Observable {

    /**
     * blocs contient l'ensemble des types de blocs.
     */
    private BlocType [] blocs=null;

    /**
     * terrain contient l'ensemble des blocs du terrain.
     */
    private Bloc [][][] terrain=null;

    /**
     * defaultBloc contient le bloc par défaut à afficher sur les cases vides.
     */
    private Bloc defaultBloc=null;

    /**
     * taillex contient la taille x du terrain.
     */
    private int taillex=0;

    /**
     * tailley contient la taille y du terrain.
     */
    private int tailley=0;

    /**
     * taillez contient la taille z du terrain.
     */
    private int taillez=0;

    /**
     * joueur contient le personnage du joueur.
     */
    private Joueur joueur=new Joueur();

    /**
     * nomNiveau contient le nom du niveau à charger.
     */
    private String nomNiveau;

    /**
     * fin contient si le joueur souhaite quitter le niveau.
     */
    private boolean fin=false;

    /**
     * Charge l'ensemble des types de blocs.
     * @param fichier le nom du fichier qui contient les types de blocs
     * @return boolean si le chargement a bien été effectué
     */
    public boolean chargerBlocs(String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=exp.split("\r\n");
            if (exps.length<=1)
                return false;
            blocs=new BlocType [exps.length-1];

            // suppression des espaces en trop
            for (int i=0;i<exps.length;i++)
                while (exps[i].contains("  "))
                    exps[i]=exps[i].replace("  "," ");
            
            // chargement des blocs
            BlocType nouvBloc=null;
            for (int i=1;i<exps.length;i++) {
                nouvBloc=new BlocType();
                if (!nouvBloc.charger(exps[i])) {
                    blocs=null;
                    System.out.println("Impossible de charger le bloc à la ligne "+i);
                    return false;
                }
                int id=nouvBloc.getId();
                if (id<0 || blocs.length<=id) {
                    blocs=null;
                    System.out.println("L'id "+id+" doit etre entre 0 et "+blocs.length);
                    return false;
                }
                if (blocs[id]!=null) {
                    blocs=null;
                    System.out.println("L'id "+id+" est présent en double");
                    return false;
                }
                blocs[id]=nouvBloc;
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Charge un terrain.
     * @param fichier le nom du fichier qui contient le terrain
     * @return boolean si le chargement a bien été effectué
     */
    public boolean chargerTerrain (String fichier) {
        joueur.setVictoire(false);
        joueur.setMort(false);
        fin=false;
        nomNiveau=fichier;
        if (blocs==null || blocs.length<1) {
            System.out.println("Aucun bloc chargé");
            return false;
        }
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=exp.split("\r\n");
            if (exps.length<=2) {
                System.out.println("Le fichier contient moins de 3 lignes");
                return false;
            }
            
            // suppression des espaces en trop
            for (int i=0;i<exps.length;i++)
                while (exps[i].contains("  "))
                    exps[i]=exps[i].replace("  "," ");
            
            // Chargement des dimensions du terrain
            String [] line=exps[0].split(" ");
            if (line.length<3 || 4<line.length) {
                System.out.println("La première ligne doit posséder les 3 dimensions du terrain et ou le bloc par defaut");
                return false;
            }
            taillex=Integer.parseInt(line[0]);
            tailley=Integer.parseInt(line[1]);
            taillez=Integer.parseInt(line[2]);
            if (taillex<=0 || tailley<=0 || taillez<=0) {
                System.out.println("Les dimensions du terrain doivent etre positive");
                return false;
            }

            // Chargement du bloc par défaut
            if (line.length==4)
                defaultBloc=new Bloc (Integer.parseInt(line[3]));
            else
                defaultBloc=null;
            
            joueur.setPos(0,0,0);
            if (exps.length<taillez*tailley+1) {
                System.out.println("Le fichier doit avoir "+(taillez*tailley+1)+" lignes");
                return false;
            }
            terrain=new Bloc [taillex][tailley][taillez];
            
            // chargement des blocs du terrain
            for (int z=0;z<taillez;z++)
                for (int y=0;y<tailley;y++) {
                    int numLine=2+z*(tailley+1)+y;
                    line=exps[numLine].split(" ");
                    if (line.length!=taillex) {
                        System.out.println("Le fichier doit avoir "+taillex+" blocs par ligne");
                        return false;
                    }
                    for (int x=0;x<taillex;x++) {
                        String [] bloc=line[x].split("/");
                        if (bloc[0].equals(".")) // Si c'est un bloc de vide
                            terrain[x][y][z]=null;
                        else {
                            if (bloc[0].equals("j")) {
                                terrain[x][y][z]=null;
                                joueur.setPos(x, y, z);
                            }
                            else {
                                int indice=Integer.parseInt(bloc[0]);
                                if (indice<0 || indice>blocs.length) {
                                    System.out.println("Le bloc "+indice+" n est pas definie");
                                    return false;
                                }
                                switch (blocs[indice].getType()) {
                                    case 1: terrain[x][y][z]=new BlocMouvant (indice);break;
                                    case 2: terrain[x][y][z]=new BlocLevier (indice);break;
                                    case 3: terrain[x][y][z]=new BlocPlaque (indice);break;
                                    case 4: terrain[x][y][z]=new BlocTeleporteur (indice);break;
                                    case 5: terrain[x][y][z]=new BlocPiston (indice);break;
                                    case 6: terrain[x][y][z]=new BlocEnnemi (indice);break;
                                    case 7: terrain[x][y][z]=new BlocMortel (indice);break;
                                    case 8: terrain[x][y][z]=new BlocEscalier (indice);break;
                                    case 9: terrain[x][y][z]=new BlocExplosif (indice);break;
                                    default: terrain[x][y][z]=new Bloc (indice);
                                }
                                if (!terrain[x][y][z].setParametres(line[x])) {
                                    System.out.println("Parametres invalides pour le bloc x:"+x+" y:"+y+" z:"+z+" pour "+line[x]);
                                    return false;
                                }
                            }
                        }
                    }
                }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Permet au joueur de controler le personnage.
     * @param action le texte qui décrit l'action du joueur
     * @return boolean si l'action peut être réalisée
     */
    public boolean actionJoueur (String action) {
        if (!getVictoire()) {
            boolean res=joueur.actionJoueur(terrain,blocs,action);
            miseAjourTerrain();
            return res;
        }
        return false;
    }

    /**
     * Affiche les informations de tous les types de blocs.
     */
    public void afficherBlocs () {
        if (blocs!=null)
            for (int i=0;i<blocs.length;i++) {
                System.out.println("Bloc "+i);
                blocs[i].afficher();
                System.out.println();
            }
        else
            System.out.println("Aucun bloc");
    }

    /**
     * Affiche les informations de tous les blocs du terrain.
     */
    public void afficherTerrainDetails () {
        for (int z=0;z<taillez;z++)
            for (int y=0;y<tailley;y++)
                for (int x=0;x<taillex;x++) {
                    if (terrain[x][y][z]!=null) {
                        System.out.println("\nBloc "+x+"/"+y+"/"+z);
                        terrain[x][y][z].afficher(blocs);
                    }
                }
        System.out.println("\nDimensions : "+taillex+"/"+tailley+"/"+taillez);
    }

    /**
     * Affiche le terrain en version console.
     */
    public void afficherTerrain () {
        for (int z=0;z<taillez;z++) {
            System.out.println("Level "+z+"/"+(taillez-1));
            for (int y=0;y<tailley;y++) {
                for (int x=0;x<taillex;x++)
                    if (terrain[x][y][z]!=null)
                        System.out.print(terrain[x][y][z].getIdBlocType()+"  ");
                    else
                        if (x==joueur.getX() && y==joueur.getY() && z==joueur.getZ())
                            System.out.print("J  ");
                        else
                            System.out.print(".  ");
                System.out.print("\n");
            }
        }
    }

    /**
     * Accesseur de taillex
     * @return int taillex
     */
    public int getTaillex () {
        return taillex;
    }

    /**
     * Accesseur de tailley
     * @return int tailley
     */
    public int getTailley () {
        return tailley;
    }

    /**
     * Accesseur de taillez
     * @return int taillez
     */
    public int getTaillez () {
        return taillez;
    }

    /**
     * Retourne si le niveau est réussi.
     * @return boolean si le niveau est réussi
     */
    public boolean getVictoire () {
        return joueur.getVictoire();
    }

    /**
     * Retourne si le joueur est mort.
     * @return boolean si le joueur est mort
     */
    private boolean getMort () {
        return joueur.getMort();
    }

    /**
     * Met à jour le niveau.
     */
    private void miseAjourTerrain () {
        
        // mise à jour du joueur
        joueur.miseAjour(terrain, blocs);

        // mise à jour du bloc par défaut
        if (defaultBloc!=null)
            defaultBloc.miseAjour(terrain, blocs, 0, 0, 0, joueur);

        // mise à jour des blocs
        for (int z=0;z<taillez;z++)
            for (int y=0;y<tailley;y++)
                for (int x=0;x<taillex;x++) 
                    if (terrain[x][y][z]!=null)
                        terrain[x][y][z].miseAjour(terrain, blocs, x, y, z, joueur);
        
        // on recharge le niveau lorsque le joueur meurt
        if (getMort())
            recommencer();

        setChanged();
        notifyObservers();
    }

    /**
     * Réinitialise le niveau.
     */
    public void recommencer () {
        chargerTerrain(nomNiveau);
    }

    /**
     * Arrête le niveau.
     */
    public void quitter () {
        fin=true;
    }

    /**
     * Lance le niveau 2 secondes après.
     */
    public void lancementNiveau () {
        joueur.setVictoire(false);
        joueur.setMort(false);
        fin=false;
        Thread terrainUpdateThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            while (!getVictoire() && !fin) {
                
                // mise à jour du terrain à intervalles réguliers
                miseAjourTerrain();

                try {
                    Thread.sleep(10); // attendre 10 millisecondes avant la prochaine mise à jour (100 maj/s)
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        terrainUpdateThread.start();
    }

    /**
     * Renvoie la liste de toutes les textures à charger.
     * @param folder le chemin vers le dossier qui contient les textures
     * @return Vector <String> la liste de toutes les textures à charger
     */
    public Vector <String> getTextures (String folder) {
        File fold=new File (folder);
        Vector <String> textures=new Vector <String> ();
        for (File file : fold.listFiles()) {
            if (!file.isDirectory())
                textures.add(file.getName());
        }
        return textures;
    }

    /**
     * Renvoie la texture du bloc.
     * @param int x la coordonnée x du bloc
     * @param int y la coordonnée y du bloc
     * @param int z la coordonnée z du bloc
     * @return String la texture du bloc
     */
    public String getTextureBloc (int x, int y, int z) {
        if (0<=x && x<terrain.length && 0<=y && y<terrain[x].length && 0<=z && z<terrain[x][y].length) {
            if (terrain[x][y][z]!=null)
                return terrain[x][y][z].getTexture(terrain, blocs, x, y, z, joueur);
            else
                if (x==joueur.getX() && y==joueur.getY() && z==joueur.getZ())
                    return joueur.getTexture(terrain, blocs);
                else
                    if (defaultBloc!=null)
                        return defaultBloc.getTexture(terrain, blocs, x, y, z, joueur);
        }
        return "default";
    }

    public void validerNiveau() {
        
        try {
            String exp=new String(Files.readAllBytes(Paths.get("./data/texte/niveaux.txt")));
            String [] exps=exp.split("\r\n");
            if (exps.length<1)
                return;
            
            String nivFile = nomNiveau.split("/")[nomNiveau.split("/").length-1];
                        
            for (int i=0;i<exps.length;i++) {
                String [] line=exps[i].split(";");
                if (line[1].equals(nivFile)) {
                    line[3]="1";
                    exps[i]=String.join(";",line);
                    System.out.println("Niveau "+line[1]+" validé");
                    break;
                }
            }

            try {
                Files.write(Paths.get("./data/texte/niveaux.txt"),String.join("\r\n",exps).getBytes());
            }
            catch (IOException e) {
                return;
            }
        }
        catch (IOException e) {
            return;
        }
    }
    
}
