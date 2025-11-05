import modele.Jeu;
import vue_controleur.Console2048;
import vue_controleur.Swing2048;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Main {
	private static final String NOM_APPLI = "2048*";
	private static final Color CYAN = new Color(160,217,234);
	private static final Color GRIS = new Color(195,195,195);
	private static final int MAX_BASE = 10;
	private static final boolean UTILISER_ICONES = true;
	private static final int LARGEUR_FEN_STATS = 80 * MAX_BASE;
	private static final int HAUTEUR_FEN_STATS = 600;
	private static final int NB_STATS_AFFICHEES = (4 + 7 + 7);
	private static final int NB_PARAM = 7;
	private static final int LARGEUR_FEN_PARAM = 80 * MAX_BASE;
	private static final int HAUTEUR_FEN_PARAM = 60 * NB_PARAM + 2;
	private static final Dimension FORMAT_BOUTON = new Dimension(150,50);
	private static final Dimension FORMAT_PETIT_BOUTON = new Dimension(30,30);
	
	private static final Dimension FORMAT_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        //mainConsole();
        mainSwing();

    }

    public static void mainConsole() {
        System.out.println("----------- " + NOM_APPLI + " -----------\n");
        
        System.out.println("Taille de la grille de jeu:");
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Base=");
        int base = scan.nextInt();
        if( base <=1 ) { base = 2; };
        
        System.out.println("Puissance=");
        int puissance = scan.nextInt();
        
        System.out.println("Mode progressif:");
        int prog_act = scan.nextInt();
        
        System.out.println("Lignes=");
        int lignes = scan.nextInt();
        
        System.out.println("Colonnes=");
        int colonnes = scan.nextInt();
        
        ///  /!\ NE PAS fermer le Scanner /!\ Si on le fait, Console2048 ne sera plus capable de lire la saisie de l'utilisateur
        ///  (Fermer un seul Scanner d'entrée 'System.in' du programme => Fermer System.in complètement.)
		///  Je sais que ce n'est pas une bonne habitude de laisser un Scanner ouvert après avoir fini de l'utiliser, mais que voulez-vous.. quand Java casse les pieds...
		///  De toute façon comme Java n'est pas si mal fait que ça sur ce point, il fermera automatiquement la Scanner après avoir fini de s'en servir.
        
        Jeu jeu;
        
        if (prog_act == 1) { jeu = new Jeu( base, puissance, lignes, colonnes, true, "Joueur CONSOLE");
        } else { jeu = new Jeu( base, puissance, lignes, colonnes, false, "Joueur CONSOLE"); };

        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();

    }
    
    private static int entier(String str, int par_defaut) {
		try {
			int val = Integer.parseInt(str);  
		    return val;
		} catch (NumberFormatException e){  
		    return par_defaut;  
		}  
	}

    public static void mainSwing() {
    	
    	JFrame fenetreMenu = new JFrame();
		fenetreMenu.setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI +" - Menu");
		fenetreMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetreMenu.setPreferredSize(new Dimension(500,400));
		fenetreMenu.getContentPane().setBackground(CYAN);
		
		fenetreMenu.setLayout(new GridBagLayout());
		
		JPanel panneauCentralMenu = new JPanel();
		fenetreMenu.add(panneauCentralMenu, new GridBagConstraints());
		panneauCentralMenu.setBackground(CYAN);
		panneauCentralMenu.setLayout(new BorderLayout());
		
		JLabel labelTitreMenu = new JLabel(NOM_APPLI);
		panneauCentralMenu.add(labelTitreMenu,BorderLayout.PAGE_START);
		labelTitreMenu.setPreferredSize(new Dimension(150,80));
		labelTitreMenu.setFont(new Font("Helvetica", Font.PLAIN, 40));
		labelTitreMenu.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitreMenu.setHorizontalTextPosition(JLabel.CENTER);
		
		///Création et ajout du bouton Jouer
		JButton boutonJouer = new JButton("Jouer");
		panneauCentralMenu.add(boutonJouer,BorderLayout.CENTER);
		boutonJouer.setPreferredSize(FORMAT_BOUTON);
		///
		///Création et ajout du bouton Statistiques
		JButton boutonStats = new JButton("Statistiques");
		panneauCentralMenu.add(boutonStats,BorderLayout.PAGE_END);
		boutonStats.setPreferredSize(FORMAT_BOUTON);
		///

		//////Ajout de la logique de fenêtre(s) / des liens de fenêtre(s):
		
		boutonStats.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent ae){ 
				///Le bouton Statistiques conduit à la fenêtre des statistiques de jeu...
				fenetreMenu.setVisible(false);
				
			    JFrame fenetreStats = new JFrame();
			    fenetreStats.setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI + " - Statistiques");
			    fenetreStats.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    fenetreStats.setPreferredSize(new Dimension(LARGEUR_FEN_STATS,HAUTEUR_FEN_STATS));
			    fenetreStats.getContentPane().setBackground(GRIS);
				
				JPanel panneauStats = new JPanel();
				fenetreStats.add(panneauStats);
				panneauStats.setBackground(GRIS);
				panneauStats.setLayout(new BorderLayout());
				
				JPanel panneauStatsInf = new JPanel();
				panneauStats.add(panneauStatsInf, BorderLayout.PAGE_END);
				panneauStatsInf.setBackground(GRIS);
				panneauStatsInf.setLayout(new GridBagLayout());
				panneauStatsInf.setPreferredSize(new Dimension(LARGEUR_FEN_STATS,70));
				
				///Création et ajout du bouton Effacer
				JButton boutonEffacerStats = new JButton("Effacer statistiques");
				panneauStatsInf.add(boutonEffacerStats, new GridBagConstraints());
				boutonEffacerStats.setPreferredSize(FORMAT_BOUTON);
				boutonEffacerStats.setBackground(Color.ORANGE);
				boutonEffacerStats.setOpaque(true);
				///

				JTabbedPane panneauxStatsSup = new JTabbedPane();
				panneauStats.add(panneauxStatsSup, BorderLayout.CENTER);
				
				JPanel panRetourStatsSup = new JPanel();
				panRetourStatsSup.setBackground(GRIS);
				panRetourStatsSup.setLayout(new GridBagLayout());
				panRetourStatsSup.setPreferredSize(new Dimension(LARGEUR_FEN_STATS,60));

				///Création et ajout du bouton de retour au menu
				JButton boutonRetourStats = new JButton();
				panRetourStatsSup.add(boutonRetourStats, new GridBagConstraints());
				boutonRetourStats.setPreferredSize(FORMAT_PETIT_BOUTON);
				if (UTILISER_ICONES) {
					boutonRetourStats.setIcon(new ImageIcon("src/vue_controleur/retour.png"));
				} else {
					boutonRetourStats.setBackground(CYAN);
					boutonRetourStats.setOpaque(true);
				};
				
				panneauStats.add(panRetourStatsSup, BorderLayout.PAGE_START);
				///

		        ////////Récupération des statistiques dans les fichiers texte
				///Fichier des statistiques cumulées
				File cumul = new File("src/modele/stats_cumul.txt");
				
				String[][] stats_lignes_cumul = new String[MAX_BASE+1][4];
		        
		        try {
		        	BufferedReader lecture_cumul = new BufferedReader( new FileReader(cumul));
		        	
		        	String ligne_cumul;
		        	
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		ligne_cumul = lecture_cumul.readLine();
		        		stats_lignes_cumul[i] = ligne_cumul.split(",");
		        	};
		        	lecture_cumul.close();
		        } catch (Exception e) {
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		stats_lignes_cumul[i][0] = "[Inaccessible]";
		        		stats_lignes_cumul[i][1] = "[Inaccessible]";
		        		stats_lignes_cumul[i][2] = "[Inaccessible]";
		        		stats_lignes_cumul[i][3] = "[Inaccessible]";
		        	};
		        };
		        
		        ///Fichier des statistiques des scores maximaux
		        File scores = new File("src/modele/stats_partie_score.txt");
		        
		        String[][] stats_lignes_scores = new String[MAX_BASE+1][7];
		        
		        try {
		        	
		        	BufferedReader lecture_scores = new BufferedReader( new FileReader(scores));
		        	
		        	String ligne_scores;
		        	
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		
		        		ligne_scores = lecture_scores.readLine();
		        		
		        		stats_lignes_scores[i] = ligne_scores.split(",");
		        	};
		        	
		        	lecture_scores.close();
		        } catch (Exception e) {
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		stats_lignes_scores[i][0] = "[Inaccessible]";
		        		stats_lignes_scores[i][1] = "[Inaccessible]";
		        		stats_lignes_scores[i][2] = "[Inaccessible]";
		        		stats_lignes_scores[i][3] = "[Inaccessible]";
		        		stats_lignes_scores[i][4] = "[Inaccessible]";
		        		stats_lignes_scores[i][5] = "[Inaccessible]";
		        		stats_lignes_scores[i][6] = "[Inaccessible]";
		        		
		        	};
		        };
		        
		        ///Fichier des statistiques des tours gagnants minimaux
		        File tours = new File("src/modele/stats_partie_tour.txt");
		        
		        String[][] stats_lignes_tours = new String[MAX_BASE+1][7];
		        
		        try {
		        	
		        	BufferedReader lecture_tours = new BufferedReader( new FileReader(tours));
		        	
		        	String ligne_tours;
		        	
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		
		        		ligne_tours = lecture_tours.readLine();
		        		
		        		stats_lignes_tours[i] = ligne_tours.split(",");
		        	};
		        	
		        	lecture_tours.close();
		        } catch (Exception e) {
		        	for (int i = 0; i < MAX_BASE + 1; i++) {
		        		stats_lignes_tours[i][0] = "[Inaccessible]";
		        		stats_lignes_tours[i][1] = "[Inaccessible]";
		        		stats_lignes_tours[i][2] = "[Inaccessible]";
		        		stats_lignes_tours[i][3] = "[Inaccessible]";
		        		stats_lignes_tours[i][4] = "[Inaccessible]";
		        		stats_lignes_tours[i][5] = "[Inaccessible]";
		        		stats_lignes_tours[i][6] = "[Inaccessible]";
		        		
		        	};
		        };
		        
		        ////////Affichage des statistiques récupérées dans la fenêtre
		        JPanel[] panneauStatsSupTab = new JPanel[MAX_BASE];
				//Les panneaux de tous les tab (1 par base + le tab général)
				JLabel[] labelStatsSupTabTitre = new JLabel[MAX_BASE];
				//Les titre des panneaux de tous les tab (1 par base + le tab général)
		        JLabel[][] labelStatsSupTab = new JLabel[MAX_BASE][NB_STATS_AFFICHEES];
				//Les label texte des panneaux de tous les tab (1 par base + le tab général, 18 pour chaque panneau)
				
				for (int i = 0 ; i < MAX_BASE ; i++) {
					panneauStatsSupTab[i] = new JPanel();
					panneauStatsSupTab[i].setBackground(GRIS);
					panneauStatsSupTab[i].setPreferredSize(new Dimension(LARGEUR_FEN_STATS,HAUTEUR_FEN_STATS+60+70));
					panneauStatsSupTab[i].setLayout(new GridLayout(NB_STATS_AFFICHEES + 4,1));
					panneauxStatsSup.add(panneauStatsSupTab[i]);

					labelStatsSupTabTitre[i] = new JLabel("Base " + (i+1));
					labelStatsSupTabTitre[i].setFont(new Font("Helvetica", Font.BOLD, 12));
					labelStatsSupTabTitre[i].setPreferredSize(new Dimension(50,20));
					panneauxStatsSup.setTabComponentAt(i, labelStatsSupTabTitre[i]);
					
					////Ecriture des statistiques cumulées
					labelStatsSupTab[i][0] = new JLabel("    Parties jouées: " + stats_lignes_cumul[i][0] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][0]);
					labelStatsSupTab[i][1] = new JLabel("         Parties gagnées: " + stats_lignes_cumul[i][1] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][1]);
					labelStatsSupTab[i][2] = new JLabel("         Parties perdues: " + stats_lignes_cumul[i][2] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][2]);
					labelStatsSupTab[i][3] = new JLabel("    Fusions effectuées: " + stats_lignes_cumul[i][3] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][3]);
					
				    ////Ecriture des statistiques des scores maximaux
					labelStatsSupTab[i][4] = new JLabel("  ");
					panneauStatsSupTab[i].add(labelStatsSupTab[i][4]);
					labelStatsSupTab[i][5] = new JLabel("    Score maximum obtenu: " + stats_lignes_scores[i][1] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][5]);
					labelStatsSupTab[i][6] = new JLabel("         Base: " + stats_lignes_scores[i][0] );
					if(i == 0) {
						panneauStatsSupTab[i].add(labelStatsSupTab[i][6]);
					} else {
						panneauStatsSupTab[i].add(new JLabel(""));
					};
					labelStatsSupTab[i][7] = new JLabel("         Joueur: " + stats_lignes_scores[i][2] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][7]);
					labelStatsSupTab[i][8] = new JLabel("         Tours: " + stats_lignes_scores[i][3] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][8]);
					labelStatsSupTab[i][9] = new JLabel("         Format grille (Lignes x Colonnes): " + stats_lignes_scores[i][4] + "x" + stats_lignes_scores[i][5] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][9]);
					labelStatsSupTab[i][10] = new JLabel("         Mode progressif activé: " + stats_lignes_scores[i][6] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][10]);
					
				    ////Ecriture des statistiques des tours gagnants minimaux
					labelStatsSupTab[i][11] = new JLabel("  ");
					panneauStatsSupTab[i].add(labelStatsSupTab[i][11]);
					labelStatsSupTab[i][12] = new JLabel("    Tour de victoire minimum: " + stats_lignes_tours[i][2] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][12]);
					if(i == 0) {
						labelStatsSupTab[i][13] = new JLabel("         Base / Puissance: " + stats_lignes_tours[i][0] + " / " + stats_lignes_tours[i][1]);
					} else {
						labelStatsSupTab[i][13] = new JLabel("         Puissance atteinte: " + stats_lignes_tours[i][1]);
					};
					panneauStatsSupTab[i].add(labelStatsSupTab[i][13]);
					labelStatsSupTab[i][14] = new JLabel("         Joueur: " + stats_lignes_tours[i][3] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][14]);
					labelStatsSupTab[i][15] = new JLabel("         Format grille (Lignes x Colonnes): " + stats_lignes_tours[i][4] + "x" + stats_lignes_tours[i][5] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][15]);
					labelStatsSupTab[i][16] = new JLabel("         Mode progressif activé: " + stats_lignes_tours[i][6] );
					panneauStatsSupTab[i].add(labelStatsSupTab[i][16]);
				};
				labelStatsSupTabTitre[0].setText("Général");

				///On programme l'effet du bouton de retour au menu
				boutonRetourStats.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent ae){ 
						fenetreStats.dispose();
						fenetreMenu.setVisible(true);
					}
				});
				
				///On programme l'effet du bouton d'effaçage des statistiques
				boutonEffacerStats.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent ae){ 
						
						try {
						BufferedWriter ecriture_cumul = new BufferedWriter(new FileWriter(cumul));
			        	
						String ligne_nulle = "0,0,0,0\n";
						
						ecriture_cumul.write( ligne_nulle );
			        	for (int i = 0; i < MAX_BASE ; i++) {
			        	    ecriture_cumul.write( ligne_nulle );
    
						    labelStatsSupTab[i][0].setText("    Parties jouées: 0");
			        	    labelStatsSupTab[i][1].setText("         Parties gagnées: 0");
			        	    labelStatsSupTab[i][2].setText("         Parties perdues: 0");
			        	    labelStatsSupTab[i][3].setText("    Fusions effectuées: 0");
			        	};
			        	
			        	ecriture_cumul.close();
			        	
						} catch (Exception e){
							System.out.println("Effacement des statistiques cumulées de jeu impossible (erreur d'accès fichier):");
							System.out.println(e.toString());
						};
						
						try {
							BufferedWriter ecriture_scores = new BufferedWriter(new FileWriter(scores));
				        	
							String ligne_nulle = "0,0,0,0,0,0,0\n";
							
							ecriture_scores.write( ligne_nulle );
				        	for (int i = 0; i < MAX_BASE ; i++) {
				        		ecriture_scores.write( ligne_nulle );

								labelStatsSupTab[i][5].setText("    Score maximum obtenu: 0");
				        		if (i == 0) {
				        			labelStatsSupTab[i][6].setText("         Base: 0");
				        		};
				        		labelStatsSupTab[i][7].setText("         Joueur: 0");
				        		labelStatsSupTab[i][8].setText("         Tours: 0");
				        		labelStatsSupTab[i][9].setText("         Format grille (Lignes x Colonnes): 0x0");
				        		labelStatsSupTab[i][10].setText("         Mode progressif activé: 0");
				        	};
				        	
				        	ecriture_scores.close();
				        	
						} catch (Exception e){
							System.out.println("Effacement des statistiques score de jeu impossible (erreur d'accès fichier):");
							System.out.println(e.toString());
						};
							
						try {
							BufferedWriter ecriture_tours = new BufferedWriter(new FileWriter(tours));
					    	
							String ligne_nulle = "0,0,inf,0,0,0,0\n";
							
							ecriture_tours.write( ligne_nulle );
					    	for (int i = 0; i < MAX_BASE ; i++) {
					    		ecriture_tours.write( ligne_nulle );
							
								labelStatsSupTab[i][12].setText("    Tour de victoire minimum: inf");
					        	if (i == 0) {
					        		labelStatsSupTab[i][13].setText("         Base / Puissance: 0 / 0");
					        	} else {
					        		labelStatsSupTab[i][13].setText("         Puissance atteinte: 0");
					        	};
					        	labelStatsSupTab[i][14].setText("         Joueur: 0");
					        	labelStatsSupTab[i][15].setText("         Format grille (Lignes x Colonnes): 0x0");
					        	labelStatsSupTab[i][16].setText("         Mode progressif activé: 0");
					        };
					    	
					        ecriture_tours.close();
					        	
						} catch (Exception e){
							System.out.println("Effacement des statistiques tours de jeu impossible (erreur d'accès fichier):");
							System.out.println(e.toString());
						};
					}
				});
				
				///On ajuste les contenus de la fenêtre puis on la révèle à l'utilisateur
				fenetreStats.pack();
				fenetreStats.setLocation((int)FORMAT_ECRAN.getWidth()/2 - fenetreStats.getWidth() / 2,(int)FORMAT_ECRAN.getHeight()/2 - fenetreStats.getHeight() /2);
				fenetreStats.setVisible(true);
			
			}
		});
		

		boutonJouer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){ 
				///Le bouton Statistiques conduit à la fenêtre de création d'une partie (paramètres)... 
				fenetreMenu.setVisible(false);
				
			    JFrame fenetreParam = new JFrame();
			    fenetreParam.setTitle("[ INF3007L - Projet binôme 17 ] " + NOM_APPLI + " - Paramètres de jeu");
			    fenetreParam.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    fenetreParam.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,HAUTEUR_FEN_PARAM));
			    fenetreParam.getContentPane().setBackground(GRIS);
				
				JPanel panneauParam = new JPanel();
				fenetreParam.add(panneauParam);
				panneauParam.setBackground(GRIS);
				panneauParam.setLayout(new BorderLayout());
				
				JPanel panneauParamInf = new JPanel();
				panneauParam.add(panneauParamInf, BorderLayout.PAGE_END);
				panneauParamInf.setBackground(GRIS);
				panneauParamInf.setLayout(new GridBagLayout());
				panneauParamInf.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				
				///Création et ajout du bouton Lancer
				JButton boutonLancer = new JButton("Lancer");
				panneauParamInf.add(boutonLancer, new GridBagConstraints());
				boutonLancer.setPreferredSize(FORMAT_BOUTON);
				boutonLancer.setBackground(Color.GREEN);
				boutonLancer.setOpaque(true);
				///
				
				JPanel panneauParamSup = new JPanel();
				panneauParam.add(panneauParamSup, BorderLayout.PAGE_START);
				panneauParamSup.setBackground(GRIS);
				panneauParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,HAUTEUR_FEN_PARAM-60));
				panneauParamSup.setLayout(new GridLayout(NB_PARAM+2,1));
				
				JPanel panneauRetourParamSup = new JPanel();
				panneauParamSup.add(panneauRetourParamSup);
				panneauRetourParamSup.setBackground(GRIS);
				panneauRetourParamSup.setLayout(new GridBagLayout());
				panneauRetourParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				
				///Création et ajout du bouton Effacer
				JButton boutonRetourParam = new JButton();
				panneauRetourParamSup.add(boutonRetourParam, new GridBagConstraints());
				boutonRetourParam.setPreferredSize(FORMAT_PETIT_BOUTON);
				if (UTILISER_ICONES) {
					boutonRetourParam.setIcon(new ImageIcon("src/vue_controleur/retour.png"));
				} else {
					boutonRetourParam.setBackground(CYAN);
					boutonRetourParam.setOpaque(true);
				};
				///

				//////On crée et ajoute un panneau pour chaque paramètre ou donnée de jeu:

				///Le nom de joueur/pseudonyme:
				JPanel panneauJoueurParamSup = new JPanel();
				panneauParamSup.add(panneauJoueurParamSup);
				panneauJoueurParamSup.setBackground(GRIS);
				panneauJoueurParamSup.setLayout(new FlowLayout());
				panneauJoueurParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauJoueurParamSup.add(new JLabel("Nom Joueur:"));
				JTextField nomJoueur = new JTextField(20);
				panneauJoueurParamSup.add(nomJoueur);
				
				///Le choix de la base:
				JPanel panneauBaseParamSup = new JPanel();
				panneauParamSup.add(panneauBaseParamSup);
				panneauBaseParamSup.setBackground(GRIS);
				panneauBaseParamSup.setLayout(new GridLayout(1,16));
				panneauBaseParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauBaseParamSup.add(new JLabel("   Base:"));
				
				ButtonGroup grpBoutonsSelecBase = new ButtonGroup();
				JRadioButton[] boutonsSelecBase = new JRadioButton[MAX_BASE-1];
				
				for (int i = 0 ; i < MAX_BASE - 1 ; i++) {
					boutonsSelecBase[i] = new JRadioButton(""+(i+2));
					boutonsSelecBase[i].setBackground(GRIS);
					grpBoutonsSelecBase.add(boutonsSelecBase[i]);
					panneauBaseParamSup.add(boutonsSelecBase[i]);
				};
				boutonsSelecBase[0].setSelected(true);
				
				///Le choix de la puissance gagnante:
				JPanel panneauPuissanceParamSup = new JPanel();
				panneauParamSup.add(panneauPuissanceParamSup);
				panneauPuissanceParamSup.setBackground(GRIS);
				panneauPuissanceParamSup.setLayout(new FlowLayout());
				panneauPuissanceParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauPuissanceParamSup.add(new JLabel("Puissance:"));
				JTextField puissanceTexte = new JTextField("11",3);
				panneauPuissanceParamSup.add(puissanceTexte);
				
				///L'activation ou non du mode progressif:
				JPanel panneauProgressifParamSup = new JPanel();
				panneauParamSup.add(panneauProgressifParamSup);
				panneauProgressifParamSup.setBackground(GRIS);
				panneauProgressifParamSup.setLayout(new FlowLayout());
				panneauProgressifParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauProgressifParamSup.add(new JLabel("Mode progressif:"));

				JCheckBox progCase = new JCheckBox("",false);
				panneauProgressifParamSup.add(progCase);
				progCase.setBackground(GRIS);
				
				///Le choix du format de la grille de jeu:
				JPanel panneauFormatParamSup = new JPanel();
				panneauParamSup.add(panneauFormatParamSup);
				panneauFormatParamSup.setBackground(GRIS);
				panneauFormatParamSup.setLayout(new FlowLayout());
				panneauFormatParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauFormatParamSup.add(new JLabel("Format de la grille de jeu:"),BorderLayout.LINE_START);
				
				///Les lignes:
				JPanel panneauLignesParamSup = new JPanel();
				panneauParamSup.add(panneauLignesParamSup);
				panneauLignesParamSup.setBackground(GRIS);
				panneauLignesParamSup.setLayout(new FlowLayout());
				panneauLignesParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauLignesParamSup.add(new JLabel("    Lignes:"));
				JTextField lignesTexte = new JTextField("4",2);
				panneauLignesParamSup.add(lignesTexte);
				
				///Les colonnes:
				JPanel panneauColonnesParamSup = new JPanel();
				panneauParamSup.add(panneauColonnesParamSup);
				panneauColonnesParamSup.setBackground(GRIS);
				panneauColonnesParamSup.setLayout(new FlowLayout());
				panneauColonnesParamSup.setPreferredSize(new Dimension(LARGEUR_FEN_PARAM,60));
				panneauColonnesParamSup.add(new JLabel("    Colonnes:"));
				JTextField colonnesTexte = new JTextField("4",2);
				panneauColonnesParamSup.add(colonnesTexte);
				
				//////

				///On programme l'effet du bouton de retour au menu
				boutonRetourParam.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent ae){ 
						fenetreParam.dispose();
						fenetreMenu.setVisible(true);
					}
				});
				
				///On programme l'effet du bouton de lancement d'une partie
				boutonLancer.addActionListener(new ActionListener(){  
					public void actionPerformed(ActionEvent ae){ 
						fenetreParam.setVisible(false);
						
						///On récupère les paramètres de jeu rentrés par l'utilisateur
						String joueurJeu = nomJoueur.getText();
						if (joueurJeu == null) {
							joueurJeu = "";
						};

						joueurJeu = joueurJeu.replaceAll(","," ");
						// Si le joueur saisit un pseudonyme avec une virgule et qu'il effectue un record et qu'on l'utilise dans les fichiers texte de statistiques,
						// nos fichiers n'auront plus le bon format pour être lus correctement par notre application, donc, pour parer à ce problème, on remplace toutes
						// les occurences d'une virgule dans le pseudonyme par un espace qui lui ne porte pas atteinte à l'intégrité de nos formats de fichiers
						
						int baseJeu = 2;
						for (int i = 0 ; i < MAX_BASE - 1 ; i++) {
							if (boutonsSelecBase[i].isSelected()) {
								baseJeu = 2+i;
								break;
							};
						};
						//Remarque: il n'y aura qu'un seul bouton sélectionné donc pas de problème avec cette boucle
						
						int expoJeu = entier(puissanceTexte.getText(),11);
						
						boolean mode_progJeu;
						if (progCase.isSelected()) {
							mode_progJeu = true;
						} else {
							mode_progJeu = false;
						};
						
						int lignesJeu = entier(lignesTexte.getText(),4);
						
						int colonnesJeu = entier(colonnesTexte.getText(),4);
						
						///Création et affichage du jeu avec les paramètres récupérés
						Jeu jeu = new Jeu( baseJeu, expoJeu, lignesJeu, colonnesJeu, mode_progJeu, joueurJeu);
						Swing2048 vue = new Swing2048(jeu,fenetreMenu);
						jeu.addObserver(vue);
						vue.setVisible(true);

					}
				});
				
				///On ajuste les contenus de la fenêtre puis on la révèle à l'utilisateur
				fenetreParam.pack();
		        fenetreParam.setLocation((int)( FORMAT_ECRAN.getWidth()/2 - fenetreParam.getWidth() / 2),(int)( FORMAT_ECRAN.getHeight()/2 - fenetreParam.getHeight() /2));
				fenetreParam.setVisible(true);
			
			}
		}); 
		////// 
		
		///On ajuste les contenus de la fenêtre puis on la révèle à l'utilisateur
		fenetreMenu.pack();
		fenetreMenu.setLocation((int)( FORMAT_ECRAN.getWidth()/2 - fenetreMenu.getWidth() / 2),(int)( FORMAT_ECRAN.getHeight()/2 - fenetreMenu.getHeight() /2));
		fenetreMenu.setVisible(true);
    }

}
