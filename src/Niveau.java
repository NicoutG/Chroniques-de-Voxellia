import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.text.ParseException;
import java.util.Observable;
import java.util.Vector;

public class Niveau extends Observable {
    private BlocType [] blocs=null; // les différents types de blocs
    private Bloc [][][] terrain=null;
    private int taillex=0;
    private int tailley=0;
    private int taillez=0;
    private Joueur joueur=new Joueur();
    private String nomNiveau;

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

    private boolean chargerTerrain (String fichier) {
        nomNiveau=fichier;
        joueur.setVictoire(false);
        joueur.setMort(false);
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
            if (line.length!=3) {
                System.out.println("La première ligne doit posséder les 3 dimensions du terrain");
                return false;
            }
            taillex=Integer.parseInt(line[0]);
            tailley=Integer.parseInt(line[1]);
            taillez=Integer.parseInt(line[2]);
            if (taillex<=0 || tailley<=0 || taillez<=0) {
                System.out.println("Les dimensions du terrain doivent etre positive");
                return false;
            }
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
                        System.out.println("Le fichier doit avoir "+taillez+" blocs par ligne");
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
                                    default: terrain[x][y][z]=new Bloc (indice);
                                }
                                if (!terrain[x][y][z].setParametres(line[x])) {
                                    System.out.println("Parametres invalides pour le bloc x:"+x+" y:"+y+" z:"+z);
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

    public boolean actionJoueur (String action) {
        return joueur.actionJoueur(terrain,blocs,action);
    }

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

    public int getTaillex () {
        return taillex;
    }

    public int getTailley () {
        return tailley;
    }

    public int getTaillez () {
        return taillez;
    }

    public Bloc getBloc (int x, int y, int z) {
        if (x<0 || taillex<=x || y<0 || tailley<=y || z<0 || taillez<=z)
            return null;
        return terrain[x][y][z];
    }

    public Joueur getJoueur () {
        return joueur;
    }

    public boolean getVictoire () {
        return joueur.getVictoire();
    }

    public boolean getMort () {
        return joueur.getMort();
    }

    public void miseAjourTerrain () {
        
        // mise à jour du joueur
        joueur.miseAjour(terrain, blocs);

        // mise à jour des blocs
        for (int z=0;z<taillez;z++)
            for (int y=0;y<tailley;y++)
                for (int x=0;x<taillex;x++) 
                    if (terrain[x][y][z]!=null) {
                        terrain[x][y][z].miseAjour(terrain, blocs, x, y, z, joueur);
                        if (x==joueur.getX() && y==joueur.getY() && z==joueur.getZ())
                            joueur.setMort(true);
                    }
        
        // on recharge le niveau lorsque le joueur meurt
        if (getMort())
            chargerTerrain(nomNiveau);

        setChanged();
        notifyObservers();
    }

    public void lancementNiveau (String fichier) {
        
        // chargement du terrain
        chargerTerrain(fichier);

        Thread terrainUpdateThread = new Thread(() -> {
            while (!getVictoire()) {
                
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

    public Vector <String> getTextures (String folder) {
        File fold=new File (folder);
        Vector <String> textures=new Vector <String> ();
        for (File file : fold.listFiles()) {
            if (!file.isDirectory())
                textures.add(file.getName());
        }
        return textures;
    }

    public String getTextureBloc (int x, int y, int z) {
        if (0<=x && x<terrain.length && 0<=y && y<terrain[x].length && 0<=z && z<terrain[x][y].length && terrain[x][y][z]!=null)
            return terrain[x][y][z].getTexture(terrain, blocs, x, y, z, joueur);
        return "default.jpg";
    }

    public String getTextureJoueur () {
        return joueur.getTexture(terrain, blocs);
    }
    
}
