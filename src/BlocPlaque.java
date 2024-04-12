public class BlocPlaque extends BlocActivation {

    public BlocPlaque (int id) {
        super(id);
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,blocs,x,y,z,joueur);
        if (z+1<terrain[x][y].length) {
            if (getEtat()) {
                if ((joueur.getX()!=x || joueur.getY()!=y || joueur.getZ()!=z+1) && terrain[x][y][z+1]==null) {
                    setEtat(false);
                    activer(terrain,blocs);
                }
            }
            else {
                if ((joueur.getX()==x && joueur.getY()==y && joueur.getZ()==z+1) || terrain[x][y][z+1]!=null) {
                    setEtat(true);
                    activer(terrain,blocs);
                }
            }
        }
    }
}
