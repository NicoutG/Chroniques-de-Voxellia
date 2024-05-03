public class BlocLevier extends BlocActivation {

    public BlocLevier (int id) {
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
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
        if ((depx!=0 || depy!=0) && num==0) {
            setEtat(!getEtat());
            if (getEtat())
                activer(terrain,blocs,joueur);
            else
                desactiver(terrain,blocs,joueur);
        }
        return super.deplacer(terrain,blocs,x,y,z,depx,depy,depz,joueur,1);
    }

}
