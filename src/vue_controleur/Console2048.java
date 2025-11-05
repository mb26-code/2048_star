package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Console2048 extends Thread implements Observer {

    private Jeu jeu;

    public Console2048(Jeu _jeu) {
        jeu = _jeu;

    }


    @Override
    public void run() {
        while(true) {
            afficher();

            synchronized (this) {
                ecouteEvennementClavier();
                try {
                    wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void ecouteEvennementClavier() {

        final Object _this = this;

        new Thread() {
            public void run() {

                synchronized (_this) {
                    boolean end = false;

                    while (!end) {
                        String s = null;
                        try {
                        	s = Character.toString((char)System.in.read());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        switch(s) {
                            case "8":
                                end = true;
                                if (jeu.getmaxVal()==0) {jeu.ReinitFusionsPuisRemplir(); break;}
                                else {jeu.ActionSiPartieNonFinie(Direction.haut); break;}
                            case "4":
                                end = true;
                                if (jeu.getmaxVal()==0) {jeu.ReinitFusionsPuisRemplir(); break;}
                                else {jeu.ActionSiPartieNonFinie(Direction.gauche); break;}
                            case "6":
                                end = true;
                                if (jeu.getmaxVal()==0) {jeu.ReinitFusionsPuisRemplir(); break;}
                                else {jeu.ActionSiPartieNonFinie(Direction.droite); break;}
                            case "2":
                                end = true;
                                if (jeu.getmaxVal()==0) {jeu.ReinitFusionsPuisRemplir(); break;}
                                else {jeu.ActionSiPartieNonFinie(Direction.bas); break;}

                        };
                    }


                }

            }
        }.start();


    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private static String multiple(int facteur, String s) {
        return new String(new char[facteur]).replace("\0", s);
    }

    private void afficher()  {


        System.out.printf("\033[H\033[J"); // permet d'effacer la console (ne fonctionne pas toujours depuis la console de l'IDE)
        System.out.println();

        if (!jeu.estGagne() && !jeu.estPerdu()) {
            String sep_lignes = multiple(jeu.getC(),"---------");
            for (int i = 0; i < jeu.getL(); i++) {
                System.out.println(sep_lignes);
                for (int j = 0; j < jeu.getC(); j++) {
                    Case c = jeu.getCase(i, j);
                    System.out.print("|");
                    if (c != null) {
                        System.out.format("%5.5s", c.getValeur());
                    } else {
                        System.out.format("%5.5s","");
                    };
                    System.out.print("  ");

                }
                System.out.print("  |");
                System.out.println();
            };
            System.out.println(sep_lignes);
        }
        else {
            if (jeu.estGagne()) {
                System.out.println("Vous avez gagné.\n");
            } else {
                System.out.println("Vous avez perdu.\n");
            };
            System.out.println("Tours: "+jeu.getTour());
            System.out.println("Score: "+jeu.getmaxVal());
            System.out.println("Fusions effectuées: "+jeu.getFusionsEffectuees());
        }


    }

    private void raffraichir() {
        synchronized (this) {
            try {
                notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        raffraichir();
    }
}
