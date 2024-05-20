
/**
 * Classe BlocPiston permet la gestion des blocs de piston.
 */

public class BlocPiston extends BlocActivable {

    /**
     * indiceBloc contient l'index du type de bloc correspondant à l'extension du piston de la bonne orientation dans le tableau.
     */
    private int indiceBloc=-1;

    /**
     * orientation contient l'orientation du piston (0 haut, 1 bas, 2 avant, 3 arrière, 4 gauche, 5 droite).
     */
    private int orientation=1;

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocPiston (int id) {
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
        orientation=Integer.parseInt(paramList[3]);
        if (orientation<0 || 5<orientation)
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
        recupIdBloc(blocs);
        int [][] val ={{x,y,z+1},{x,y,z-1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int [][] dep ={{0,0,1},{0,0,-1},{0,-1,0},{0,1,0},{-1,0,0},{1,0,0}};
        int xi=val[orientation][0];
        int yi=val[orientation][1];
        int zi=val[orientation][2];
        if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
            int depxi=dep[orientation][0];
            int depyi=dep[orientation][1];
            int depzi=dep[orientation][2];

            // déplacement du bloc
            if (terrain[xi][yi][zi]!=null)
                terrain[xi][yi][zi].deplacer(terrain,blocs,xi,yi,zi,depxi,depyi,depzi,joueur,2);
            else

                // déplacement du joueur
                if (xi==joueur.getX() && yi==joueur.getY() && zi==joueur.getZ())
                    joueur.deplacer(terrain,blocs,depxi,depyi,depzi,2);
            
            // placement du bloc de piston
            if (terrain[xi][yi][zi]==null)
                terrain[xi][yi][zi]=new Bloc (indiceBloc);
        }
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
        recupIdBloc(blocs);
        int [][] val ={{x,y,z+1},{x,y,z-1},{x,y-1,z},{x,y+1,z},{x-1,y,z},{x+1,y,z}};
        int xi=val[orientation][0];
        int yi=val[orientation][1];
        int zi=val[orientation][2];
        if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && 0<=zi && zi<terrain[xi][yi].length) {
            if (terrain[xi][yi][zi]!=null && terrain[xi][yi][zi].getIdBlocType()==indiceBloc)
                terrain[xi][yi][zi]=null;
        }
    }

    /**
     * Récupère l'index du type de bloc correspondant à l'extension du piston de la bonne orientation dans le tableau
     * @param params la chaine qui contient les informations sur les paramètres du bloc
     * @param blocs la liste des types de blocs
     */
    private void recupIdBloc (BlocType [] blocs) {
        if (indiceBloc<0) {
            String nomBloc="piston"+orientation;

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

    /**
     * Affiche les informations du bloc.
     * @param blocs la liste des types de blocs
     */
    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Orientation : "+orientation);
    }

}
