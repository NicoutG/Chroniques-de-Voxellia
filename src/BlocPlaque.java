/**
 * Classe BlocPlaque permet la gestion des blocs de plaque.
 */

public class BlocPlaque extends BlocActivation {

    /**
     * valNonActif contient la valeur de la plaque lorsqu'elle n'est pas préssée.
     */
    private boolean valNonActif;

    /**
     * idBloc contient les identifiants des blocs mouvants qui peuvent l'activer (-2 joueur et blocs, -1 blocs, autres blocs du même identifiant)
     */
    private int idBloc;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocPlaque (int id) {
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
        if (paramList.length!=3 && paramList.length!=4)
            return false;
        valNonActif=paramList[1].equals("t");
        setEtat(valNonActif);
        setIdGroupe(Integer.parseInt(paramList[2]));
        if (paramList.length==3)
            idBloc=-2;
        else
            idBloc=Integer.parseInt(paramList[3]);
        return true;
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
        if (z+1<terrain[x][y].length) {
            boolean actif=false;
            switch (idBloc) {
                case -2: actif=(joueur.getX()==x && joueur.getY()==y && joueur.getZ()==z+1) || (terrain[x][y][z+1]!=null && terrain[x][y][z+1] instanceof BlocMouvant);break;
                case -1: actif=terrain[x][y][z+1]!=null && terrain[x][y][z+1] instanceof BlocMouvant;break;
                default: actif=terrain[x][y][z+1] instanceof BlocMouvant && ((BlocMouvant)terrain[x][y][z+1]).getIdBloc()==idBloc;break;
            }
            
            // activation ou désactivation de la plaque
            if (getEtat()!=valNonActif) {
                if (!actif) {
                    setEtat(!getEtat());
                    if (valNonActif)
                        activer(terrain,blocs,joueur);
                    else
                        desactiver(terrain,blocs,joueur);
                }
            }
            else {
                if (actif) {
                    setEtat(!getEtat());
                    if (valNonActif)
                        desactiver(terrain,blocs,joueur);
                    else
                        activer(terrain,blocs,joueur);
                }
            }
        }
        super.miseAjour(terrain,blocs,x,y,z,joueur);
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
        if (getEtat()!=valNonActif) {
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
        System.out.println("Val non actif : "+valNonActif);
        System.out.println("Id bloc : "+idBloc);
    }

}
