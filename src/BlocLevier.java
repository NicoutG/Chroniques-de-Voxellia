public class BlocLevier extends BlocActivation {

    public BlocLevier (BlocType bloc) {
        super(bloc);
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        if (depx!=0 || depy!=z) {
            setEtat(!getEtat());
            activer(terrain);
        }
        return super.deplacer(terrain,x,y,z,depx,depy,depz,joueur);
    }
}
