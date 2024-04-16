import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;

public class MF extends JFrame implements Observer{
    
    Niveau niveau;
    JLabel tab [][]=null;
    ImageIcon images[] = null;

    public MF (Niveau niv) {
        niveau=niv;
        build();
        addEC();
    }

    private void build () {
        int taillex=niveau.getTaillex();
        int tailley=niveau.getTailley();
       
        int height = 780 / tailley;
        int width = height;

        tab = new JLabel[tailley][taillex];

        JPanel jp=new JPanel(new FlowLayout());
        JPanel jpC=new JPanel(new GridLayout(taillex,tailley,0,0));
        jp.add(jpC, BorderLayout.CENTER);
        add(jp);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        String[] textures = niveau.getTextures();
        images = new ImageIcon[textures.length + 2];
        for (int i=0;i<textures.length;i++) {
            images[i] = new ImageIcon(new ImageIcon( "data/img/"+textures[i]).getImage().getScaledInstance(width, height, java.awt.Image.SCALE_FAST));
        }
        images[textures.length] = new ImageIcon(new ImageIcon( "data/img/grass.jpg").getImage().getScaledInstance(width, height, java.awt.Image.SCALE_FAST));
        images[textures.length + 1] = new ImageIcon(new ImageIcon( "data/img/player.png").getImage().getScaledInstance(width, height, java.awt.Image.SCALE_FAST));
        
        for (int j=0;j<tailley;j++)
            for (int i=0;i<taillex;i++) {
                tab[i][j]=new JLabel();
                // tab[i][j].setOpaque(true);
                tab[i][j].setPreferredSize(new Dimension(width, height));
                tab[i][j].setMinimumSize(new Dimension(width, height));
                tab[i][j].setMaximumSize(new Dimension(width, height));
                jpC.add(tab[i][j]);
            }
        update(niveau,null);
    }

    private void addEC () {
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: niveau.deplacerJoueur("haut");break;
                    case KeyEvent.VK_DOWN: niveau.deplacerJoueur("bas");break;
                    case KeyEvent.VK_LEFT: niveau.deplacerJoueur("gauche");break;
                    case KeyEvent.VK_RIGHT: niveau.deplacerJoueur("droite");break;
                }
                niveau.miseAjourTerrain();
            }
        });

        /*Thread terrainUpdateThread = new Thread(() -> {
            while (true) {
                
                // Mettre à jour le terrain à intervalles réguliers (par exemple, toutes les 500 millisecondes)
                niveau.miseAjourTerrain();
                try {
                    Thread.sleep(50); // Attendre 50 millisecondes avant la prochaine mise à jour
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        terrainUpdateThread.start();*/

        requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!niveau.getVictoire()) {
            for (int j=0;j<niveau.getTailley();j++)
                for (int i=0;i<niveau.getTaillex();i++) {
                    Bloc bloc=niveau.getBloc(i,j,1);
                    if (bloc==null) {
                        if (niveau.getJoueur().getX()==i && niveau.getJoueur().getY()==j)
                            tab[i][j].setIcon(images[images.length - 1]);
                        else
                            tab[i][j].setIcon(images[images.length - 2]);

                    }
                    else
                        tab[i][j].setIcon(images[bloc.getIdBlocType()]);
                }
        }
    }
}
