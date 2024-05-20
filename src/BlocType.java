
/**
 * Classe BlocType contient les informations sur les types de blocs.
 */

public class BlocType {

    /**
     * id contient l'index auquel il se trouve dans le tableau.
     */
    private int id=0;

    /**
     * nom contient le nom du type de bloc.
     */
    private String nom="default";

    /**
     * destructible contient si le bloc est destructible lors d'un impacte.
     */
    private boolean destructible=false;

    /**
     * matière contient la matière qui compose le bloc.
     */
    private char matiere='d';

    /**
     * type contient le type de bloc (0 bloc normal, 1 bloc mouvant, 2 bloc de levier, 3 bloc de plaque, ...).
     */
    private int type=0;

    /**
     * texture contient la texture par défaut du bloc.
     */
    private String texture = "default.jpg";

    /**
     * nbImages contient le nombre de textures qui définissent l'animation du bloc.
     */
    private int nbImages=1;

    /**
     * Charge les paramètres du type de bloc à partir d'une chaine de caractères.
     * @param exps la chaine qui contient les informations sur les paramètres du type de bloc
     * @return boolean si le chargement des paramètres a bien été effectué
     */
    public boolean charger (String exp) {
        String [] exps=exp.split(" ");
        if (exps.length<6 || 7<exps.length) {
            System.out.println("Mauvais nombre d'arguments");
            return false;
        }
        id=Integer.parseInt(exps[0]);
        nom=exps[1];
        type=Integer.parseInt(exps[2]);
        destructible=exps[3].equals("t");
        matiere=exps[4].charAt(0);
        texture=exps[5];
        if (exps.length==7) {
            nbImages=Integer.parseInt(exps[6]);
            if (nbImages<=0) {
                System.out.println("Le nombre d'images doit être strictement positif");
                return false;
            }
        }
        else 
            nbImages=1;
        return true;
    }

    /**
     * accesseur de getId
     * @return int l'index auquel il se trouve dans le tableau
     */
    public int getId () {
        return id;
    }

    /**
     * accesseur de nom
     * @return String le nom du type de bloc
     */
    public String getNom () {
        return nom;
    }

    /**
     * accesseur de destructible
     * @return boolean si le bloc est destructible lors d'un impacte
     */
    public boolean getDestructible () {
        return destructible;
    }

    /**
     * accesseur de type
     * @return int le type de bloc
     */
    public int getType () {
        return type;
    }

    /**
     * accesseur de matière
     * @return char la matière qui compose le bloc
     */
    public char getMatiere () {
        return matiere;
    }

    /**
     * accesseur de texture
     * @return String la texture par défaut du bloc
     */
    public String getTexture () {
        return texture;
    }

    /**
     * accesseur de nbImages
     * @return int le nombre de textures qui définissent l'animation du bloc
     */
    public int getNbImages () {
        return nbImages;
    }

    /**
     * Affiche les informations du type de bloc.
     */
    public void afficher () {
        System.out.println("Nom : "+nom);
        System.out.println("Type : "+type);
        System.out.println("Mortel : "+destructible);
        System.out.println("Matiere : "+matiere);
        System.out.println("Texture : "+texture);
    }

}
