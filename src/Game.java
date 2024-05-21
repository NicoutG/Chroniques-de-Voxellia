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


public class Game {
    String[][] niveaux = null;
    Niveau niveau = new Niveau();

    public void start() {
        chargerNiveaux("./data/texte/niveaux.txt");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Levels");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 5)); // Set layout to grid with 5 columns

            for (int i = 0; i < niveaux.length; i++) {
            final int levelIndex = i; // Create a final copy of i

            // Create a panel for each level
            JPanel levelPanel = new JPanel();
            levelPanel.setLayout(null); // Set layout to null for absolute positioning

            // Create button for the level
            JButton button = new JButton("Level " + (i + 1) + " - " + niveaux[i][0]);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                playNiveau(levelIndex); // Use the final copy of i
                }
            });
            button.setBounds(10, 10, 200, 30); // Set position and size of the button
            levelPanel.add(button);

            // Add image to the level panel
            if (niveaux[i][3].equals("1")) {
                ImageIcon checkIcon = new ImageIcon("./data/img/check-icon.png");
                checkIcon.setImage(checkIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                JLabel checkLabel = new JLabel(checkIcon);
                checkLabel.setBounds(10, 50, 50, 50); // Set position and size of the image
                checkLabel.setPreferredSize(new Dimension(50, 50)); // Set the preferred size of the image label
                levelPanel.add(checkLabel);
            }

            ImageIcon imageIcon = new ImageIcon("./data/img/niveaux/" + niveaux[i][2]);
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setBounds(10, 50, 200, 200); // Set position and size of the image
            imageLabel.setPreferredSize(new Dimension(200, 200)); // Set the preferred size of the image label
            levelPanel.add(imageLabel);

            levelPanel.setPreferredSize(new Dimension(220, 250)); // Set preferred size of the level panel
            panel.add(levelPanel);
            }

            frame.add(panel);
            
            frame.pack();
            frame.setVisible(true);
        });
    }
    
    public void playNiveau(int id) {
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
                niveaux[i][0] = ligne[0];
                niveaux[i][1] = ligne[1];
                niveaux[i][2] = ligne[2];
                niveaux[i][3] = ligne[3];
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}
