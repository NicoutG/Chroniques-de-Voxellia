public class Joueur {
    private int x;
    private int y;
    private int z;
    private int depX=0;
    private int depY=0;
    private boolean victoire;
    private long tempsMaj=System.currentTimeMillis();

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
        int x2=x;
        int y2=y;
        int z2=z+depz;

        // si le joueur a les pieds sur le sol
        if (z-1>=0 && terrain[x][y][z-1]!=null) {
            x2=x+depx;
            y2=y+depy;
        }
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if (terrain[x2][y2][z2]==null || terrain[x2][y2][z2].deplacer(terrain,blocs,x2,y2,z2,depx,depy,depz,this)) {
                if (z-1>=0 && terrain[x][y][z-1]!=null && depX==0 && depY==0) {
                    depX=depx;
                    depY=depy;
                }
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

        // maj de la position toute les 100 ms
        if (System.currentTimeMillis()-tempsMaj>=100) {
            tempsMaj=System.currentTimeMillis();

            // application de la gravité
            deplacer(terrain,blocs,0,0,-1);

            if (depX!=0 || depY!=0) {
                    // si le bloc est sur un bloc de glace, il glisse
                    boolean avancer=(z>0 && terrain[x][y][z-1]!=null && terrain[x][y][z-1].getBlocType(blocs).getMatiere()=='g');
                    if (avancer) {
                        avancer=deplacer(terrain, blocs,depX,depY,0);
                    }
                    if (!avancer) {
                        depX=0;
                        depY=0;
                    }
            }
        }
    }

}
