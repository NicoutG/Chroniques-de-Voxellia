

/**
 * Classe Joueur permet la gestion du personnage.
 */

public class Joueur {

    /**
     * x contient la coordonnée x du joueur.
     */
    private int x;

    /**
     * y contient la coordonnée y du joueur.
     */
    private int y;

    /**
     * z contient la coordonnée z du joueur.
     */
    private int z;

    /**
     * depX contient le déplacement précédent sur x du joueur.
     */
    private int depX=0;

    /**
     * depY contient le déplacement précédent sur y du joueur.
     */
    private int depY=0;

    /**
     * victoire contient si le joueur a réussi le niveau.
     */
    private boolean victoire;

    /**
     * mort contient si le joueur est mort.
     */
    private boolean mort;

    /**
     * tempsMaj contient l'instant de la dernière mise à jour de la position du joueur.
     */
    private long tempsMaj=System.currentTimeMillis();

    /**
     * tempsImage contient l'instant du dernier changement de texture du joueur.
     */
    private long tempsImage=0;

    /**
     * numImage contient le numéro actuel de la texture du joueur.
     */
    private int numImage;

    /**
     * accesseur de x
     * @return int la position x du joueur
     */
    public int getX () {
        return x;
    }

    /**
     * accesseur de y
     * @return int la position y du joueur
     */
    public int getY () {
        return y;
    }

    /**
     * accesseur de z
     * @return int la position z du joueur
     */
    public int getZ () {
        return z;
    }

    /**
     * mutateur de x, y et z
     * @param posx la position x du joueur
     * @param posy la position y du joueur
     * @param posz la position z du joueur
     */
    public void setPos(int posx, int posy, int posz) {
        x=posx;
        y=posy;
        z=posz;
    }

    /**
     * accesseur de victoire
     * @return boolean si le joueur a réussi le niveau
     */
    public boolean getVictoire () {
        return victoire;
    }

    /**
     * mutateur de victoire
     * @param v si le joueur a réussi le niveau
     */
    public void setVictoire (boolean v) {
        victoire=v;
    }

    /**
     * accesseur de mort
     * @return boolean si le joueur est mort
     */
    public boolean getMort () {
        return mort;
    }

    /**
     * mutateur de mort
     * @return m si le joueur est mort
     */
    public void setMort (boolean m) {
        mort=m;
    }

    /**
     * Envoie une requête de déplacement au joueur.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     * @param depx le déplacement x du joueur
     * @param depy le déplacement y du joueur
     * @param depz le déplacement z du joueur
     * @param num identifie l'objet qui appelle la fonction
     * @return boolean si le joueur a pu effectuer le déplacement
     */
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int depx, int depy, int depz, int num) {
        int x2=x+depx;
        int y2=y+depy;
        int z2=z+depz;
        if (0<=x2 && x2<terrain.length && 0<=y2 && y2<terrain[x2].length && 0<=z2 && z2<terrain[x2][y2].length) {
            if ((z==0 || depz!=0 || num==2 || (terrain[x][y][z-1]!=null && !(terrain[x][y][z-1] instanceof BlocMortel))) && (terrain[x2][y2][z2]==null || terrain[x2][y2][z2].deplacer(terrain,blocs,x2,y2,z2,depx,depy,depz,this,0))) {
                depX=depx;
                depY=depy;
                x=x2;
                x2=x-depx;
                y=y2;
                y2=y-depy;
                z=z2;
                z2=z-depz;
                return true;
            }
        }
        if (z==0)
            setMort(true);
        return false;
    }

    /**
     * Met à jour le joueur.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     */
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs) {

        // maj de la position toute les 100 ms
        if (System.currentTimeMillis()-tempsMaj>=100) {
            tempsMaj=System.currentTimeMillis();

            // application de la gravité
            if (deplacer(terrain,blocs,0,0,-1,1)) {
                if (z>0 && terrain[x][y][z-1]!=null)
                    terrain[x][y][z-1].impacter(terrain,blocs,x,y,z-1,this);
            }

            if (depX!=0 || depY!=0) {
                    // si le bloc est sur un bloc de glace, il glisse
                    boolean avancer=(z>0 && terrain[x][y][z-1]!=null && terrain[x][y][z-1].getBlocType(blocs).getMatiere()=='g');
                    if (avancer) {
                        avancer=deplacer(terrain, blocs,depX,depY,0,0);
                    }
                    if (!avancer) {
                        depX=0;
                        depY=0;
                    }
            }
        }
    }

    /**
     * Execute l'action du joueur.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     * @param action contient l'action à réaliser
     * @return boolean si l'action a bien été réalisée
     */
    public boolean actionJoueur (Bloc [][][] terrain, BlocType [] blocs, String action) {

        // si le joueur a les pieds au sol et qu'il ne glisse pas sur de la glace
        if (z>0 && terrain[x][y][z-1]!=null && ((depX==0 && depY==0) || terrain[x][y][z-1].getBlocType(blocs).getMatiere()!='g'))
            switch (action) {
                case "haut": return deplacer(terrain,blocs, 0, -1, 0,0);
                case "bas": return deplacer(terrain,blocs, 0, 1, 0,0);
                case "gauche": return deplacer(terrain,blocs, -1, 0, 0,0);
                case "droite": return deplacer(terrain,blocs, 1, 0, 0,0);
            }
        return false;
    }

    /**
     * Renvoie la texture actuelle du joueur.
     * @param terrain le terrain sur lequel se déplace le joueur
     * @param blocs la liste des types de blocs
     * @return String la texture actuelle du joueur
     */
    public String getTexture (Bloc [][][] terrain, BlocType [] blocs) {
        int num=getNumImage();
        return "player-"+num+".png";
    }

    /**
     * Renvoie et met à jour le numéro d'image.
     * @return int le numéro d'image du joueur
     */
    private int getNumImage () {
        int nbImages=4;

        // si il faut changer d'image
        if (System.currentTimeMillis()-tempsImage>=100) {
            tempsImage=System.currentTimeMillis();
            numImage=(numImage+1)%nbImages;
        }
        return numImage;
    }

}
