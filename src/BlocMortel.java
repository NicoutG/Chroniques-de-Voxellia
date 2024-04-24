public class BlocMortel extends Bloc {

    public BlocMortel (int id) {
        super(id);
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        return true;
    }
}
