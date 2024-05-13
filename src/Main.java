
public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/texte/blocs/blocs.txt");
        niveau.chargerTerrain("./data/texte/niveaux/terrainTest8.txt");
        MF mf=new MF(niveau);
        mf.setVisible(true);
        niveau.lancementNiveau();
        niveau.addObserver(mf);
    }

}