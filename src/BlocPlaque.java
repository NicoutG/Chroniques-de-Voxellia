public class BlocPlaque extends BlocActivation {
    private boolean valNonActif;
    private int idBloc; /*
                            -2 = joueur et blocs peuvent activer
                            -1 = seuls les blocs peuvent activer
                            Autres = seuls les blocs du même identifiants peuvent activer
                         */

    public BlocPlaque (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=3 && paramList.length!=4)
            return false;
        valNonActif=!paramList[1].equals("t");
        setEtat(valNonActif);
        setIdGroupe(Integer.parseInt(paramList[2]));
        if (paramList.length==3)
            idBloc=-2;
        else
            idBloc=Integer.parseInt(paramList[3]);
        return true;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (z+1<terrain[x][y].length) {
            boolean actif=false;
            switch (idBloc) {
                case -2: actif=(joueur.getX()==x && joueur.getY()==y && joueur.getZ()==z+1) || (terrain[x][y][z+1]!=null && terrain[x][y][z+1] instanceof BlocMouvant);break;
                case -1: actif=terrain[x][y][z+1]!=null && terrain[x][y][z+1] instanceof BlocMouvant;break;
                default: actif=terrain[x][y][z+1] instanceof BlocMouvant && ((BlocMouvant)terrain[x][y][z+1]).getIdPlaque()==idBloc;break;
            }
            
            // activation ou désactivation de la plaque
            if (getEtat()!=valNonActif) {
                if (!actif) {
                    desactiver(terrain,blocs,joueur);
                    setEtat(true);
                }
            }
            else {
                if (actif) {
                    activer(terrain,blocs,joueur);
                    setEtat(false);
                }
            }
        }
        super.miseAjour(terrain,blocs,x,y,z,joueur);
    }
}
