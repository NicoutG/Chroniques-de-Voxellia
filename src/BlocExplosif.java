
/**
 * Classe BlocExplosif permet la gestion des blocs explosifs.
 */

public class BlocExplosif extends BlocActivable {

    /**
     * rayon contient le rayon d'explosion (explosion cubique).
     */
    private int rayon=0;

    /**
     * delai contient le délai entre l'activation du bloc et l'explosion en ms.
     */
    private int delai=0;

    /**
     * tempsExplosion contient l'instant d'activation du bloc.
     */
    private long tempsExplosion=System.currentTimeMillis();

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocExplosif (int id) {
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
        if (paramList.length<4 || 5<paramList.length)
            return false;
        setEtat(paramList[1].equals("t"));
        setIdGroupe(Integer.parseInt(paramList[2]));
        rayon=Integer.parseInt(paramList[3]);
        if (paramList.length==5)
            delai=Integer.parseInt(paramList[4]);
        else
            delai=0;
        if (delai<0)
            return false;
        return true;
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
        tempsExplosion=System.currentTimeMillis();
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
        if (getEtat()) {
            if (System.currentTimeMillis()-tempsExplosion>=delai) {
                explosion(terrain, blocs, x, y, z, joueur);
            }
        }
    }

    /**
     * Détruit les blocs destructibles dans le rayon d'explosion du bloc.
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    public void explosion (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        terrain[x][y][z]=null;
        for (int i=Math.max(0,x-rayon);i<Math.min(x+rayon+1,terrain.length);i++)
            for (int j=Math.max(0,y-rayon);j<Math.min(y+rayon+1,terrain[x].length);j++)
                for (int k=Math.max(0,z-rayon);k<Math.min(z+rayon+1,terrain[x][y].length);k++)
                    if (terrain[i][j][k]!=null && terrain[i][j][k].getBlocType(blocs).getDestructible())
                        terrain[i][j][k]=null;            
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
        System.out.println("Rayon : "+rayon);
        System.out.println("Delai : "+delai+" ms");
    }

}
