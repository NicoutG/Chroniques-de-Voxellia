public class BlocMouvant extends Bloc {
    private int idPlaque=0; // l'identifiant relatif à une plaque d'activation

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
                terrain[x2][y2][z2].miseAjour(terrain,blocs,x2,y2,z2,joueur);

                // mise à jour du bloc qui se trouvait en dessous du bloc
                if (z-1!=z2 && 0<z && terrain[x][y][z-1]!=null) {
                    terrain[x][y][z-1].miseAjour(terrain,blocs,x,y,z-1,joueur);
                }

                // mise à jour du bloc qui se trouvait au dessus du bloc
                if (z+1!=z2 && z+1<terrain[x2][y2].length && terrain[x][y][z+1]!=null) {
                    terrain[x][y][z+1].miseAjour(terrain,blocs,x,y,z+1,joueur);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("idPlaque : "+idPlaque);
    }
}
