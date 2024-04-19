public abstract class BlocActivable extends Bloc {
    private boolean etat=false;
    private int idGroupe=0;

    public BlocActivable (int id) {
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

    public abstract void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur);

    public abstract void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur);

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }


}
