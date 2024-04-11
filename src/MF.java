import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

public class MF extends JFrame implements Observer{
    Niveau niveau;
    JPanel tab [][]=null;

    public MF () {
        build();
        addEC();
    }

    private void build () {
        niveau.chargerBlocs("./data/texte/blocs.txt");
        niveau.chargerTerrain("./data/texte/terrainTest.txt");
        int taillex=niveau.getTaillex();
        int tailley=niveau.getTailley();
        tab=new JPanel[tailley][taillex];
        JPanel jp=new JPanel(new BorderLayout());
        JPanel jpC=new JPanel(new GridLayout(taillex,tailley));
        jp.add(jpC, BorderLayout.CENTER);
        add(jp);
        for (int j=0;j<tailley;j++)
            for (int i=0;i<taillex;i++) {
                tab[j][i]=new JPanel();
                jpC.add(tab[j][i]);
            } 
    }

    private void addEC () {
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: niveau.deplacerJoueur(0,-1,0);
                    case KeyEvent.VK_DOWN: niveau.deplacerJoueur(0,1,0);
                    case KeyEvent.VK_LEFT: niveau.deplacerJoueur(-1,0,0);
                    case KeyEvent.VK_RIGHT: niveau.deplacerJoueur(1,0,0);
                }
            }
        });
        requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO: Implement the update method
    }
}
