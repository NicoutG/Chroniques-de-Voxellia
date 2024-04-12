public class BlocActivation extends Bloc {
    private boolean etat=false;
    private int idGroupe=0;

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

    protected void activer (Bloc [][][] terrain, BlocType [] blocs) {
        for (int x=0;x<terrain.length;x++)
            for (int y=0;y<terrain[x].length;y++)
                for (int z=0;z<terrain[x][y].length;z++)
                    if (terrain[x][y][z] instanceof BlocActivable)
                        if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                            ((BlocActivable)terrain[x][y][z]).activation();
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }


}
