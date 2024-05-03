

public class Bloc {
    private int idBlocType=1;
    private long tempsImage=0;
    private int numImage;

    public Bloc (int id) {
        idBlocType=id;
    }

    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=1)
            return false;
        return true;
    }

    public int getIdBlocType () {
        return idBlocType;
    }

    public BlocType getBlocType (BlocType [] blocs) {
        return blocs[idBlocType];
    }

    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
        return false;
    }

    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {

        // teste les interractions avec les blocs autour de lui et interragit
        int [][] val ={{x,y,z-1},{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int xi,yi,zi;
        for (int i=0;i<6;i++) {
            xi=val[i][0];
            yi=val[i][1];
            zi=val[i][2];
            if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                if (terrain[xi][yi][zi]!=null) {
                    interraction(terrain,blocs,x,y,z,terrain[xi][yi][zi],joueur);
                    terrain[xi][yi][zi].interraction(terrain,blocs,xi,yi,zi,terrain[x][y][z],joueur);
                }
            }
        }
    }

    public void interraction (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Bloc blocInter, Joueur joueur) {
        if (blocInter!=null) {
            BlocType typeBlocInter=blocInter.getBlocType(blocs);
            char matiere=getBlocType(blocs).getMatiere();
            switch (typeBlocInter.getMatiere()) {
                case 'f': { // Si le bloc d'interraction est du feu
                    if (matiere=='b' || matiere=='g') // Si ce bloc est en bois ou en glace
                        terrain[x][y][z]=null;
                }break;
            }
        }
    }

    public void impacter (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (getBlocType(blocs).getDestructible()) {
            terrain[x][y][z]=null;
        }
    }

    public String getTexture (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int num=getNumImage(blocs);
        if (num==-1)
            return getBlocType(blocs).getTexture();
        String [] text=getBlocType(blocs).getTexture().split("\\.");
        return text[0]+"-"+num+"."+text[1];
    }

    private int getNumImage (BlocType [] blocs) {
        int nbImages=getBlocType(blocs).getNbImages();
        if (nbImages==1)
            return -1;
        if (System.currentTimeMillis()-tempsImage>=100) {
            tempsImage=System.currentTimeMillis();
            numImage=(numImage+1)%nbImages;
        }
        return numImage;
    }

    public void afficher (BlocType [] blocs) {
        getBlocType(blocs).afficher();
    }

}
