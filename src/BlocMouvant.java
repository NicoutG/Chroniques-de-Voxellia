
/**
 * Classe BlocMouvant permet la gestion des blocs mouvants.
 */

public class BlocMouvant extends Bloc {

    /**
     * idBloc contient un identifiant de bloc pour les activations de plaques.
     */
    protected int idBloc=0;

    /**
     * depX contient le déplacement précédent sur x du bloc.
     */
    private int depX=0;

    /**
     * depY contient le déplacement précédent sur y du bloc.
     */
    private int depY=0;

    /**
     * tempsMaj contient l'instant de la dernière mise à jour de la position du bloc.
     */
    private long tempsMaj=System.currentTimeMillis();

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocMouvant (int id) {
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
        if (paramList.length>2)
            return false;
        if (paramList.length==2)
            idBloc=Integer.parseInt(paramList[1]);
        else
            idBloc=-1;
        return true;
    }

    /**
     * accesseur de idBloc
     * @return int un identifiant de bloc pour les activations de plaques
     */
    public int getIdBloc () {
        return idBloc;
    }

    /**
     * Envoie une requête de déplacement au bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param depx le déplacement x du bloc
     * @param depy le déplacement y du bloc
     * @param depz le déplacement z du bloc
     * @param joueur le joueur qui joue sur le niveau
     * @param num identifie l'objet qui appelle la fonction
     * @return boolean si le bloc a pu effectuer le déplacement
     */
    @Override
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
        int x2=x+depx;
        int y2=y+depy;
        int z2=z+depz;
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if (terrain[x2][y2][z2]==null) {

                // deplacement du bloc
                if (z==0 || depz!=0 || terrain[x][y][z-1]!=null || num==2) {
                    terrain[x2][y2][z2]=this;
                    terrain[x][y][z]=null;
                    depX=depx;
                    depY=depy;
                    if (z+1<terrain[x][y].length) {
                        if (terrain[x][y][z+1]==null) {
                            if (joueur.getX()==x && joueur.getY()==y && joueur.getZ()==z+1)
                                joueur.deplacer(terrain, blocs, depx, depy, depz,2);
                        }
                        else
                            terrain[x][y][z+1].deplacer(terrain, blocs, x, y, z+1, depx, depy, depz, joueur, 2);
                    }
                    return true;
                }
            }
        }
        return false;
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

        // maj de la position toute les 100 ms
        if (System.currentTimeMillis()-tempsMaj>=100) {
            tempsMaj=System.currentTimeMillis();

            //application de la gravité
            if (deplacer(terrain,blocs,x,y,z,0,0,-1,joueur,1)) {
                if (z>1 && terrain[x][y][z-2]!=null)
                    terrain[x][y][z-2].impacter(terrain,blocs,x,y,z-2,joueur);
            }

            if (depX!=0 || depY!=0) {
                    // si le bloc est sur un bloc de glace ou si c'est un bloc de glace, il glisse
                    boolean avancer=(getBlocType(blocs).getMatiere()=='g'  || (z>0 && terrain[x][y][z-1]!=null && terrain[x][y][z-1].getBlocType(blocs).getMatiere()=='g'));
                    if (avancer) {
                        avancer=deplacer(terrain, blocs, x, y, z, depX, depY, 0, joueur,1);
                    }
                    if (!avancer) {
                        depX=0;
                        depY=0;
                    }
            }
        }
        super.miseAjour(terrain, blocs, x, y, z, joueur);
    }

    /**
     * Affiche les informations du bloc.
     * @param blocs la liste des types de blocs
     */
    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("idBloc : "+idBloc);
    }
}
