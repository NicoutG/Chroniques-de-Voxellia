import java.io.IOException;
import java.nio.file.*;

public class Niveau {
    private BlocType [] blocs=null; // les différents types de blocs

    public boolean chargerBlocs(String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=exp.split("\n");
            if (exps.length<=1)
                return false;
            blocs=new BlocType [exps.length];
            blocs[0]=new BlocType(); // le bloc de vide par défaut
            for (int i=1;i<exps.length;i++) {
                blocs[i]=new BlocType();
                if (!blocs[i].charger(exps[i])) {
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
    
}
