import java.util.Observer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;
import java.awt.Insets;

public class MF extends JFrame implements Observer{
    
    Niveau niveau;
    JLabel tab [][][]=null;
    HashMap<String, ImageIcon> images = null;

    public MF (Niveau niv) {
        niveau=niv;
        build();
        addEC();
    }

    private void build () {

        setTitle("Sokoban");
        setLayout(new BorderLayout());

        int layoutSize = 1500;

        int taillex=niveau.getTaillex();
        int tailley=niveau.getTailley();
        int taillez=niveau.getTaillez();
       
        int blockHeight = layoutSize / tailley;
        if (blockHeight > 50) blockHeight = 50;
        int blockWidth = blockHeight;

        tab = new JLabel[taillex][tailley][taillez];

        JPanel jp=new JPanel();
        JPanel jpC=new JPanel();
        jpC.setLayout(null);
        jpC.setPreferredSize(new Dimension(layoutSize, layoutSize));
        jp.add(jpC);
        add(jp);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        String folder="data/img/";
        Vector <String> textures = niveau.getTextures(folder);
        images = new HashMap <String, ImageIcon>();
        for (int i=0;i<textures.size();i++) {
            images.put(textures.get(i),new ImageIcon(new ImageIcon( folder+textures.get(i)).getImage().getScaledInstance(blockWidth, blockHeight, java.awt.Image.SCALE_FAST)));
        }
        
        int offsetX = 0;
        int offsetY = 0;

        for (int k=0;k<taillez;k++) {
            for (int j=0;j<tailley;j++)
                for (int i=0;i<taillex;i++) {
                    tab[i][j][k]=new JLabel();
                    // tab[i][j].setOpaque(true);
                    tab[i][j][k].setPreferredSize(new Dimension(blockWidth, blockHeight));
                    tab[i][j][k].setMinimumSize(new Dimension(blockWidth, blockHeight));
                    tab[i][j][k].setMaximumSize(new Dimension(blockWidth, blockHeight));

                    offsetX = (layoutSize + blockWidth * (i - j)) / 2 ;
                    offsetY = (taillez * blockHeight + ((blockHeight * (i + j)) / 2 - k * blockHeight)) / 2;

                    tab[i][j][k].setBounds(offsetX, offsetY, blockWidth, blockHeight);
                    jpC.add(tab[i][j][k]);


                    jpC.setComponentZOrder(tab[i][j][k],0);

                }
            }
        update(niveau,null);
    }

    private void addEC () {
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: niveau.actionJoueur("haut");break;
                    case KeyEvent.VK_DOWN: niveau.actionJoueur("bas");break;
                    case KeyEvent.VK_LEFT: niveau.actionJoueur("gauche");break;
                    case KeyEvent.VK_RIGHT: niveau.actionJoueur("droite");break;
                }
                niveau.miseAjourTerrain();
            }
        });
        requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!niveau.getVictoire()) {
            for (int k=0;k<niveau.getTaillez();k++)
                for (int j=0;j<niveau.getTailley();j++)
                    for (int i=0;i<niveau.getTaillex();i++) {
                        if (niveau.getJoueur().getX()==i && niveau.getJoueur().getY()==j && niveau.getJoueur().getZ()==k)
                            tab[i][j][k].setIcon(images.get(niveau.getTextureJoueur()));
                        else
                            tab[i][j][k].setIcon(images.get(niveau.getTextureBloc(i,j,k)));
                    }

            
        }
    }
}
