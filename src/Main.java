

public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/texte/blocs.txt");
        niveau.afficherBlocs();
        if (niveau.chargerTerrain("./data/texte/terrainTest.txt"))
            niveau.afficherTerrain();

    }

}