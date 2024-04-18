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

    public void attente (int a) {
        attente=a;
    }

    public void activation () {
        setEtat(true);
    }

    public void desactivation () {
        setEtat(false);
    }

    private boolean teleporterJoueur (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (z+1<terrain[x][y].length && terrain[x][y][z+1]==null) {
            joueur.setPos(x,y,z+1);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        if (y>0 && terrain[x][y-1][z]==null) {
            joueur.setPos(x,y-1,z);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        if (y+1<terrain[x].length && terrain[x][y+1][z]==null) {
            joueur.setPos(x,y+1,z);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        if (x>0 && terrain[x-1][y][z]==null) {
            joueur.setPos(x-1,y,z);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        if (x+1<terrain.length && terrain[x+1][y][z]==null) {
            joueur.setPos(x+1,y,z);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        if (z>0 && terrain[x][y][z-1]==null) {
            joueur.setPos(x,y,z-1);
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(1);
            return true;
        }
        return false;
    }

    private boolean teleporterBloc (Bloc [][][] terrain, BlocType [] blocs, int xb, int yb, int zb, int x, int y, int z, Joueur joueur) {
        if (z+1<terrain[x][y].length && terrain[x][y][z+1]==null) {
            terrain[x][y][z+1]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        if (y>0 && terrain[x][y-1][z]==null) {
            terrain[x][y-1][z]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        if (y+1<terrain[x].length && terrain[x][y+1][z]==null) {
            terrain[x][y+1][z]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        if (x>0 && terrain[x-1][y][z]==null) {
            terrain[x-1][y][z]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        if (x+1<terrain.length && terrain[x+1][y][z]==null) {
            terrain[x+1][y][z]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        if (z>0 && terrain[x][y][z-1]==null) {
            terrain[x][y][z-1]=terrain[xb][yb][zb];
            terrain[xb][yb][zb]=null;
            if (terrain[x][y][z] instanceof BlocTeleporteur)
                ((BlocTeleporteur)terrain[x][y][z]).attente(2);
            return true;
        }
        return false;
    }

    private int verifAutour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int xj=joueur.getX();
        int yj=joueur.getY();
        int zj=joueur.getZ();
        if (z>0) {
            if (xj==x && yj==y && zj==z-1)
                return 1;
            if (terrain[x][y][z-1] instanceof BlocMouvant)
                return 2;
        }
        if (z+1<terrain[x][y].length) {
            if (xj==x && yj==y && zj==z+1)
                return 1;
            if (terrain[x][y][z+1] instanceof BlocMouvant)
                return 2;
        }
        if (y>0) {
            if (xj==x && yj==y-1 && zj==z)
                return 1;
            if (terrain[x][y-1][z] instanceof BlocMouvant)
                return 2;
        }
        if (y+1<terrain[x].length) {
            if (xj==x && yj==y+1 && zj==z)
                return 1;
            if (terrain[x][y+1][z] instanceof BlocMouvant)
                return 2;
        }
        if (x>0) {
            if (xj==x-1 && yj==y && zj==z)
                return 1;
            if (terrain[x-1][y][z] instanceof BlocMouvant)
                return 2;
        }
        if (x+1<terrain.length) {
            if (xj==x+1 && yj==y && zj==z)
                return 1;
            if (terrain[x+1][y][z] instanceof BlocMouvant)
                return 2;
        }
        return 0;
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,blocs,x,y,z,joueur);

        // téléportation des blocs et du joueur
        if (getEtat()) {
            int autour=verifAutour(terrain, blocs, x, y, z, joueur);
            System.out.println(attente+" "+autour);
            if (attente!=autour) {
                attente=0;
                int xtp=0;
                int ytp=0;
                int ztp=0;
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
                    int xj=joueur.getX();
                    int yj=joueur.getY();
                    int zj=joueur.getZ();
                    if (z+1<terrain[x][y].length) {
                        if (xj==x && yj==y && zj==z+1) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x][y][z+1] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x,y,z+1,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                    if (z>0) {
                        if (xj==x && yj==y && zj==z-1) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x][y][z-1] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x,y,z-1,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                    if (y+1<terrain[x].length) {
                        if (xj==x && yj==y+1 && zj==z) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x][y+1][z] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x,y+1,z,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                    if (y>0) {
                        if (xj==x && yj==y-1 && zj==z) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x][y-1][z] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x,y-1,z,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                    if (x+1<terrain.length) {
                        if (xj==x+1 && yj==y && zj==z) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x+1][y][z] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x+1,y,z,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                    if (x>0) {
                        if (xj==x-1 && yj==y && zj==z) {
                            teleporterJoueur(terrain,blocs,xtp,ytp,ztp,joueur);
                            return ;
                        }
                        if (terrain[x-1][y][z] instanceof BlocMouvant) {
                            teleporterBloc(terrain,blocs,x-1,y,z,xtp,ytp,ztp,joueur);
                            return ;
                        }
                    }
                }
            }
        }
    }
}
