
/**
 * Classe BlocActivation permet la gestion des blocs d'activation.
 */

public class BlocActivation extends Bloc {

    /**
     * etat contient l'état actuel du bloc.
     */
    private boolean etat=false;

    /**
     * idGroupe contient l'identifiant relatif à l'activation et à la désactivation des blocs (-1 victoire).
     */
    private int idGroupe=0;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocActivation (int id) {
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
     * @return int l'identifiant relatif à l'activation et à la désactivation des blocs
     */
    public int getIdGroupe () {
        return idGroupe;
    }

    /**
     * mutateur de idGroupe
     * @return id l'identifiant relatif à l'activation et à la désactivation des blocs
     */
    public void setIdGroupe (int id) {
        idGroupe=id;
    }

    /**
     * Active le bloc d'activation.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param joueur le joueur qui joue sur le niveau
     */
    protected void activer (Bloc [][][] terrain, BlocType [] blocs, Joueur joueur) {

        // vérification que tout est allumé pour activer
        boolean activation=true;
        for (int x=0;x<terrain.length;x++)
                for (int y=0;y<terrain[x].length;y++)
                    for (int z=0;z<terrain[x][y].length;z++)
                        if (terrain[x][y][z] instanceof BlocActivation && idGroupe==((BlocActivation)terrain[x][y][z]).getIdGroupe() && !((BlocActivation)terrain[x][y][z]).getEtat())
                            activation=false;

        // si tout est allumé
        if (activation) {
            if (idGroupe==-1)
                joueur.setVictoire(true);
            else
                for (int x=0;x<terrain.length;x++)
                    for (int y=0;y<terrain[x].length;y++)
                        for (int z=0;z<terrain[x][y].length;z++)
                            if (terrain[x][y][z] instanceof BlocActivable)
                                if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                                    ((BlocActivable)terrain[x][y][z]).activation(terrain,blocs,x,y,z,joueur);
        }
    }

    /**
     * Désactive le bloc d'activation.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param joueur le joueur qui joue sur le niveau
     */
    protected void desactiver (Bloc [][][] terrain, BlocType [] blocs, Joueur joueur) {
        // désactivation des blocs du groupe
        for (int x=0;x<terrain.length;x++)
            for (int y=0;y<terrain[x].length;y++)
                for (int z=0;z<terrain[x][y].length;z++)
                    if (terrain[x][y][z] instanceof BlocActivable)
                        if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                            ((BlocActivable)terrain[x][y][z]).desactivation(terrain,blocs,x,y,z,joueur);
    }

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
