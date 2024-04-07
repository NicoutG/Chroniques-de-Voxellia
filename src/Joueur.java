public class Joueur {
    private int x;
    private int y;
    private int z;

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

    public boolean deplacer (Bloc [][][] terrain, int depx, int depy, int depz) {
        int x2=x+depx;
        int y2=y+depy;
        int z2=z+depz;
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if (terrain[x2][y2][z2]==null || deplacer(terrain,depx,depy,depz)) {
                x=x2;
                x2=x-depx;
                y=y2;
                y2=y-depy;
                z=z2;
                z2=z-depz;

                // mise à jour du bloc precedement sous le joueur
                if (z2>0 && terrain[x2][y2][z2-1]!=null)
                    terrain[x2][y2][z2-1].miseAjour(terrain, x2, y2, z2-1,this);
                
                // application de la gravite
                while (z>0 && terrain[x][y][z-1]==null)
                    z--;
                
                // mise à jour du bloc desormais sous le joueur
                if (z>0)
                    terrain[x][y][z-1].miseAjour(terrain, x, y, z-1,this);
                return true;
            }
        }
        return false;
    }
}
