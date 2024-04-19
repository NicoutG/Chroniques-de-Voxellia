public class BlocPiston extends BlocActivable {
    private int indiceBloc=-1;
    private int direction=1; /*
                                0 : haut
                                1 : bas
                                2 : avant
                                3 : arrière
                                4 : gauche
                                5 : droite
                             */

    public BlocPiston (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=4)
            return false;
        setEtat(paramList[1].equals("t"));
        setIdGroupe(Integer.parseInt(paramList[2]));
        direction=Integer.parseInt(paramList[3]);
        if (direction<0 || 5<direction)
            return false;
        return true;
    }

    public void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(true);
        recupIdBloc(blocs);
        int [][] val ={{x,y,z+1},{x,y,z-1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int [][] dep ={{0,0,1},{0,0,-1},{0,-1,0},{0,1,0},{-1,0,0},{1,0,0}};
        int xi=val[direction][0];
        int yi=val[direction][1];
        int zi=val[direction][2];
        if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
            int depxi=dep[direction][0];
            int depyi=dep[direction][1];
            int depzi=dep[direction][2];

            // déplacement du bloc
            if (terrain[xi][yi][zi]!=null)
                terrain[xi][yi][zi].deplacer(terrain,blocs,xi,yi,zi,depxi,depyi,depzi,joueur);
            else

                // déplacement du joueur
                if (xi==joueur.getX() && yi==joueur.getY() && zi==joueur.getZ())
                    joueur.deplacer(terrain,blocs,depxi,depyi,depzi);
            
            // placement du bloc de piston
            if (terrain[xi][yi][zi]==null)
                terrain[xi][yi][zi]=new Bloc (indiceBloc);
        }
    }

    public void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(false);
        recupIdBloc(blocs);
        int [][] val ={{x,y,z+1},{x,y,z-1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int xi=val[direction][0];
        int yi=val[direction][1];
        int zi=val[direction][2];
        if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
            if (terrain[xi][yi][zi]!=null && terrain[xi][yi][zi].getIdBlocType()==indiceBloc)
                terrain[xi][yi][zi]=null;
        }
    }

    private void recupIdBloc (BlocType [] blocs) {
        if (indiceBloc<0) {
            String nomBloc="piston"+direction;

            // recherche du bloc de piston
            for (int i=0;i<blocs.length;i++)
                if (nomBloc.equals(blocs[i].getNom())) {
                    indiceBloc=i;
                    return ;
                }
            
            // si le bloc n'a pas été trouvé, on stop le programme
            System.out.println("Le bloc de nom "+nomBloc+" est introuvable");
            System.exit(0);
        }
    }
}
