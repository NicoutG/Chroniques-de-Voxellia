public class BlocPlaque extends BlocActivation {

    public BlocPlaque (BlocType bloc) {
        super(bloc);
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,x,y,z,joueur);
        if (z+1<terrain[x][y].length) {
            if (getEtat()) {
                if ((joueur.getX()!=x || joueur.getY()!=y || joueur.getZ()!=z+1) && terrain[x][y][z+1]==null) {
                    setEtat(false);
                    activer(terrain);
                }
            }
            else {
                if ((joueur.getX()==x && joueur.getY()==y && joueur.getZ()==z+1) || terrain[x][y][z+1]!=null) {
                    setEtat(true);
                    activer(terrain);
                }
            }
        }
    }
}
