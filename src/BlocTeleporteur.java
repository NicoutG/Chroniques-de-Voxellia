public class BlocTeleporteur extends BlocActivable {
    private int idTele=0;
    private int attente=0;

    public BlocTeleporteur (int id) {
        super(id);
    }

    @Override
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=4)
            return false;
        setEtat(paramList[1].equals("t"));
        setIdGroupe(Integer.parseInt(paramList[2]));
        idTele=Integer.parseInt(paramList[3]);
        return true;
    }

    public int getIdTele () {
        return idTele;
    }

    public void setAttente (int a) {
        attente=a;
    }

    public void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(true);
    }

    public void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(false);
    }

    private boolean teleporterJoueur (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int [][] val ={{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z},{x,y,z-1}};
        int xi,yi,zi;
        for (int i=0;i<6;i++) {
            xi=val[i][0];
            yi=val[i][1];
            zi=val[i][2];
            if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                if (terrain[xi][yi][zi]==null) {
                    joueur.setPos(xi,yi,zi);
                    if (terrain[x][y][z] instanceof BlocTeleporteur)
                        ((BlocTeleporteur)terrain[x][y][z]).setAttente(1);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean teleporterBloc (Bloc [][][] terrain, BlocType [] blocs, int xb, int yb, int zb, int x, int y, int z, Joueur joueur) {
        int [][] val ={{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z},{x,y,z-1}};
        int xi,yi,zi;
        for (int i=0;i<6;i++) {
            xi=val[i][0];
            yi=val[i][1];
            zi=val[i][2];
            if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                if (terrain[xi][yi][zi]==null) {
                    terrain[xi][yi][zi]=terrain[xb][yb][zb];
                    terrain[xb][yb][zb]=null;
                    if (terrain[x][y][z] instanceof BlocTeleporteur)
                        ((BlocTeleporteur)terrain[x][y][z]).setAttente(2);
                    return true;
                }
            }
        }
        return false;
    }

    private int verifAutour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int xj=joueur.getX();
        int yj=joueur.getY();
        int zj=joueur.getZ();
        int [][] val ={{x,y,z-1},{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int xi,yi,zi;
        for (int i=0;i<6;i++) {
            xi=val[i][0];
            yi=val[i][1];
            zi=val[i][2];
            if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                if (xj==xi && yj==yi && zj==zi)
                    return 1;
                if (terrain[xi][yi][zi] instanceof BlocMouvant)
                    return 2;
            }
        }
        return 0;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,blocs,x,y,z,joueur);

        // téléportation des blocs et du joueur
        if (getEtat()) {
            int autour=verifAutour(terrain, blocs, x, y, z, joueur);
            if (attente!=autour)
                attente=0;
            if (attente!=autour) {
                int xtp=0;
                int ytp=0;
                int ztp=0;

                // recherche d'un téléporteur de destination
                boolean trouver=false;
                for (int xi=0;xi<terrain.length;xi++)
                    for (int yi=0;yi<terrain[xi].length;yi++)
                        for (int zi=0;zi<terrain[xi][yi].length;zi++)
                            if ((xi!=x || yi!=y || zi!=z) && terrain[xi][yi][zi] instanceof BlocTeleporteur && idTele==((BlocTeleporteur)terrain[xi][yi][zi]).getIdTele()) {
                                trouver=true;
                                xtp=xi;
                                ytp=yi;
                                ztp=zi;
                            }
                if (trouver) {

                    // si il faut téléporter le joueur
                    if (autour==1) {
                        int xj=joueur.getX();
                        int yj=joueur.getY();
                        int zj=joueur.getZ();
                        int [][] val ={{x,y,z-1},{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
                        int xi,yi,zi;
                        for (int i=0;i<6;i++) {
                            xi=val[i][0];
                            yi=val[i][1];
                            zi=val[i][2];
                            if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                                if (xj==xi && yj==yi && zj==zi) {
                                    teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                                    return ;
                                }
                            }
                        }
                    }
                    else {

                        // si il faut téléporter un bloc mouvant
                        if (autour==2) {
                            int [][] val ={{x,y,z-1},{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
                            int xi,yi,zi;
                            for (int i=0;i<6;i++) {
                                xi=val[i][0];
                                yi=val[i][1];
                                zi=val[i][2];
                                if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
                                    if (terrain[xi][yi][zi] instanceof BlocMouvant) {
                                        teleporterBloc(terrain,blocs,xi,yi,zi,xtp,ytp,ztp,joueur);
                                        return ;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
