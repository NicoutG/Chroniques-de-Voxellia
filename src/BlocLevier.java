
/**
 * Classe BlocLevier permet la gestion des blocs de levier.
 */

public class BlocLevier extends BlocActivation {

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocLevier (int id) {
        super(id);
    }

    /**
     * Charge les paramètres du bloc à partir d'une chaine de caractères.
     * @param params la chaine qui contient les informations sur les paramètres du bloc
     * @return boolean si le chargement des paramètres a bien été effectué
     */
    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=3)
            return false;
        super.setEtat(paramList[1].equals("t"));
        super.setIdGroupe(Integer.parseInt(paramList[2]));
        return true;
    }

    /**
     * Envoie une requête de déplacement au bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param depx le déplacement x du bloc
     * @param depy le déplacement y du bloc
     * @param depz le déplacement z du bloc
     * @param joueur le joueur qui joue sur le niveau
     * @param num identifie l'objet qui appelle la fonction
     * @return boolean si le bloc a pu effectuer le déplacement
     */
    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
        if ((depx!=0 || depy!=0) && num==0) {
            setEtat(!getEtat());
            if (getEtat())
                activer(terrain,blocs,joueur);
            else
                desactiver(terrain,blocs,joueur);
        }
        return super.deplacer(terrain,blocs,x,y,z,depx,depy,depz,joueur,1);
    }

    /**
     * Renvoie la texture actuelle du bloc.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     * @return String la texture actuelle du bloc
     */
    @Override
    public String getTexture (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (getEtat()) {
            String texture=super.getTexture(terrain, blocs, x, y, z, joueur);
            String [] text=texture.split("\\.");
            return text[0]+"-T."+text[1];
        }
        String texture=getBlocType(blocs).getTexture();
        String [] text=texture.split("\\.");
        return text[0]+"-F."+text[1];
    }

}
