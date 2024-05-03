public class BlocActivation extends Bloc {
    private boolean etat=false;
    private int idGroupe=0; /*
                                -1 = Victoire
                                Autres = activations des blocs du même idGroupe
                             */

    public BlocActivation (int id) {
        super(id);
    }

    public void setEtat (boolean e) {
        etat=e;
    }

    public boolean getEtat () {
        return etat;
    }

    public void setIdGroupe (int id) {
        idGroupe=id;
    }

    public int getIdGroupe () {
        return idGroupe;
    }

    protected void activer (Bloc [][][] terrain, BlocType [] blocs, Joueur joueur) {

        // vérification que tout est allumé pour activer
        boolean activation=true;
        for (int x=0;x<terrain.length;x++)
                for (int y=0;y<terrain[x].length;y++)
                    for (int z=0;z<terrain[x][y].length;z++)
                        if (terrain[x][y][z] instanceof BlocActivation && idGroupe==((BlocActivation)terrain[x][y][z]).getIdGroupe() && !((BlocActivation)terrain[x][y][z]).getEtat())
                            activation=false;

        // si tout est allumé
        if (activation) {
            if (idGroupe==-1)
                joueur.setVictoire(true);
            else
                for (int x=0;x<terrain.length;x++)
                    for (int y=0;y<terrain[x].length;y++)
                        for (int z=0;z<terrain[x][y].length;z++)
                            if (terrain[x][y][z] instanceof BlocActivable)
                                if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                                    ((BlocActivable)terrain[x][y][z]).activation(terrain,blocs,x,y,z,joueur);
        }
    }

    protected void desactiver (Bloc [][][] terrain, BlocType [] blocs, Joueur joueur) {
        // désactivation des blocs du groupe
        for (int x=0;x<terrain.length;x++)
            for (int y=0;y<terrain[x].length;y++)
                for (int z=0;z<terrain[x][y].length;z++)
                    if (terrain[x][y][z] instanceof BlocActivable)
                        if (((BlocActivable)terrain[x][y][z]).getIdGroupe()==idGroupe)
                            ((BlocActivable)terrain[x][y][z]).desactivation(terrain,blocs,x,y,z,joueur);
    }

    @Override
    public void afficher (BlocType [] blocs) {
        super.afficher(blocs);
        System.out.println("Etat : "+etat);
        System.out.println("IdGroupe : "+idGroupe);
    }

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

}
