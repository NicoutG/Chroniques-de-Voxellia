public class BlocType {
    private String nom="vide"; // le nom du bloc
    private boolean collision=false; // si le bloc empêche le déplacement du joueur
    private boolean mouvant=false; // si le bloc peut être déplacé
    private boolean mortel=false; // si le bloc est mortel au touché
    private char matiere='v'; // définie la matière du bloc

    public boolean charger (String exp) {
        String [] exps=exp.split(" ");
        if (exps.length!=5)
            return false;
        nom=exps[0];
        collision=exps[1].equals("1");
        mouvant=exps[2].equals("1");
        mortel=exps[3].equals("1");
        matiere=exps[4].charAt(0);
        return true;
    }

    public String getNom () {
        return nom;
    }

    public boolean getCollision () {
        return collision;
    }

    public boolean getMouvant () {
        return mouvant;
    }

    public boolean getMortel () {
        return mortel;
    }

    public char getMatiere () {
        return matiere;
    }

    public void afficher () {
        System.out.println("Nom : "+nom);
        System.out.println("Collision : "+collision);
        System.out.println("Mouvant : "+mouvant);
        System.out.println("Mortel : "+mortel);
        System.out.println("Matiere : "+matiere);
    }

}
