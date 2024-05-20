
/**
 * Classe BlocActivable permet la gestion des blocs activables.
 */

public abstract class BlocActivable extends Bloc {

    /**
     * etat contient l'état actuel du bloc.
     */
    private boolean etat=false;

    /**
     * idGroupe contient l'identifiant relatif à l'activation et à la désactivation du bloc.
     */
    private int idGroupe=0;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocActivable (int id) {
        super(id);
    }

    /**
     * accesseur de etat
     * @return boolean l'état actuel du bloc
     */
    public boolean getEtat () {
        return etat;
    }

    /**
     * mutateur de etat
     * @param e l'état actuel du bloc
     */
    public void setEtat (boolean e) {
        etat=e;
    }

    /**
     * accesseur de idGroupe
     * @return int l'identifiant relatif à l'activation et à la désactivation du bloc
     */
    public int getIdGroupe () {
        return idGroupe;
    }

    /**
     * mutateur de idGroupe
     * @return id l'identifiant relatif à l'activation et à la désactivation du bloc
     */
    public void setIdGroupe (int id) {
        idGroupe=id;
    }

    /**
     * Active le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public abstract void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur);

    /**
     * Désactive le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public abstract void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur);

    /**
     * Affiche les informations du bloc.
     * @param blocs la liste des types de blocs
     */
    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }

}
