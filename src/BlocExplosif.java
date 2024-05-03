public class BlocExplosif extends BlocActivable {
    private int rayon=0;
    private int delai=0;
    private long tempsExplosion=System.currentTimeMillis();

    public BlocExplosif (int id) {
        super(id);
    }

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

    public void activation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(true);
        tempsExplosion=System.currentTimeMillis();
    }

    public void desactivation (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        setEtat(false);
    }

    @Override
    public void miseAjour (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        super.miseAjour(terrain,blocs,x,y,z,joueur);
        if (getEtat()) {
            if (System.currentTimeMillis()-tempsExplosion>=delai) {
                explosion(terrain, blocs, x, y, z, joueur);
            }
        }
    }

    public void explosion (Bloc [][][] terrain, BlocType [] blocs, int x, int y, int z, Joueur joueur) {
        terrain[x][y][z]=null;
        for (int i=Math.max(0,x-rayon);i<Math.min(x+rayon+1,terrain.length);i++)
            for (int j=Math.max(0,y-rayon);j<Math.min(y+rayon+1,terrain[x].length);j++)
                for (int k=Math.max(0,z-rayon);k<Math.min(z+rayon+1,terrain[x][y].length);k++)
                    if (terrain[i][j][k]!=null && terrain[i][j][k].getBlocType(blocs).getDestructible())
                        terrain[i][j][k]=null;            
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
