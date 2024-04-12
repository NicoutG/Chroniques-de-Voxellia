public class BlocLevier extends BlocActivation {

    public BlocLevier (int id) {
        super(id);
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        if (depx!=0 || depy!=z) {
            setEtat(!getEtat());
            activer(terrain,blocs);
        }
        return super.deplacer(terrain,blocs,x,y,z,depx,depy,depz,joueur);
    }
}
