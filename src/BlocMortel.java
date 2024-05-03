public class BlocMortel extends Bloc {

    public BlocMortel (int id) {
        super(id);
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
        return true;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain, blocs, x, y, z, joueur);
        if (z+1<terrain[x][y].length)
            if (terrain[x][y][z+1] instanceof BlocMouvant)
                terrain[x][y][z]=null;
    }
}
