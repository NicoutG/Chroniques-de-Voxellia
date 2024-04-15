public class Joueur {
    private int x;
    private int y;
    private int z;
    private int depX=0;
    private int depY=0;
    private boolean victoire;

    public void setPos(int posx, int posy, int posz) {
        x=posx;
        y=posy;
        z=posz;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public int getZ () {
        return z;
    }

    public boolean getVictoire () {
        return victoire;
    }

    public void setVictoire (boolean v) {
        victoire=v;
    }

    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int depx, int depy, int depz) {
        int x2=x+depx;
        int y2=y+depy;
        int z2=z+depz;
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if (terrain[x2][y2][z2]==null || terrain[x2][y2][z2].deplacer(terrain,blocs,x2,y2,z2,depx,depy,depz,this)) {
                depX=depx;
                depY=depy;
                x=x2;
                x2=x-depx;
                y=y2;
                y2=y-depy;
                z=z2;
                z2=z-depz;
                return true;
            }
        }
        return false;
    }

    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs) {
        if (depX!=0 || depY!=0) {

            // si le joueur est sur un bloc de glace, il glisse
            if (z>0 && terrain[x][y][z]!=null && terrain[x][y][z].getBlocType(blocs).getMatiere()=='g')
                deplacer(terrain,blocs,depX,depY,0);
            else {
                depX=0;
                depY=0;
            }
        }
    }

}
