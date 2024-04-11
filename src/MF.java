import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

public class MF extends JFrame implements Observer{
    Niveau niveau;
    JPanel tab [][]=null;

    public MF (Niveau niv) {
        niveau=niv;
        build();
        addEC();
    }

    private void build () {
        int taillex=niveau.getTaillex();
        int tailley=niveau.getTailley();
        tab=new JPanel[tailley][taillex];
        JPanel jp=new JPanel(new BorderLayout());
        JPanel jpC=new JPanel(new GridLayout(taillex,tailley,1,1));
        jp.add(jpC, BorderLayout.CENTER);
        add(jp);
        for (int j=0;j<tailley;j++)
            for (int i=0;i<taillex;i++) {
                tab[i][j]=new JPanel();
                jpC.add(tab[i][j]);
            }
        update(niveau,null);
    }

    private void addEC () {
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: niveau.deplacerJoueur(0,-1,0);break;
                    case KeyEvent.VK_DOWN: niveau.deplacerJoueur(0,1,0);break;
                    case KeyEvent.VK_LEFT: niveau.deplacerJoueur(-1,0,0);break;
                    case KeyEvent.VK_RIGHT: niveau.deplacerJoueur(1,0,0);break;
                }
            }
        });
        requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        for (int j=0;j<niveau.getTailley();j++)
            for (int i=0;i<niveau.getTaillex();i++) {
                Bloc bloc=niveau.getBloc(i,j,0);
                if (bloc==null) {
                    if (niveau.getJoueur().getX()==i && niveau.getJoueur().getY()==j)
                        tab[i][j].setBackground(Color.RED);
                    else
                        tab[i][j].setBackground(Color.GREEN);
                }
                else
                    switch (niveau.getBloc(i,j,0).getTypeBloc().getId()) {
                        case 3: tab[i][j].setBackground(Color.BLUE);break;
                        default: tab[i][j].setBackground(Color.BLACK);
                    }
            }
    }
}
