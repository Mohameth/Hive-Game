package Modele.Joueurs;

import Modele.Case;
import Modele.CoupleCaesInsecte;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.ThreadCoups.*;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoueurIA extends Joueur {

    private int difficulte;
    private Joueur adverse;
    private AbstractCoup threadCoup;
    private Insecte insecteChoisi;
    private HexaPoint caseChoisie;
    private boolean placement;

    public JoueurIA(Plateau p, int difficulte, boolean extensions, NumJoueur numJoueur, Joueur adverse) {
        super(p, extensions, numJoueur);
        this.difficulte = difficulte;
        this.adverse = adverse;
        this.setThreadCoup();
    }

    public JoueurIA(Plateau p, int difficulte, NumJoueur numJoueur, boolean extensions) {
        super(p, extensions, numJoueur);
        this.difficulte = difficulte;
        this.adverse = null;
    }

    public void addJoueurAdverse(Joueur j) {
        this.adverse = j;
    }

    public Joueur getAdverse() {
        return adverse;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int dif){
        this.difficulte = dif;
    }

    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        try {
            Thread t = new Thread(this.threadCoup);
            t.start();
            
            t.join();
        } catch (InterruptedException ex) {
            System.err.print("Erreur Thread IA : " + ex);
        }
        
        if(this.coupChoisiExistant()) {
            if(this.placement) {
                this.plateau.ajoutInsecte(this.insecteChoisi, this.caseChoisie);
            } else {
                this.plateau.deplaceInsecte(this.insecteChoisi, this.caseChoisie);
            }
            this.resetCoupChoisi();
            this.incrementeTour();
            return true;
        }
        
        return false;
    }

    public void coupChoisi(Insecte insecte, HexaPoint caseChoisie, boolean placement) {
        if (insecte != null) {
          this.insecteChoisi = insecte;
          this.caseChoisie = caseChoisie;
          this.placement = placement;
        } 
    }
    
    private void resetCoupChoisi() {
        this.insecteChoisi = null;
        this.caseChoisie = null;
        this.placement = false;
    }
    
    private boolean coupChoisiExistant() {
        return (this.insecteChoisi != null && this.caseChoisie != null);
    }
    
    private void setThreadCoup() {
        switch(this.difficulte) {
            case 1:
                this.threadCoup = new CoupFacile(this.plateau, this, this.adverse);
            break;
            case 2:
                this.threadCoup = new CoupMoyen(this.plateau, this, this.adverse);
            break;
            case 3:
                this.threadCoup = new CoupDifficile(this.plateau, this, this.adverse);
            break;
        }
    }

}
