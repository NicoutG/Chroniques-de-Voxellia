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
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        if (depx!=0 || depy!=z) {
            setEtat(!getEtat());
            activer(terrain,blocs);
        }
        return super.deplacer(terrain,blocs,x,y,z,depx,depy,depz,joueur);
    }
}
