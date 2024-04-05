public class Bloc {
    private BlocType typeBloc=null;
    private boolean etat=false;
    private int idGroupe=0;

    public Bloc (BlocType bloc) {
        typeBloc=bloc;
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

    public void afficher () {
        typeBloc.afficher();
        if (typeBloc.getType()>0) {
            System.out.println("Etat : "+etat);
            System.out.println("IdGroupe : "+idGroupe);
        }
    }

}
