public class BlocPlaque extends BlocActivation {

    public BlocPlaque (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=3)
            return false;
        super.setEtat(paramList[1].equals("t"));
        super.setIdGroupe(Integer.parseInt(paramList[2]));
        return true;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,blocs,x,y,z,joueur);
        if (z+1<terrain[x][y].length) {
            if (getEtat()) {
                if ((joueur.getX()!=x || joueur.getY()!=y || joueur.getZ()!=z+1) && terrain[x][y][z+1]==null) {
                    setEtat(false);
                    desactiver(terrain,blocs);
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
