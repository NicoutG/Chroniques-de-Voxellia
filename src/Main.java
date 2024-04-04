

public class Main {
    public static void main(String[] args) {
        Niveau niveau=new Niveau();
        niveau.chargerBlocs("./data/blocs.txt");
        niveau.afficherBlocs();
    }

}