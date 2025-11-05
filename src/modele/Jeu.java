package modele;

import java.io.*;

import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {
	private static final int MAX_BASE_STATS = 10;
	
	private static Random rnd = new Random(System.currentTimeMillis());
	
	private final String nom_joueur;
	
    private final int base;
    private final int expo;
    private final int score_victoire;
    
    private final boolean prog;
    private int prog1;
    private int prog2;

    private final int l,c;
    private Case[][] tabCases;

    private int maxVal; // = score
    private int casesOccup;
    private boolean gagne;
    private boolean perdu;
    
    private int tour;
    private int fusions_effectuees;
    
    
    public Jeu() {
    	
        this.nom_joueur = "";
        
    	this.base = 2;
    	this.expo = 11;
    	this.score_victoire = (int)Math.pow(base, expo);
    	
        this.prog = false;
        this.prog1 = 2;
        this.prog2 = 4;
        
        this.l = 4;
        this.c = 4;
        this.tabCases = new Case[4][4];
        
        this.maxVal = 0;
        this.casesOccup = 0;
        this.gagne = false;
        this.perdu = false;
        
        this.tour = 1;
        this.fusions_effectuees = 0;
        
        
        ReinitFusionsPuisRemplir();
    }
    ///Jeu 2048 classique
    
    public Jeu(int _base, int _expo,int lignes, int colonnes,boolean _prog, String _nom_joueur) {
    	
    	this.nom_joueur = _nom_joueur;
        this.base = _base;
        this.expo = _expo;
        this.score_victoire = (int)Math.pow(_base, expo);
        this.prog = _prog;
        this.prog1 = _base;
        this.prog2 = (int)Math.pow(_base, 2);;
        
        this.l = lignes;
        this.c = colonnes;
        this.tabCases = new Case[lignes][colonnes];
        
        this.maxVal = 0;
        this.casesOccup = 0;
        this.gagne = false;
        this.perdu = false;
        
        this.tour = 1;
        this.fusions_effectuees = 0;
        
        
        ReinitFusionsPuisRemplir();
    }
    ///Jeu personnalisé
    
    //////Les "getteurs"

    public int getBase() { return base; }

    public int getBasexp(int n) { return (int)Math.pow(base, n); }

    public int getExpo() { return expo; }
    
    public int getProg1() { return prog1; }
    
    public int getProg2() { return prog2; }

    public int getL() { return l; }

    public int getC() { return c; }

    public Case getCase(int i, int j) { return tabCases[i][j]; }
    
    public int getmaxVal() { return maxVal; }
    
    public boolean estGagne() { return gagne; }

    public boolean estPerdu() { return perdu; }
    
    public int getTour() { return tour; }
    
    public int getFusionsEffectuees() { return fusions_effectuees; }
    
    //////Les "setteurs"
    
    public void setmaxVal(int v) { maxVal = v; }
    ///Utilisé par la classe Case lors des fusions
    
    //////Les méthodes de gestion du jeu

    public void effCase(int i, int j) {
        tabCases[i][j] = null;
        decrOccup();
    }

    public void incrOccup() { casesOccup++; }

    public void decrOccup() { casesOccup--; }
    
    public void incrFusionsEffectuees() { fusions_effectuees++; }
    
    
    public void ActionSiPartieNonFinie(Direction d) {
        if (!perdu && !gagne) { action(d); };
    }

    public void action(Direction d){
        switch(d) {
            case haut:
                for (int j = 0 ; j < c ; j++) {
                    for (int i = 1 ; i < l ; i++) {
                        if (tabCases[i][j]!=null) {
                            tabCases[i][j].deplacer(d);
                        };
                    };
                };
                break;
            case bas:
                for (int j = 0 ; j < c ; j++) {
                    for (int i = l-1 ; i > -1 ; i--) {
                        if (tabCases[i][j]!=null) {
                            tabCases[i][j].deplacer(d);
                        };
                    };
                };
                break;
            case gauche:
                for (int i = 0 ; i < l ; i++) {
                    for (int j = 1 ; j < c ; j++) {
                        if (tabCases[i][j]!=null) {
                            tabCases[i][j].deplacer(d);
                        };
                    };
                };
                break;
            case droite:
                for (int i = 0 ; i < l ; i++) {
                    for (int j = c-1 ; j > -1 ; j--) {
                        if (tabCases[i][j]!=null) {
                            tabCases[i][j].deplacer(d);
                        };
                    };
                };
                break;
        };
        
        if (maxVal == score_victoire) {
            gagne = true;
            
            maj_stats();
            
            setChanged();
            notifyObservers();
        }
        else {
            if (casesOccup == l*c) {
                perdu = true;
                
                maj_stats();
                
                setChanged();
                notifyObservers();
            }
            else {
            	int v = Math.max((int)(Math.log(maxVal) / Math.log(base)) - 5,1);
            	prog1 = getBasexp(v);
            	prog2 = getBasexp(v+1);
            	
            	tour++;
            	
                ReinitFusionsPuisRemplir();
            };
        };
    }
    
    
    public void decaleCase(int i, int j, Direction d) {
        Case c = tabCases[i][j];
        switch(d){
            case haut:
                tabCases[i-1][j] = c;
                break;
            case bas:
                tabCases[i+1][j] = c;
                break;
            case gauche:
                tabCases[i][j-1] = c;
                break;
            case droite:
                tabCases[i][j+1] = c;
                break;
        };
        tabCases[i][j] = null;
    }
    ///Utilisée par la classe Case lors des déplacements

    public void ReinitFusionsPuisRemplir() {
    	Jeu jeu = this;
        new Thread() {
            public void run() {
                for (int i = 0 ; i < l ; i++) {
                    for (int j = 0 ; j < c ; j++) {
                    	if (tabCases[i][j] != null) {
                    		tabCases[i][j].reinitFusion();
                    	}
                    	else{
                    		
                    		int amplitude;
                    		if (l*c-casesOccup <= base) {
                    			amplitude = 6;
                    		} else {
                    			amplitude = 6+2*base;
                    		};
                    		
                    		int r = rnd.nextInt(amplitude);
                    		
                    		Case cij = null;
                    		
                    		switch (r) {
                            case 0:
                            case 1:
                                cij = new Case(prog1,i,j,jeu);
                                incrOccup();
                                break;
                            case 2:
                            	cij = new Case(prog2,i,j,jeu);
                            	incrOccup();
                                break;
                            default:
                                break;
                            }
                    		
                            tabCases[i][j] = cij;
                    	}                
                    }
                };
                
                if (prog) {
                	for (int i = 0 ; i < l ; i++) {
                        for (int j = 0 ; j < c ; j++) {
                        	if (tabCases[i][j] != null) { tabCases[i][j].progChange(); };       
                        }
                    };
                };   
            }
        }.start();
        setChanged();
        notifyObservers();
    }
    
    private void maj_stats() {
    	
    	//////Mise à jour des statistiques cumulées
    	
    	File cumul = new File("src/modele/stats_cumul.txt");
        
        try {
        	///Lecture
        	BufferedReader lecture_cumul = new BufferedReader( new FileReader(cumul));
        	
        	String[] lignes_cumul = new String[MAX_BASE_STATS+1];
        	String[][] stats_lignes_cumul = new String[MAX_BASE_STATS+1][4];
        	
        	for (int i = 0 ; i < MAX_BASE_STATS + 1 ; i++) {
        		lignes_cumul[i] = lecture_cumul.readLine();
        		stats_lignes_cumul[i] = lignes_cumul[i].split(",");
        	};
        	
        	lecture_cumul.close();
        	
        	int parties_jouees_gen = Integer.parseInt(stats_lignes_cumul[0][0]);
        	int parties_gagnees_gen = Integer.parseInt(stats_lignes_cumul[0][1]);
        	int parties_perdues_gen = Integer.parseInt(stats_lignes_cumul[0][2]);
        	int fusions_effect_gen = Integer.parseInt(stats_lignes_cumul[0][3]);
        	
        	int parties_jouees_base = Integer.parseInt(stats_lignes_cumul[base-1][0]);
        	int parties_gagnees_base = Integer.parseInt(stats_lignes_cumul[base-1][1]);
        	int parties_perdues_base = Integer.parseInt(stats_lignes_cumul[base-1][2]);
        	int fusions_effect_base = Integer.parseInt(stats_lignes_cumul[base-1][3]);
        	
        	parties_jouees_base++;
        	parties_jouees_gen++;
        	
        	if(gagne) {
        		parties_gagnees_base++;
            	parties_gagnees_gen++;
        	} else {
        		parties_perdues_base++;
            	parties_perdues_gen++;
        	};
        	
        	fusions_effect_base+= fusions_effectuees;
        	fusions_effect_gen+= fusions_effectuees;
        	
        	///Ecriture
        	
        	String nouvelle_ligne_base = String.valueOf(parties_jouees_base) + "," + String.valueOf(parties_gagnees_base) + "," + String.valueOf(parties_perdues_base) + "," + String.valueOf(fusions_effect_base);
        	String nouvelle_ligne_gen = String.valueOf(parties_jouees_gen) + "," + String.valueOf(parties_gagnees_gen) + "," + String.valueOf(parties_perdues_gen) + "," + String.valueOf(fusions_effect_gen);
        	
        	BufferedWriter ecriture_cumul = new BufferedWriter(new FileWriter(cumul));
        	
        	ecriture_cumul.write(nouvelle_ligne_gen + "\n");
        	for (int i = 1 ; i < MAX_BASE_STATS + 1 ; i++) {
        		if (i==base-1) { ecriture_cumul.write(nouvelle_ligne_base + "\n"); }
        		else { ecriture_cumul.write(lignes_cumul[i] + "\n"); };
        	};
        	
        	ecriture_cumul.close();
        	
        } catch(Exception e) {
        	System.out.println(e.toString());
        };
        
        String prog_act;
      	if(prog) { prog_act = "Oui"; } 
      	else { prog_act = "Non"; };
        
        //////Mise à jour des statistiques des scores maximaux
        
        File scores = new File("src/modele/stats_partie_score.txt");
        
        try {
        	///Lecture
        	BufferedReader lecture_scoresmax = new BufferedReader( new FileReader(scores));
          	
          	String[] lignes_scoresmax = new String[MAX_BASE_STATS+1];
          	String[][] stats_lignes_scoresmax = new String[MAX_BASE_STATS+1][7];
          	
          	for (int i = 0 ; i < MAX_BASE_STATS + 1 ; i++) {
          		lignes_scoresmax[i] = lecture_scoresmax.readLine();
          		stats_lignes_scoresmax[i] = lignes_scoresmax[i].split(",");
          	};
          	
          	lecture_scoresmax.close();
          	
          	int val_scoremax_gen = Integer.parseInt(stats_lignes_scoresmax[0][1]);
          	
          	int val_scoremax_base = Integer.parseInt(stats_lignes_scoresmax[base-1][1]);
          	
          	if (maxVal > val_scoremax_base || maxVal > val_scoremax_gen) {
          	    ///Ecriture
          		String nouvelle_ligne = String.valueOf(base) + "," + String.valueOf(maxVal) + "," + nom_joueur + "," + String.valueOf(tour) + "," + String.valueOf(l) + "," + String.valueOf(c) + "," + prog_act;
              	
          		BufferedWriter ecriture_scoresmax = new BufferedWriter(new FileWriter(scores));
          		
          		for (int i = 0 ; i < MAX_BASE_STATS + 1 ; i++) {
          			if (i == 0 && maxVal > val_scoremax_gen) { ecriture_scoresmax.write(nouvelle_ligne + "\n"); }
          			else {
              			if (i == base-1 && maxVal > val_scoremax_base) { ecriture_scoresmax.write(nouvelle_ligne + "\n"); }
                  		else { ecriture_scoresmax.write(lignes_scoresmax[i] + "\n"); };
              		};
              	};
          		
          		ecriture_scoresmax.close();
          	};
          	
        } catch(Exception e) {
        	System.out.println(e.toString());
        };
        
        //////Mise à jour des statistiques des tours gagnants minimaux
        
        if(perdu) { return; };
        //Si la partie a été perdue, on ne modifie rien et on a finit, sinon, on continue
        
        File tours = new File("src/modele/stats_partie_tour.txt");
        
        try {
        	///Lecture
        	BufferedReader lecture_toursmin = new BufferedReader( new FileReader(tours));
          	
          	String[] lignes_toursmin = new String[MAX_BASE_STATS+1];
          	String[][] stats_lignes_toursmin = new String[MAX_BASE_STATS+1][7];
          	
          	for (int i = 0 ; i < MAX_BASE_STATS + 1 ; i++) {
          		lignes_toursmin[i] = lecture_toursmin.readLine();
          		stats_lignes_toursmin[i] = lignes_toursmin[i].split(",");
          	};
          	
          	lecture_toursmin.close();
          	
          	int val_tourmin_gen = entierTour(stats_lignes_toursmin[0][2]);
          	int val_tourmin_base = entierTour(stats_lignes_toursmin[base-1][2]);
          	
          	if (tour < val_tourmin_base || tour < val_tourmin_gen) {
          	    ///Ecriture
          		String nouvelle_ligne = String.valueOf(base) + "," + String.valueOf(expo) + "," + String.valueOf(tour) + "," + nom_joueur + "," + String.valueOf(l) + "," + String.valueOf(c) + "," + prog_act;
              	
          		BufferedWriter ecriture_toursmin = new BufferedWriter(new FileWriter(tours));
          		
          		for (int i = 0 ; i < MAX_BASE_STATS + 1 ; i++) {
          			if (i == 0 && tour < val_tourmin_gen) { ecriture_toursmin.write(nouvelle_ligne + "\n"); }
          			else {
              			if (i == base-1 && tour < val_tourmin_base) { ecriture_toursmin.write(nouvelle_ligne + "\n"); }
                  		else { ecriture_toursmin.write(lignes_toursmin[i] + "\n"); };
              		};
              	};
          		
          		ecriture_toursmin.close();
          	};
          	
        } catch(Exception e) {
        	System.out.println(e.toString());
        };
    }
    
    private static int entierTour(String str) {
		try {
			int val = Integer.parseInt(str);  
		    return val;
		} catch (NumberFormatException e){  
		    return Integer.MAX_VALUE;  
		} 
	}
    // Utilisée par maj_stats
    // (Pour obtenir la valeur entière du tour gagnant minimum à comparer avec la valeur de tour gagant d'une partie gagnée)

};