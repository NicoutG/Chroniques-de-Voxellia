public class BlocType {
    private int id=0;
    private String nom="default"; // le nom du bloc
    private boolean destructible=false; // si le bloc se casse lorsque le joueur ou un bloc tombe dessus
    private char matiere='d'; // définie la matière du bloc f:feu, g:glace, b:bois, m:metal, p:pierre
    private int type=0; /*  0 bloc normal
                            1 bloc mouvant
                            2 bloc de levier
                            3 bloc de plaque
                        */
    private String texture = "default.jpg"; // le chemin de la texture

    public boolean charger (String exp) {
        String [] exps=exp.split(" ");
        if (exps.length!=6) {
            System.out.println("Mauvais nombre d'arguments");
            return false;
        }
        id=Integer.parseInt(exps[0]);
        nom=exps[1];
        type=Integer.parseInt(exps[2]);
        destructible=exps[3].equals("t");
        matiere=exps[4].charAt(0);
        texture=exps[5];
        return true;
    }

    public int getId () {
        return id;
    }

    public String getNom () {
        return nom;
    }

    public boolean getDestructible () {
        return destructible;
    }

    public int getType () {
        return type;
    }

    public char getMatiere () {
        return matiere;
    }

    public String getTexture () {
        return texture;
    }

    public void afficher () {
        System.out.println("Nom : "+nom);
        System.out.println("Type : "+type);
        System.out.println("Mortel : "+destructible);
        System.out.println("Matiere : "+matiere);
        System.out.println("Texture : "+texture);
    }

}
