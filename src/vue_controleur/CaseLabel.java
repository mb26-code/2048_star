package vue_controleur;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

import modele.Case;

public class CaseLabel extends JLabel {
    private Case c;
    private static final Border BORDER = BorderFactory.createLineBorder(new Color(187,173,160), 2);
    private static final Color[] COULEURS = {
            new Color(38,108,255),
            new Color(83,184,254),
            new Color(138,246,222),
            new Color(41,250,60),
            new Color(165,255,20),
            new Color(254,236,12),
            new Color(255,145,20),
            new Color(255,104,1),
            new Color(255,16,0),
            new Color(255,9,120),
            new Color(249,86,203),
            new Color(213,35,235),
            new Color(212,120,253),
    		new Color(250,197,243)};
    private static final int NB_COULEURS = 14;
    private static final Color MARRON = new Color(205,193,180);

    public CaseLabel(Case _c) {
        this.c = _c;

        setBorder(BORDER);
        setFont(new Font("Helvetica", Font.BOLD, Math.max((int) 20 * ((Swing2048.PIXELS_PAR_CASE) / 40), 10)));
        setBackground(new Color(205,193,180));
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
    };
    
    public void modifCase(Case _c){ this.c = _c; }

    public void nettoie(){ setBackground(MARRON); }

    public void majCouleur(int b){
       if(c!= null){
           int log = (int) (Math.log(c.getValeur()) / Math.log(b) - 1);
           if (log < NB_COULEURS-1) { setBackground(COULEURS[log]); }
           else { setBackground(Color.white); };
       }
    }
    
}
