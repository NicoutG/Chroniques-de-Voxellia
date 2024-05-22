import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Image;

/*
 * Classe Game permet la gestion du jeu
 */
public class Game {
    /*
     * niveaux contient la liste des niveaux.
     */
    private String[][] niveaux = null;

    /*
     * niveau contient le niveau actuel.
     */
    private Niveau niveau = new Niveau();

    /**
     * Démarre le jeu
     */
    public void start() {
        chargerNiveaux("./data/texte/niveaux.txt");

        // Crée une fenêtre pour la sélection niveaux
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Game Levels");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 5));

            for (int i = 0; i < niveaux.length; i++) {
                final int levelIndex = i;

                // Créer un panel pour chaque niveau
                JPanel levelPanel = new JPanel();
                levelPanel.setLayout(null);

                // Créer un bouton pour chaque niveau
                JButton button = new JButton("Level " + (i + 1) + " - " + niveaux[i][0]);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    playNiveau(levelIndex);
                    }
                });

                button.setBounds(10, 10, 200, 30);
                levelPanel.add(button);

                // Ajoute un icon validé si le niveau est terminé
                if (niveaux[i][3].equals("1")) {
                    ImageIcon checkIcon = new ImageIcon("./data/img/check-icon.png");
                    checkIcon.setImage(checkIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                    JLabel checkLabel = new JLabel(checkIcon);
                    checkLabel.setBounds(10, 50, 50, 50); // Set position and size of the image
                    checkLabel.setPreferredSize(new Dimension(50, 50)); // Set the preferred size of the image label
                    levelPanel.add(checkLabel);
                }

                // Ajoute une image pour chaque niveau
                ImageIcon imageIcon = new ImageIcon("./data/img/niveaux/" + niveaux[i][2]);
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setBounds(10, 50, 200, 200); 
                imageLabel.setPreferredSize(new Dimension(200, 200));
                levelPanel.add(imageLabel);

                levelPanel.setPreferredSize(new Dimension(220, 250));
                panel.add(levelPanel);
            }

            frame.add(panel);
            
            frame.pack();
            frame.setVisible(true);
        });
    }

    /**
     * Joue un niveau
     * @param id l'identifiant du niveau
     */
    public void playNiveau(int id) {
        // Vérifie si le niveau est valide
        if (niveaux == null || id < 0 || id >= niveaux.length) {
            System.out.println("Niveau invalide");
            return;
        }
        
        niveau = new Niveau();
        niveau.chargerBlocs("./data/texte/blocs/blocs.txt");
        niveau.chargerTerrain("./data/texte/niveaux/" + niveaux[id][1]);
        MF mf=new MF(niveau);
        mf.setVisible(true);
        niveau.lancementNiveau();
        niveau.addObserver(mf);
    }

    /**
     * Charge les niveaux
     * @param fichier le fichier contenant les niveaux
     * @return true si les niveaux ont été chargés, false sinon
     */
    private boolean chargerNiveaux(String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=exp.split("\r\n");
            if (exps.length<=1)
                return false;

            // suppression des espaces en trop
            for (int i=0;i<exps.length;i++)
                while (exps[i].contains("  "))
                    exps[i]=exps[i].replace("  "," ");
            
            niveaux = new String[exps.length][4];

            // chargement des niveaux
            for (int i=0;i<exps.length ;i++) {
                String [] ligne=exps[i].split(";");
                // Contient le nom du niveau
                niveaux[i][0] = ligne[0];
                // Contient le nom du fichier du niveau
                niveaux[i][1] = ligne[1];
                // Contient le nom de l'image du niveau
                niveaux[i][2] = ligne[2];
                // Contient si le niveau est terminé ou pas
                niveaux[i][3] = ligne[3];
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}
