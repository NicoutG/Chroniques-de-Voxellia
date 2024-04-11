
public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/texte/blocs.txt");
        niveau.chargerTerrain("./data/texte/terrainTest.txt");
        MF mf=new MF(niveau);
        niveau.addObserver(mf);
        mf.setVisible(true);
    }

}