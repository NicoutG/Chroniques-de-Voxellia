
/**
 * Classe Bloc permet la gestion des blocs.
 */

public class Bloc {

    /**
     * idBlocType l'index du type de bloc correspondant dans le tableau.
     */
    private int idBlocType=1;

    /**
     * tempsImage contient l'instant du dernier changement de texture du bloc.
     */
    private long tempsImage=0;

    /**
     * numImage contient le numéro actuel de la texture du bloc.
     */
    private int numImage;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public Bloc (int id) {
        idBlocType=id;
    }

    /**
     * Charge les paramètres du bloc à partir d'une chaine de caractères.
     * @param params la chaine qui contient les informations sur les paramètres du bloc
     * @return boolean si le chargement des paramètres a bien été effectué
     */
    public boolean setParametres (String params) {
        String [] paramList=params.split("/");
        if (paramList.length!=1)
            return false;
        return true;
    }

    /**
     * accesseur de idBlocType
     * @return int l'index du type de bloc correspondant dans le tableau
     */
    public int getIdBlocType () {
        return idBlocType;
    }

    /**
     * Renvoie le type de bloc correspondant à ce bloc.
     * @param blocs la liste des types de blocs
     * @return BlocType le type de bloc correspondant à ce bloc
     */
    public BlocType getBlocType (BlocType [] blocs) {
        return blocs[idBlocType];
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
    public boolean deplacer (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, int depx, int depy, int depz, Joueur joueur, int num) {
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

    /**
     * Interragit avec un autre bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param blocInter le bloc avec lequel ce bloc interragit
     * @param joueur le joueur qui joue sur le niveau
     */
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

    /**
     * Met à jour le bloc en cas d'impact.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public void impacter (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        if (getBlocType(blocs).getDestructible()) {
            terrain[x][y][z]=null;
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
    public String getTexture (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int num=getNumImage(blocs);
        if (num==-1)
            return getBlocType(blocs).getTexture();
        String [] text=getBlocType(blocs).getTexture().split("\\.");
        return text[0]+"-"+num+"."+text[1];
    }

    /**
     * Renvoie et met à jour le numéro d'image.
     * @param blocs la liste des types de blocs
     * @return int le numéro d'image du bloc
     */
    private int getNumImage (BlocType [] blocs) {
        int nbImages=getBlocType(blocs).getNbImages();
        if (nbImages==1)
            return -1;
        
        // si il faut changer d'image
        if (System.currentTimeMillis()-tempsImage>=100) {
            tempsImage=System.currentTimeMillis();
            numImage=(numImage+1)%nbImages;
        }
        return numImage;
    }

    /**
     * Affiche les informations du bloc.
     * @param blocs la liste des types de blocs
     */
    public void afficher (BlocType [] blocs) {
        getBlocType(blocs).afficher();
    }

}
