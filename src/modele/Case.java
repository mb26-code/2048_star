package modele;

public class Case {
	
    private int valeur;
    
    private int ind_l,ind_c;
    
    private Jeu jeu;
    
    private int fusion_opp;
    private boolean fusion;

    public Case(int _valeur,int _ind_l, int _ind_c, Jeu _jeu) {
        this.valeur = _valeur;
        
        this.ind_l = _ind_l;
        this.ind_c = _ind_c;
        
        this.jeu = _jeu;
        jeu.setmaxVal(Math.max(jeu.getmaxVal(), valeur));
        
        this.fusion = false;
        this.fusion_opp = 0;
    }

    public int getValeur() { return valeur; }
    
    public void progChange() {
        if (valeur == jeu.getBasexp(2)) { valeur = jeu.getProg2(); };
        if (valeur <= jeu.getProg1()) { valeur = jeu.getProg1(); };
    }

    public void reinitFusion(){
        fusion = false;
        fusion_opp = 0;
    }

    public Case voisin(Direction d) {
        Case vois = null;
        switch(d) {
            case haut:
                if (0 <= ind_l-1 && ind_l-1 < jeu.getL()) { vois = jeu.getCase(ind_l-1,ind_c); };
                break;
            case bas:
                if (0 <= ind_l+1 && ind_l+1 < jeu.getL()) { vois = jeu.getCase(ind_l+1,ind_c); };
                break;
            case gauche:
                if (0 <= ind_c-1 && ind_c-1 < jeu.getC()) { vois = jeu.getCase(ind_l,ind_c-1); };
                break;
            case droite:
                if (0 <= ind_c+1 && ind_c+1 < jeu.getC()) { vois = jeu.getCase(ind_l,ind_c+1); };
                break;
        };
        return vois;
    }

    private void fusionne(Direction d) {
    	Case[] tabchaine = new Case[jeu.getBase()];
    	
    	tabchaine[jeu.getBase()-1] = this;
    	for (int i = jeu.getBase()-2 ; i>=0 ; i--) { tabchaine[i] = tabchaine[i+1].voisin(d); };
    	
    	tabchaine[0].valeur = tabchaine[0].valeur * jeu.getBase();
    	if (tabchaine[0].valeur > jeu.getmaxVal()) { jeu.setmaxVal(tabchaine[0].valeur); };
        
        for (int i=1 ; i<jeu.getBase() ; i++) { jeu.effCase(tabchaine[i].ind_l,tabchaine[i].ind_c); };
        jeu.incrFusionsEffectuees();
        
        tabchaine[0].fusion = true;
    }

    public void deplacer(Direction d) {
        Case voisine = voisin(d);
        switch(d) {
            case haut:
                while(ind_l!=0 && voisine == null) {
                    jeu.decaleCase(ind_l,ind_c,d);
                    ind_l = ind_l-1;
                    voisine = voisin(d);
                };
                
                if (ind_l!=0 && voisine.valeur==valeur && !voisine.fusion) {
                	fusion_opp = voisine.fusion_opp+1;
                	if (fusion_opp==jeu.getBase()-1) { fusionne(d); };
                };
                break;
            case bas:
                while(ind_l!=jeu.getL()-1 && voisine == null) {
                    jeu.decaleCase(ind_l,ind_c,d);
                    ind_l = ind_l+1;
                    voisine = voisin(d);
                };
                
                if (ind_l!=jeu.getL()-1 && voisine.valeur==valeur && !voisine.fusion) {
                	fusion_opp = voisine.fusion_opp+1;
                	if (fusion_opp==jeu.getBase()-1) { fusionne(d); };
                };
                break;
            case gauche:
                while(ind_c!=0 && voisine == null) {
                    jeu.decaleCase(ind_l,ind_c,d);
                    ind_c = ind_c-1;
                    voisine = voisin(d);
                };
                
                if (ind_c!=0 && voisine.valeur==valeur && !voisine.fusion) {
                	fusion_opp = voisine.fusion_opp+1;
                	if (fusion_opp==jeu.getBase()-1) { fusionne(d); };
                };
                break;
            case droite:
                while(ind_c!=jeu.getC()-1 && voisine == null) {
                    jeu.decaleCase(ind_l,ind_c,d);
                    ind_c = ind_c+1;
                    voisine = voisin(d);
                };
                
                if (ind_c!=jeu.getC()-1 && voisine.valeur==valeur && !voisine.fusion) {
                	fusion_opp = voisine.fusion_opp+1;
                	if (fusion_opp==jeu.getBase()-1) { fusionne(d); };
                };
                break;
        };
    }
}
