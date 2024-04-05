import java.io.IOException;
import java.nio.file.*;

public class Niveau {
    private BlocType [] blocs=null; // les différents types de blocs
    private Bloc [][][] terrain=null;
    private int taillex;
    private int tailley;
    private int taillez;


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
            for (int i=1;i<exps.length;i++) {
                blocs[i-1]=new BlocType();
                if (!blocs[i-1].charger(exps[i])) {
                    blocs=null;
                    return false;
                }
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean chargerTerrain (String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=exp.split("\r\n");
            if (exps.length<=1) {
                System.out.println("Le fichier contient moins de 2 lignes");
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
            if (taillex<=0) {
                System.out.println("Les dimensions du terrain doivent etre positive");
                return false;
            }
            tailley=Integer.parseInt(line[1]);
            if (tailley<=0) {
                System.out.println("Les dimensions du terrain doivent etre positive");
                return false;
            }
            taillez=Integer.parseInt(line[2]);
            if (taillez<=0) {
                System.out.println("Les dimensions du terrain doivent etre positive");
                return false;
            }
            if (exps.length<taillez*tailley+1) {
                System.out.println("Le fichier doit avoir "+(taillez*tailley+1)+" lignes");
                return false;
            }
            terrain=new Bloc [taillex][tailley][taillez];
            
            // chargement des blocs du terrain
            for (int z=0;z<taillez;z++)
                for (int y=0;y<tailley;y++) {
                    int numLine=1+z*tailley+y;
                    line=exps[numLine].split(" ");
                    if (line.length!=taillex) {
                        System.out.println("Le fichier doit avoir "+taillez+" blocs par ligne");
                        return false;
                    }
                    for (int x=0;x<taillex;x++) {
                        String [] bloc=line[x].split("/");
                        if (bloc[0].equals("v")) // Si c'est un bloc de vide
                            terrain[x][y][z]=null;
                        else {
                            int indice=Integer.parseInt(bloc[0]);
                            if (indice<0 || indice>blocs.length) {
                                System.out.println("Le bloc "+indice+" n est pas definie");
                                return false;
                            }
                            terrain[x][y][z]=new Bloc (blocs[indice]); // recuperation du blocType
                            if (blocs[indice].getType()>0) { // Si c'est un bloc de type spécial on a besoin de son etat et idGroupe
                                if (bloc.length!=3) {
                                    System.out.println("Les blocs speciaux doivent avoir leur etat et leur idGroupe");
                                    return false;
                                }
                                terrain[x][y][z].setEtat(bloc[1].equals("1"));
                                terrain[x][y][z].setIdGroupe(Integer.parseInt(bloc[2]));
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

    public void afficherTerrain () {
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
    
}
