public abstract class BlocActivable extends Bloc {
    private boolean etat=false;
    private int idGroupe=0;

    public BlocActivable (BlocType bloc) {
        super(bloc);
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

    public abstract void activation ();

    @Override
    public void afficher () {
        super.afficher();
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }


}
