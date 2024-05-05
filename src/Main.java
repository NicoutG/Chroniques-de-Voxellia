
public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/texte/blocs/blocs.txt");
        niveau.lancementNiveau("./data/texte/niveaux/terrainTest10.txt");
        MF mf=new MF(niveau);
        niveau.addObserver(mf);
        mf.setVisible(true);
    }

}