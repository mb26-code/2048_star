package vue_controleur;

import modele.Case;
import modele.Jeu;
import modele.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    public static final int PIXELS_PAR_CASE = 160;
    private static final String NOM_APPLI = "2048*";
    
    private static final Color CYAN = new Color(153,217,234);
    private static final Dimension FORMAT_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();

    private CaseLabel[][] tabCasesLabel;
    private Jeu jeu;
    private JFrame menu;


    public Swing2048(Jeu _jeu, JFrame _menu) {
    	
        this.jeu = _jeu;
        this.menu = _menu;
        
        setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI + " - Partie en cours");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getC() * PIXELS_PAR_CASE, jeu.getL() * PIXELS_PAR_CASE);
        setLocation((int)( FORMAT_ECRAN.getWidth()/2 - jeu.getC() * PIXELS_PAR_CASE / 2),(int)( FORMAT_ECRAN.getHeight()/2 - jeu.getL() * PIXELS_PAR_CASE /2));

        tabCasesLabel = new CaseLabel[jeu.getL()][jeu.getC()];

        JPanel contentPane = new JPanel(new GridLayout(jeu.getL(), jeu.getC()));

        for (int i = 0; i < jeu.getL(); i++) {
            for (int j = 0; j < jeu.getC(); j++) {
                tabCasesLabel[i][j] = new CaseLabel(jeu.getCase(i, j));
                contentPane.add(tabCasesLabel[i][j]);
            }
        }
        
        setContentPane(contentPane);
        
        ajouterEcouteurClavier();
        rafraichir();
    }


    private void rafraichir()  {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < jeu.getL() ; i++) {
                    for (int j = 0 ; j < jeu.getC() ; j++) {
                        Case c = jeu.getCase(i, j);
                        tabCasesLabel[i][j].nettoie();
                        
                        tabCasesLabel[i][j].modifCase(jeu.getCase(i,j));
                        
                        tabCasesLabel[i][j].majCouleur(jeu.getBase());

                        if (c == null) { tabCasesLabel[i][j].setText(""); } 
                        else{ tabCasesLabel[i][j].setText(c.getValeur() + ""); };
                    }
                }
            }
        });
    }

    
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            		switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT :
                        if (jeu.getmaxVal() == 0) { jeu.ReinitFusionsPuisRemplir(); break; }
                        else { jeu.ActionSiPartieNonFinie(Direction.gauche); break; }
                    case KeyEvent.VK_RIGHT :
                        if (jeu.getmaxVal() == 0) { jeu.ReinitFusionsPuisRemplir(); break; }
                        else { jeu.ActionSiPartieNonFinie(Direction.droite); break; }
                    case KeyEvent.VK_DOWN :
                        if (jeu.getmaxVal() == 0) { jeu.ReinitFusionsPuisRemplir(); break; }
                        else { jeu.ActionSiPartieNonFinie(Direction.bas); break; }
                    case KeyEvent.VK_UP :
                        if (jeu.getmaxVal() == 0) { jeu.ReinitFusionsPuisRemplir(); break; }
                        else  {jeu.ActionSiPartieNonFinie(Direction.haut); break; }
                    }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
        if (((Jeu) o).estGagne()) {
            //////Création d'une fenêtre annonçant la victoire avec quelques statistiques
            JFrame vueVictoire = new JFrame();
            vueVictoire.setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI + " - Victoire");
            vueVictoire.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            vueVictoire.setSize(500, 400);
            vueVictoire.setLocation((int)FORMAT_ECRAN.getWidth()/2 - 200,(int)FORMAT_ECRAN.getHeight()/2 - 200);
            JPanel contentPaneVic = new JPanel(new BorderLayout(0,20));
            JLabel messageVic = new JLabel("Vous avez gagné.",SwingConstants.CENTER);
            contentPaneVic.add(messageVic,BorderLayout.NORTH);
            
            messageVic.setPreferredSize(new Dimension(400,100));
            
            JPanel statsVic = new JPanel();
            
            JPanel grosstatsVic = new JPanel();
            contentPaneVic.add(grosstatsVic,BorderLayout.CENTER);
            grosstatsVic.setLayout(new GridBagLayout());
            grosstatsVic.add(statsVic,new GridBagConstraints());
            
            statsVic.setLayout(new BorderLayout(0,10));
            statsVic.add(new JLabel("Tours: "+jeu.getTour()),BorderLayout.NORTH);
            statsVic.add(new JLabel("Score: "+jeu.getmaxVal()),BorderLayout.CENTER);
            statsVic.add(new JLabel("Fusions effectuées: "+jeu.getFusionsEffectuees()),BorderLayout.SOUTH);
            
            ///Création et ajout du bouton de retour au menu
            JButton boutonMenuVictoire = new JButton("Retourner au menu");
			contentPaneVic.add(boutonMenuVictoire, BorderLayout.SOUTH);
			boutonMenuVictoire.setPreferredSize(new Dimension(100,30));
			boutonMenuVictoire.setBackground(CYAN);
			boutonMenuVictoire.setOpaque(true);
			///
			
			///Ajout de la logique de fenêtre au bouton de retour au menu
			boutonMenuVictoire.addActionListener(new ActionListener(){  
				public void actionPerformed(ActionEvent ae){ 
					menu.setVisible(true);
					vueVictoire.dispose();
					dispose();
				}
			});
			
            vueVictoire.setContentPane(contentPaneVic);
            setVisible(false);
            vueVictoire.setVisible(true);

        } else {
            if (((Jeu) o).estPerdu()) {
                //////Création d'une fenêtre annonçant la défaite avec quelques statistiques
                JFrame vueDefaite = new JFrame();
                vueDefaite.setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI + " - Défaite");
                vueDefaite.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                vueDefaite.setSize(500, 400);
                vueDefaite.setLocation((int)FORMAT_ECRAN.getWidth()/2 - 200,(int)FORMAT_ECRAN.getHeight()/2 - 200);
                JPanel contentPaneDef = new JPanel(new BorderLayout(0,20));
                JLabel messageDef = new JLabel("Vous avez perdu.",SwingConstants.CENTER);
                messageDef.setText("Vous avez perdu.");
                contentPaneDef.add(messageDef,BorderLayout.NORTH);
                messageDef.setPreferredSize(new Dimension(400,100));
                
                JPanel statsDef = new JPanel();
                
                JPanel grosstatsDef = new JPanel();
                contentPaneDef.add(grosstatsDef,BorderLayout.CENTER);
                grosstatsDef.setLayout(new GridBagLayout());
                grosstatsDef.add(statsDef,new GridBagConstraints());
                
                statsDef.setLayout(new BorderLayout(0,10));
                statsDef.add(new JLabel("Tours: "+jeu.getTour()),BorderLayout.NORTH);
                statsDef.add(new JLabel("Score: "+jeu.getmaxVal()),BorderLayout.CENTER);
                statsDef.add(new JLabel("Fusions effectuées: "+jeu.getFusionsEffectuees()),BorderLayout.SOUTH);
                
                ///Création et ajout du bouton de retour au menu
				JButton boutonMenuDefaite = new JButton("Retourner au menu");
				contentPaneDef.add(boutonMenuDefaite, BorderLayout.SOUTH);
				boutonMenuDefaite.setPreferredSize(new Dimension(100,30));
				boutonMenuDefaite.setBackground(CYAN);
				boutonMenuDefaite.setOpaque(true);
				///
				
				///Ajout de la logique de fenêtre au bouton de retour au menu
				boutonMenuDefaite.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent ae){ 
						menu.setVisible(true);
						vueDefaite.dispose();
						dispose();
					}
				});
                
                vueDefaite.setContentPane(contentPaneDef);
                setVisible(false);
                vueDefaite.setVisible(true);

            }
        };
    }

}