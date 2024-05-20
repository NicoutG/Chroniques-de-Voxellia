
/**
 * Classe BlocMortel permet la gestion des blocs mortels pour le joueur.
 */

public class BlocMortel extends Bloc {

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocMortel (int id) {
        super(id);
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
        return true;
    }

    /**
     * Met à jour le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain, blocs, x, y, z, joueur);
        if (z+1<terrain[x][y].length)
            if (terrain[x][y][z+1] instanceof BlocMouvant)
                terrain[x][y][z]=null;
    }
}
