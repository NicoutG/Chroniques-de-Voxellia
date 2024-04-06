public class BlocType {
    private String nom="default"; // le nom du bloc
    private boolean mouvant=false; // si le bloc peut être déplacé
    private boolean mortel=false; // si le bloc est mortel au touché
    private char matiere='d'; // définie la matière du bloc f:feu, g:glace, b:bois, m:metal, p:pierre
    private int type=0; // 0 pour un type normal, 1 pour un bloc d'activation, 2 pour un bloc activable

    public boolean charger (String exp) {
        String [] exps=exp.split(" ");
        if (exps.length!=5)
            return false;
        nom=exps[0];
        type=Integer.parseInt(exps[1]);
        mouvant=exps[2].equals("1");
        mortel=exps[3].equals("1");
        matiere=exps[4].charAt(0);
        return true;
    }

    public String getNom () {
        return nom;
    }

    public boolean getMouvant () {
        return mouvant;
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
        System.out.println("Mouvant : "+mouvant);
        System.out.println("Mortel : "+mortel);
        System.out.println("Matiere : "+matiere);
    }

}
