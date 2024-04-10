public class Bloc {
    private BlocType typeBloc=null;

    public Bloc (BlocType bloc) {
        typeBloc=bloc;
    }

    public BlocType getTypeBloc () {
        return typeBloc;
    }

    public boolean deplacer (Bloc [][][] terrain, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        return false;
    }

    public void miseAjour (Bloc [][][] terrain, int x, int y, int z, Joueur joueur) {

        // teste les interractions avec les blocs autour de lui et interragit
        if (x+1<terrain.length && terrain[x+1][y][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x+1][y][z],joueur);
            terrain[x+1][y][z].interraction(terrain,x+1,y,z,terrain[x][y][z],joueur);
        }
        if (1<=x && terrain[x-1][y][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x-1][y][z],joueur);
            terrain[x-1][y][z].interraction(terrain,x-1,y,z,terrain[x][y][z],joueur);
        }
        if (y+1<terrain[x].length && terrain[x][y+1][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y+1][z],joueur);
            terrain[x][y+1][z].interraction(terrain,x,y+1,z,terrain[x][y][z],joueur);
        }
        if (1<=y && terrain[x][y-1][z]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y-1][z],joueur);
            terrain[x][y-1][z].interraction(terrain,x,y-1,z,terrain[x][y][z],joueur);
        }
        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y][z+1],joueur);
            terrain[x][y][z+1].interraction(terrain,x,y,z+1,terrain[x][y][z],joueur);
        }
        if (1<=z && terrain[x][y][z-1]!=null) {
            this.interraction(terrain,x,y,z,terrain[x][y][z-1],joueur);
            terrain[x][y][z-1].interraction(terrain,x,y,z-1,terrain[x][y][z],joueur);
        }
    }

    public void interraction (Bloc [][][] terrain, int x, int y, int z, Bloc blocInter, Joueur joueur) {
        if (blocInter!=null) {
            BlocType typeBlocInter=blocInter.getTypeBloc();
            char matiere=typeBloc.getMatiere();
            switch (typeBlocInter.getMatiere()) {
                case 'f': { // Si le bloc d'interraction est du feu
                    if (matiere=='b' || matiere=='g') { // Si ce bloc est en bois ou en glace
                        terrain[x][y][z]=null;
                        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null)
                            terrain[x][y][z+1].miseAjour(terrain,x,y,z+1,joueur);
                        if (0<z && terrain[x][y][z-1]!=null)
                            terrain[x][y][z-1].miseAjour(terrain,x,y,z-1,joueur);
                    }
                }break;
            }
        }
    }

    public void afficher () {
        typeBloc.afficher();
    }

}
