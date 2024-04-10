public class BlocType {
    private int id=0;
    private String nom="default"; // le nom du bloc
    private boolean mortel=false; // si le bloc est mortel au touché
    private char matiere='d'; // définie la matière du bloc f:feu, g:glace, b:bois, m:metal, p:pierre
    private int type=0; /*  0 bloc normal
                            1 bloc mouvant
                            2 bloc d'activation
                            3 bloc activable */

    public boolean charger (String exp) {
        String [] exps=exp.split(" ");
        if (exps.length!=5) {
            System.out.println("Mauvais nombre d'arguments");
            return false;
        }
        id=Integer.parseInt(exps[0]);
        nom=exps[1];
        type=Integer.parseInt(exps[2]);
        mortel=exps[3].equals("1");
        matiere=exps[4].charAt(0);
        return true;
    }

    public int getId () {
        return id;
    }

    public String getNom () {
        return nom;
    }

    public boolean getMortel () {
        return mortel;
    }

    public int getType () {
        return type;
    }

    public char getMatiere () {
        return matiere;
    }

    public void afficher () {
        System.out.println("Nom : "+nom);
        System.out.println("Type : "+type);
        System.out.println("Mortel : "+mortel);
        System.out.println("Matiere : "+matiere);
    }

}
