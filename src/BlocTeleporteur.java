/**
 * Classe BlocTeleporteur permet la gestion des blocs de téléportation.
 */

public class BlocTeleporteur extends BlocActivable {

    /**
     * idTele contient l'identifiant qui permet de relier plusieurs téléporteurs entre eux.
     */
    private int idTele=0;

    /**
     * attente contient un numéro pour savoir qu'un objet vient de se téléporter pour ne pas le téléporter en boucle 
     * tant qu'il ne quitte pas le téléporteur (0 rien, 1 joueur, 2 bloc mouvant).
     */
    private int attente=0;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocTeleporteur (int id) {
        super(id);
    }

    /**
     * Charge les paramètres du bloc à partir d'une chaine de caractères.
     * @param params la chaine qui contient les informations sur les paramètres du bloc
     * @return boolean si le chargement des paramètres a bien été effectué
     */
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

    /**
     * accesseur de idTele
     * @return int l'identifiant qui permet de relier plusieurs téléporteurs entre eux
     */
    public int getIdTele () {
        return idTele;
    }

    /**
     * mutateur de attente
     * @param a un numéro pour savoir qu'un objet vient de se téléporter
     */
    private void setAttente (int a) {
        attente=a;
    }

    /**
     * Active le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(true);
    }

    /**
     * Désactive le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(false);
    }

    /**
     * Téléporte le joueur à un téléporteur.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du téléporteur d'arrivé dans le terrain
     * @param y la position y du téléporteur d'arrivé dans le terrain
     * @param z la position z du téléporteur d'arrivé dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    private boolean teleporterJoueur (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int [][] val ={{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z},{x,y,z-1}};
        int xi,yi,zi;

        // cherche une case libre autour du téléporteur d'arrivé
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

    /**
     * Téléporte le joueur à un téléporteur.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param xb la position x du bloc à téléporter dans le terrain
     * @param yb la position y du bloc à téléporter dans le terrain
     * @param zb la position z du bloc à téléporter dans le terrain
     * @param x la position x du téléporteur d'arrivé dans le terrain
     * @param y la position y du téléporteur d'arrivé dans le terrain
     * @param z la position z du téléporteur d'arrivé dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    private boolean teleporterBloc (Bloc [][][] terrain, BlocType [] blocs, int xb, int yb, int zb, int x, int y, int z, Joueur joueur) {
        int [][] val ={{x,y,z+1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z},{x,y,z-1}};
        int xi,yi,zi;

        // cherche une case libre autour du téléporteur d'arrivé
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

    /**
     * Renvoie 0 si il n'y a rien à téléporter autour, 1 si il y a le joueur autour et 2 si il y a un bloc mouvant autour.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du téléporteur dans le terrain
     * @param y la position y du téléporteur dans le terrain
     * @param z la position z du téléporteur dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     * @return int 0 si il n'y a rien à téléporter autour, 1 si il y a le joueur autour et 2 si il y a un bloc mouvant autour
     */
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

    /**
     * Met à jour le bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
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

    /**
     * Renvoie la texture actuelle du bloc.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     * @return String la texture actuelle du bloc
     */
    @Override
    public String getTexture (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (getEtat()) {
            String texture=super.getTexture(terrain, blocs, x, y, z, joueur);
            String [] text=texture.split("\\.");
            return text[0]+"-T."+text[1];
        }
        String texture=getBlocType(blocs).getTexture();
        String [] text=texture.split("\\.");
        return text[0]+"-F."+text[1];
    }

    /**
     * Affiche les informations du bloc.
     * @param blocs la liste des types de blocs
     */
    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Id teleporteur : "+idTele);
    }

}
