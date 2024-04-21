public class BlocMouvant extends Bloc {
    protected int idBloc=0; // l'identifiant relatif à une plaque d'activation
    private int depX=0;
    private int depY=0;
    private long tempsMaj=System.currentTimeMillis();

    public BlocMouvant (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length>2)
            return false;
        if (paramList.length==2)
            idBloc=Integer.parseInt(paramList[1]);
        else
            idBloc=-1;
        return true;
    }

    public int getIdPlaque () {
        return idBloc;
    }

    public void setIdPlaque (int id) {
        idBloc=id;
    }

    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        int x2=x+depx;
        int y2=y+depy;
        int z2=z+depz;
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if (terrain[x2][y2][z2]==null) {

                // deplacement du bloc
                terrain[x2][y2][z2]=this;
                terrain[x][y][z]=null;
                depX=depx;
                depY=depy;
                return true;
            }
        }
        return false;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {

        // maj de la position toute les 100 ms
        if (System.currentTimeMillis()-tempsMaj>=100) {
            tempsMaj=System.currentTimeMillis();

            //application de la gravité
            if (deplacer(terrain,blocs,x,y,z,0,0,-1,joueur)) {
                if (z>1 && terrain[x][y][z-2]!=null)
                    terrain[x][y][z-2].impacter(terrain,blocs,x,y,z-2,joueur);
            }

            if (depX!=0 || depY!=0) {
                    // si le bloc est sur un bloc de glace ou si c'est un bloc de glace, il glisse
                    boolean avancer=(getBlocType(blocs).getMatiere()=='g'  || (z>0 && terrain[x][y][z-1]!=null && terrain[x][y][z-1].getBlocType(blocs).getMatiere()=='g'));
                    if (avancer) {
                        avancer=deplacer(terrain, blocs, x, y, z, depX, depY, 0, joueur);
                    }
                    if (!avancer) {
                        depX=0;
                        depY=0;
                    }
            }
        }
        super.miseAjour(terrain, blocs, x, y, z, joueur);
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("idBloc : "+idBloc);
    }
}
