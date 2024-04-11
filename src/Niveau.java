import java.io.IOException;
import java.nio.file.*;

public class Niveau {
    private BlocType [] blocs=null; // les différents types de blocs
    private Bloc [][][] terrain=null;
    private int taillex=0;
    private int tailley=0;
    private int taillez=0;
    private Joueur joueur=new Joueur();


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

    public boolean chargerTerrain (String fichier) {
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
            line=exps[1].split(" ");
            if (line.length!=3) {
                System.out.println("La deuxième ligne doit posséder les 3 coordonnées du joueur");
                return false;
            }
            int posx=Integer.parseInt(line[0]);
            int posy=Integer.parseInt(line[1]);
            int posz=Integer.parseInt(line[2]);
            if (posx<0 || taillex<=posx || posy<0 || tailley<=posy || posz<0 || taillez<=posz) {
                System.out.println("Les coordonnées du joueur doivent appartenir au terrain");
                return false;
            }
            joueur.setPos(posx,posy,posz);
            if (exps.length<taillez*tailley+1) {
                System.out.println("Le fichier doit avoir "+(taillez*tailley+2)+" lignes");
                return false;
            }
            terrain=new Bloc [taillex][tailley][taillez];
            
            // chargement des blocs du terrain
            for (int z=0;z<taillez;z++)
                for (int y=0;y<tailley;y++) {
                    int numLine=3+z*(tailley+1)+y;
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
                            int indice=Integer.parseInt(bloc[0]);
                            if (indice<0 || indice>blocs.length) {
                                System.out.println("Le bloc "+indice+" n est pas definie");
                                return false;
                            }
                            switch (blocs[indice].getType()) {
                                case 1: {
                                    terrain[x][y][z]=new BlocMouvant (blocs[indice]);
                                    if (bloc.length!=1) {
                                        System.out.println("Les blocs mouvants n'ont pas de paramètres supplémentaires");
                                        return false;
                                    }
                                }break;
                                case 2: {
                                    terrain[x][y][z]=new BlocLevier (blocs[indice]);
                                    if (bloc.length!=3) {
                                        System.out.println("Les blocs d'activations doivent avoir leur etat et leur idGroupe");
                                        return false;
                                    }
                                    ((BlocLevier)terrain[x][y][z]).setEtat(bloc[1].equals("t"));
                                    ((BlocLevier)terrain[x][y][z]).setIdGroupe(Integer.parseInt(bloc[2]));
                                }break;
                                case 3: {
                                    terrain[x][y][z]=new BlocPlaque (blocs[indice]);
                                    if (bloc.length!=3) {
                                        System.out.println("Les blocs d'activations doivent avoir leur etat et leur idGroupe");
                                        return false;
                                    }
                                    ((BlocPlaque)terrain[x][y][z]).setEtat(bloc[1].equals("t"));
                                    ((BlocPlaque)terrain[x][y][z]).setIdGroupe(Integer.parseInt(bloc[2]));
                                }break;
                                default: terrain[x][y][z]=new Bloc (blocs[indice]);
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

    public boolean deplacerJoueur (int depx, int depy, int depz) {
        return joueur.deplacer(terrain, depx, depy, depz);
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
                        terrain[x][y][z].afficher();
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
                        System.out.print(terrain[x][y][z].getTypeBloc().getId()+"  ");
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
    
}
