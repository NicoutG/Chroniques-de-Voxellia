public class BlocMouvant extends Bloc {
    private int idPlaque=0; // l'identifiant relatif à une plaque d'activation
    private int depX=0;
    private int depY=0;

    public BlocMouvant (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length>2)
            return false;
        if (paramList.length==2)
            idPlaque=Integer.parseInt(paramList[1]);
        else
            idPlaque=-1;
        return true;
    }

    public int getIdPlaque () {
        return idPlaque;
    }

    public void setIdPlaque (int id) {
        idPlaque=id;
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
        if (depX!=0 || depY!=0) {
            System.out.println("depx : "+depX+" depy : "+depY);
            // si le bloc est sur un bloc de glace, il glisse
            if (z>0 && terrain[x][y][z-1]!=null && terrain[x][y][z-1].getBlocType(blocs).getMatiere()=='g')
                deplacer(terrain, blocs, x, y, z, depX, depY, 0, joueur);
            else {
                depX=0;
                depY=0;
            }
        }
        super.miseAjour(terrain, blocs, x, y, z, joueur);
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("idPlaque : "+idPlaque);
    }
}
