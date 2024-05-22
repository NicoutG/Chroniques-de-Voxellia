import java.util.Observer;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;


/**
 * Classe MF permet la gestion de l'interface graphique
 */

public class MF extends JFrame implements Observer{
    
    /**
     * niveau contient le niveau actuel.
     */
    private Niveau niveau;

    /**
     * tab contient la matrice de JLabels.
     */
    private JLabel tab [][][] = null;

    /**
     * images contient la liste des images.
     * La clé est le nom de l'image, et la valeur l'objet ImageIcon
     */
    private HashMap<String, ImageIcon> images = null;

    /**
     * Constructeur de MF
     * @param niv le niveau actuel
     */
    public MF (Niveau niv) {
        niveau=niv;
        // Construit l'interface graphique
        build();
        // Ajoute les EventListeners
        addEC();
    }

    /**
     * Construit l'interface graphique
     */
    private void build () {

        // Création de la fenêtre
        setTitle("Sokoban"); 
        setLayout(new BorderLayout());

        int layoutSize = 1500;

        int taillex=niveau.getTaillex(); 
        int tailley=niveau.getTailley(); 
        int taillez=niveau.getTaillez();
       
        // Calcul de la taille des blocs (width, height)
        int blockHeight = layoutSize / tailley;
        // Limite la taille des blocs à 50px
        if (blockHeight > 50) blockHeight = 50;
        int blockWidth = blockHeight;

        // Initialisation de la matrice de JLabels
        tab = new JLabel[taillex][tailley][taillez];

        JPanel jp=new JPanel(); 
        JPanel jpC=new JPanel();
        JPanel jpB=new JPanel(); 
        jpC.setLayout(null); 
        jpC.setPreferredSize(new Dimension(layoutSize, layoutSize));
        
        // Création du bouton "Recommencer"
        JButton btnRecommencer = new JButton("Recommencer");
        btnRecommencer.addActionListener(e -> {
            niveau.recommencer();
            requestFocusInWindow();
        });

        // Création du bouton "Quitter"
        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(e -> {
            niveau.quitter();
            this.dispose();
        });

        // Ajout des boutons au JPanel 
        jpB.add(btnRecommencer);
        jpB.add(btnQuitter);

        jp.add(jpB);
        jp.add(jpC);
        add(jp);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        
        String folder="data/img/blocs/";
        // Récupère la liste des textures du niveau
        Vector <String> textures = niveau.getTextures(folder);
        images = new HashMap <String, ImageIcon>();
        // Charge les images et les stocke dans le HashMap
        for (int i=0;i<textures.size();i++) {
            images.put(textures.get(i),new ImageIcon(new ImageIcon( folder+textures.get(i)).getImage().getScaledInstance(blockWidth, blockHeight, java.awt.Image.SCALE_FAST))); // Load the images and store them in the HashMap
        }

        // Construction de la vue isométrique
        
        // Offset horizontal et vertical (par rapport au point (0,0) (en haut à gauche de la fenêtre))
        int offsetX = 0;
        int offsetY = 0;

        for (int k=0;k<taillez;k++) {
            for (int j=0;j<tailley;j++) {
                for (int i=0;i<taillex;i++) {
                    tab[i][j][k]=new JLabel();
                    tab[i][j][k].setPreferredSize(new Dimension(blockWidth, blockHeight));
                    tab[i][j][k].setMinimumSize(new Dimension(blockWidth, blockHeight));
                    tab[i][j][k].setMaximumSize(new Dimension(blockWidth, blockHeight));

                    // On calcule les offsets en fonction de la position du bloc
                    // Pour mieux comprendre les calculs, se référer à la documentation : doc/vue-isometrique.pdf
                    offsetX = (layoutSize + blockWidth * (i - j)) / 2 ;
                    offsetY = (taillez * blockHeight + ((blockHeight * (i + j)) / 2 - k * blockHeight)) / 2;

                    tab[i][j][k].setBounds(offsetX, offsetY, blockWidth, blockHeight);
                    jpC.add(tab[i][j][k]);

                    // On ajoute le Z-index pour que les blocs soient affichés dans le bon ordre
                    jpC.setComponentZOrder(tab[i][j][k],0); 
                }
            }
        }
    }

    /**
     * Ajoute les EventListeners
     */
    private void addEC () {
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: niveau.actionJoueur("haut");break;
                    case KeyEvent.VK_DOWN: niveau.actionJoueur("bas");break;
                    case KeyEvent.VK_LEFT: niveau.actionJoueur("gauche");break;
                    case KeyEvent.VK_RIGHT: niveau.actionJoueur("droite");break;
                }
            }
        });
        requestFocus();
    }

    /**
     * Met à jour l'interface graphique
     * @param o l'objet observable
     * @param arg l'objet passé en argument
     */
    @Override
    public void update(Observable o, Object arg) {
        // Tant que le niveau n'est pas terminé
        if (!niveau.getVictoire()) {
            for (int k=0;k<niveau.getTaillez();k++)
                for (int j=0;j<niveau.getTailley();j++)
                    for (int i=0;i<niveau.getTaillex();i++) {
                        tab[i][j][k].setIcon(images.get(niveau.getTextureBloc(i,j,k))); 
                    }
        }
        else {
            // On marque le niveau comme validé
            niveau.validerNiveau();
            this.dispose();
        }
    }
}
