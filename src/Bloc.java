public class Bloc {
    private int idBlocType=1;

    public Bloc (int id) {
        idBlocType=id;
    }

    public int getIdBlocType () {
        return idBlocType;
    }

    public BlocType getBlocType (BlocType [] blocs) {
        return blocs[idBlocType];
    }

    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur) {
        return false;
    }

    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {

        // teste les interractions avec les blocs autour de lui et interragit
        if (x+1<terrain.length && terrain[x+1][y][z]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x+1][y][z],joueur);
            terrain[x+1][y][z].interraction(terrain,blocs,x+1,y,z,terrain[x][y][z],joueur);
        }
        if (1<=x && terrain[x-1][y][z]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x-1][y][z],joueur);
            terrain[x-1][y][z].interraction(terrain,blocs,x-1,y,z,terrain[x][y][z],joueur);
        }
        if (y+1<terrain[x].length && terrain[x][y+1][z]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x][y+1][z],joueur);
            terrain[x][y+1][z].interraction(terrain,blocs,x,y+1,z,terrain[x][y][z],joueur);
        }
        if (1<=y && terrain[x][y-1][z]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x][y-1][z],joueur);
            terrain[x][y-1][z].interraction(terrain,blocs,x,y-1,z,terrain[x][y][z],joueur);
        }
        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x][y][z+1],joueur);
            terrain[x][y][z+1].interraction(terrain,blocs,x,y,z+1,terrain[x][y][z],joueur);
        }
        if (1<=z && terrain[x][y][z-1]!=null) {
            this.interraction(terrain,blocs,x,y,z,terrain[x][y][z-1],joueur);
            terrain[x][y][z-1].interraction(terrain,blocs,x,y,z-1,terrain[x][y][z],joueur);
        }
    }

    public void interraction (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Bloc blocInter, Joueur joueur) {
        if (blocInter!=null) {
            BlocType typeBlocInter=blocInter.getBlocType(blocs);
            char matiere=getBlocType(blocs).getMatiere();
            switch (typeBlocInter.getMatiere()) {
                case 'f': { // Si le bloc d'interraction est du feu
                    if (matiere=='b' || matiere=='g') { // Si ce bloc est en bois ou en glace
                        terrain[x][y][z]=null;
                        if (z+1<terrain[x][y].length && terrain[x][y][z+1]!=null)
                            terrain[x][y][z+1].miseAjour(terrain,blocs,x,y,z+1,joueur);
                        if (0<z && terrain[x][y][z-1]!=null)
                            terrain[x][y][z-1].miseAjour(terrain,blocs,x,y,z-1,joueur);
                    }
                }break;
            }
        }
    }

    public void afficher (BlocType [] blocs) {
        getBlocType(blocs).afficher();
    }

}
