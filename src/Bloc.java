public class Bloc {
    private BlocType typeBloc=null;

    public Bloc (BlocType bloc) {
        typeBloc=bloc;
    }

    public BlocType getTypeBloc () {
        return typeBloc;
    }

    public boolean deplacer (Bloc [][][] terrain, int x, int y, int z, int depx, int depy, int depz) {
        if (typeBloc.getMouvant()) {
            int x2=x+depx;
            int y2=y+depy;
            int z2=z+depz;
            if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
                if (terrain[x2][y2][z2]==null) {

                    // deplacement du bloc
                    terrain[x2][y2][z2]=this;
                    terrain[x][y][z]=null;
                    terrain[x2][y2][z2].miseAjour(terrain,x2,y2,z2);

                    // mise à jour du bloc qui se trouvait au dessus du bloc
                    if (z+1!=z2 && z+1<terrain[x2][y2].length && terrain[x][y][z+1]!=null) {
                        terrain[x][y][z+1].miseAjour(terrain,x,y,z+1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void miseAjour (Bloc [][][] terrain, int x, int y, int z) {

        // teste les interractions avec les blocs autour de lui et interragit
        if (x+1<terrain.length && terrain[x+1][y][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x+1][y][z]);
            terrain[x+1][y][z].interraction(terrain,x+1,y,z,terrain[x][y][z]);
        }
        if (1<=x && terrain[x-1][y][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x-1][y][z]);
            terrain[x-1][y][z].interraction(terrain,x-1,y,z,terrain[x][y][z]);
        }
        if (y+1<terrain[x].length && terrain[x][y+1][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y+1][z]);
            terrain[x][y+1][z].interraction(terrain,x,y+1,z,terrain[x][y][z]);
        }
        if (1<=y && terrain[x][y-1][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y-1][z]);
            terrain[x][y-1][z].interraction(terrain,x,y-1,z,terrain[x][y][z]);
        }
        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y][z+1]);
            terrain[x][y][z+1].interraction(terrain,x,y,z+1,terrain[x][y][z]);
        }
        if (1<=z && terrain[x][y][z-1]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y][z-1]);
            terrain[x][y][z-1].interraction(terrain,x,y,z-1,terrain[x][y][z]);
        }

        // application de la gravite
        if (terrain[x][y][z]!=null && typeBloc.getMouvant()) {
            if (z>0 && terrain[x][y][z-1]==null) {
                terrain[x][y][z-1]=this;
                terrain[x][y][z]=null;
                terrain[x][y][z-1].miseAjour(terrain,x,y,z-1);

                // application de la gravite sur le bloc du dessus
                if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null)
                    terrain[x][y][z+1].miseAjour(terrain,x,y,z+1);
            }
        }
    }

    public void interraction (Bloc [][][] terrain, int x, int y, int z, Bloc blocInter) {
        if (blocInter!=null) {
            BlocType typeBlocInter=blocInter.getTypeBloc();
            char matiere=typeBloc.getMatiere();
            switch (typeBlocInter.getMatiere()) {
                case 'f': { // Si le bloc d'interraction est du feu
                    if (matiere=='b' || matiere=='g') { // Si ce bloc est en bois ou en glace
                        terrain[x][y][z]=null;
                        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null)
                            terrain[x][y][z+1].miseAjour(terrain,x,y,z+1);
                    }
                }break;
            }
        }
    }

    public void afficher () {
        typeBloc.afficher();
    }

}
