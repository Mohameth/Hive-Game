/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.Case;
import Modele.CoupleCaesInsecte;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.Noeud;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author firmyn
 */
public abstract class AbstractCoup implements Runnable{
    
    protected long tempsDebut;
    protected long tempsFin;
    protected Plateau plateau;
    protected JoueurIA joueur;
    protected Joueur adverse;


    public AbstractCoup(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.adverse = adverse;
        this.resetTemps();
    }
    
    
    
    protected abstract boolean coup();
    
    @Override
    public void run() {
        this.tempsDebut = System.nanoTime();
        this.coup();
    }
    
    protected void attente() {
        this.tempsFin = System.nanoTime();
        if ((this.tempsFin - this.tempsDebut) < 2000000000) {
            try {
                TimeUnit.NANOSECONDS.sleep(2000000000 - (this.tempsFin - this.tempsDebut));
                this.resetTemps();
            } catch (InterruptedException ex) {
                System.err.println("ERREUR attente IA : " + ex);
            }
        }
    }

    protected void resetTemps() {
        this.tempsDebut = 0;
        this.tempsFin = 0;
    }

    public void jouerReine() {
        Reine r = this.joueur.getReine();
        Random ra = new Random();
        ArrayList<Case> cases = plateau.pointVersCase(plateau.casesVidePlacement(this.joueur));
        HexaPoint c = cases.get(ra.nextInt(cases.size())).getCoordonnees();
                    
        this.joueur.setDernierDeplacement(new Deplacement(r, null, c));
        this.joueur.coupChoisi(r, c, true);
    }

    
    protected CoupleCaesInsecte getCouple(Noeud pere, Noeud fils, Insecte in, Case c, Case c2) {

        if (c2 == null) {
            return new CoupleCaesInsecte(getInsecte2(pere.getMainIA(), in), getCase2(pere.getPlateau(), c), null);
        }

        return new CoupleCaesInsecte(getCase2(pere.getPlateau(), c2).getInsecteOnTop(),
                getCase2(pere.getPlateau(), c), null);
        
    }
    
    protected Case getCase2(Plateau p1, Case c) {
        Map<HexaPoint, Case> cases = p1.getCases();
        for (Map.Entry<HexaPoint, Case> e : cases.entrySet()) {
            if (e.getValue().equals(c)) {
                return e.getValue();
            }
        }
        return null;
    }

    protected Insecte getInsecte2(ArrayList<Insecte> insecte, Insecte in) {
        for (int i = 0; i < insecte.size(); i++) {
            if (insecte.get(i).equals(in)) {
                return insecte.get(i);
            }
        }
        return null;
    }

    protected boolean coupGagnant() {
        if (!adverse.reinePresqueBloquee()) {
            return false;
        }
        Case c = ((ArrayList<Case>) plateau.getCasesVoisines(adverse.getReine().getEmplacement(), true)).get(0);
        ArrayList<Insecte> in = this.joueur.pionsEnJeu();
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).deplacementPossible(plateau).contains(c)) {
                if (!in.get(i).getEmplacement().estVoisin(c) || in.get(i).getEmplacement().getNbInsectes() > 1) {
                    this.joueur.setDernierDeplacement(new Deplacement(in.get(i), in.get(i).getEmplacement().getCoordonnees() , c.getCoordonnees()));
                    in.get(i).deplacement(plateau, c.getCoordonnees());
                    return true;
                }
            }
        }
        return false;
    }


}
