public class BlocEscalier extends Bloc {

    public BlocEscalier (int id) {
        super(id);
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        if (depz==0)  
            joueur.deplacer(terrain,blocs,depx,depy,1);
        return false;
    }
}
