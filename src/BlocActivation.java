public class BlocActivation extends Bloc {
    private boolean etat=false;
    private int idGroupe=0; /*
                                -1 = Victoire
                                Autres = activations des blocs du même idGroupe
                             */

    public BlocActivation (int id) {
        super(id);
    }

    public void setEtat (boolean e) {
        etat=e;
    }

    public boolean getEtat () {
        return etat;
    }

    public void setIdGroupe (int id) {
        idGroupe=id;
    }

    public int getIdGroupe () {
        return idGroupe;
    }

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
                                    ((BlocActivable)terrain[x][y][z]).activation();
        }
    }

    protected void desactiver (Bloc [][][] terrain, BlocType [] blocs) {
        // désactivation des blocs du groupe
        for (int x=0;x<terrain.length;x++)
            for (int y=0;y<terrain[x].length;y++)
                for (int z=0;z<terrain[x][y].length;z++)
                    if (terrain[x][y][z] instanceof BlocActivable)
                        if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                            ((BlocActivable)terrain[x][y][z]).desactivation();
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }


}
