
public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/texte/blocs.txt");
        niveau.afficherBlocs();
        if (niveau.chargerTerrain("./data/texte/terrainTest.txt"))
            niveau.afficherTerrainDetails();
        
        // test des deplacements du joueur
        niveau.afficherTerrain();
        for (int i=0;i<4;i++) { // On deplace 4 fois le joueur vers la droite
            niveau.deplacerJoueur(1, 0, 0);
            niveau.afficherTerrain();
        }
        niveau.deplacerJoueur(0, 1, 0); // On deplace le joueur vers le bas
        niveau.afficherTerrain();
    }

}