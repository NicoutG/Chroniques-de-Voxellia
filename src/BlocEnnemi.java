import java.util.Random;
import java.util.Vector;

/**
 * Classe BlocEnnemi permet la gestion des ennemis.
 */

public class BlocEnnemi extends BlocMouvant {

    /**
     * tempsDep contient l'instant du dernier déplacement de l'ennemi.
     */
    protected long tempsDep=System.currentTimeMillis();

    /**
     * Constructeur de la classe
     * @param id l'index du type de bloc correspondant dans le tableau
     */
    public BlocEnnemi (int id) {
        super(id);
    }

    /**
     * Renvoi le tableau qui contient les distances les plus courtes de chaque case par rapport au joueur
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     * @return int [][] le tableau qui contient les distances les plus courtes de chaque case par rapport au joueur
     */
    private int [][] createChemin (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int [][] chemin=new int [terrain.length][terrain[0].length];
        for (int i=0;i<chemin.length;i++)
            for (int j=0;j<chemin[i].length;j++)
                chemin[i][j]=-1;
        Vector <Integer> fileX=new Vector <Integer> ();
        Vector <Integer> fileY=new Vector <Integer> ();
        Vector <Integer> fileVal=new Vector <Integer> ();
        int xj=joueur.getX();
        int yj=joueur.getY();
        fileX.add(xj);
        fileY.add(yj);
        fileVal.add(0);
        int xf,yf,val;

        // recherche du chemin le plus court entre le joueur et l'ennemi
        while (!fileX.isEmpty() && chemin[x][y]==-1) {
            xf=fileX.get(0);
            fileX.remove(0);
            yf=fileY.get(0);
            fileY.remove(0);
            val=fileVal.get(0);
            fileVal.remove(0);
            int [][] pos={{xf-1,yf},{xf+1,yf},{xf,yf-1},{xf,yf+1}};
            if (chemin[xf][yf]==-1 || chemin[xf][yf]>val) {
                chemin[xf][yf]=val;
                val++;
                int xi,yi;
                for (int i=0;i<4;i++) {
                    xi=pos[i][0];
                    yi=pos[i][1];
                    if (0<=xi && xi<chemin.length && 0<=yi && yi<chemin[xi].length && ((xi==x && yi==y) || (terrain[xi][yi][z]==null && terrain[xi][yi][z-1]!=null && !(terrain[xi][yi][z-1] instanceof BlocMortel)))) {
                        fileX.add(xi);
                        fileY.add(yi);
                        fileVal.add(val);
                    }
                }
            }
        }
        return chemin;
    }

    /**
     * Renvoi le numéro du prochain déplacement de l'ennemi pour se rapprocher du joueur
     * @param terrain le terrain dans lequel se trouve le bloc
     * @param blocs la liste des types de blocs
     * @param x la position x du bloc dans le terrain
     * @param y la position y du bloc dans le terrain
     * @param z la position z du bloc dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     * @return int le numéro du prochain déplacement de l'ennemi pour se rapprocher du joueur
     */
    private int depSuivant (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        int [][] chemin=createChemin(terrain,blocs,x,y,z,joueur);
        int [][] pos={{x-1,y},{x+1,y},{x,y-1},{x,y+1}};
        Vector <Integer> dep=new Vector <Integer> ();
        int xi,yi;
        int valMin=-1;

        // récupération des déplacements possibles
        for (int i=0;i<4;i++) {
            xi=pos[i][0];
            yi=pos[i][1];
            if (0<=xi && xi<chemin.length && 0<=yi && yi<chemin[xi].length && chemin[xi][yi]!=-1) {
                if (valMin==-1 || chemin[xi][yi]<valMin) {
                    valMin=chemin[xi][yi];
                    dep.clear();
                }
                if (chemin[xi][yi]==valMin)
                    dep.add(i);
            }
        }
        if (dep.isEmpty())
            return -1;
        
        // choix aléatoire d'un déplacement
        Random rand=new Random();
        return dep.get(rand.nextInt(dep.size()));
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
        if (num==0)
            return false;  
        return super.deplacer(terrain, blocs, x, y, z, depx, depy, depz, joueur, num);    
    }

    /**
     * Met à jour l'ennemi.
     * @param terrain le terrain dans lequel se trouve l'ennemi
     * @param blocs la liste des types de blocs
     * @param x la position x de l'ennemi dans le terrain
     * @param y la position y de l'ennemi dans le terrain
     * @param z la position z de l'ennemi dans le terrain
     * @param joueur le joueur qui joue sur le niveau
     */
    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {

        // l'ennemi meurt lorsqu'il chute ou qu'il a un bloc mouvant au dessus de lui
        if (z==0 || (z+1<terrain[x][y].length && terrain[x][y][z+1] instanceof BlocMouvant)) {
            terrain[x][y][z]=null;
            return ;
        }

        // maj de la position toute les 500 ms
        if (System.currentTimeMillis()-tempsDep>=300) {
            tempsDep=System.currentTimeMillis();
            int [][] dep={{-1,0},{1,0},{0,-1},{0,1}};
            boolean mouvementPos=false;

            // déplacement vers le joueur
            if (z==joueur.getZ()) {
                int depPos=depSuivant(terrain,blocs,x,y,z,joueur);
                mouvementPos=-1!=depPos;
                if (mouvementPos) {
                    int depx=dep[depPos][0];
                    int depy=dep[depPos][1];
                    if (deplacer(terrain, blocs, x, y, z, depx, depy, 0, joueur,1)) {
                        x+=depx;
                        y+=depy;
                    }
                    joueur.miseAjour(terrain, blocs);
                }
            }

            // déplacement aléatoire
            if (!mouvementPos) {
                int [][] pos={{x-1,y},{x+1,y},{x,y-1},{x,y+1}};
                Vector <Integer> depPos=new Vector <Integer> ();
                int xi,yi;

                // récupération des déplacements possibles
                for (int i=0;i<4;i++) {
                    xi=pos[i][0];
                    yi=pos[i][1];
                    if (0<=xi && xi<terrain.length && 0<=yi && yi<terrain[xi].length && terrain[xi][yi][z]==null && terrain[xi][yi][z-1]!=null && !(terrain[xi][yi][z-1] instanceof BlocMortel)) {
                        depPos.add(i);
                    }
                }

                // choix aléatoire d'un déplacement
                if (!depPos.isEmpty()) {
                    Random rand=new Random ();
                    int valDep=depPos.get(rand.nextInt(depPos.size()));
                    int depx=dep[valDep][0];
                    int depy=dep[valDep][1];
                    if (deplacer(terrain, blocs, x, y, z, depx, depy, 0, joueur,1)) {
                        x+=depx;
                        y+=depy;
                    }
                }
            }
        }
        super.miseAjour(terrain, blocs, x, y, z, joueur);
    }

}
