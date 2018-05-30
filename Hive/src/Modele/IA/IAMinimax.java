/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Deplacement;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.Insectes.Reine;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.NumJoueur;
import java.util.ArrayList;

/**
 *
 * @author moham
 */
public class IAMinimax extends Joueur {
    Joueur adversaire;
    int horizon = 2;
    public Coup lastCoup = null;
    
    public IAMinimax(Plateau p, boolean extensions, NumJoueur numJoueur, Joueur adversaire) {
        super(p, extensions, numJoueur);
        this.adversaire = adversaire;
    }

    public void setAdversaire(Joueur adversaire) {
        this.adversaire = adversaire;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }
    
    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        if (coupGagnant()) return true;
        
        Coup coup = minimax();
        if (coup == null) {
            /*JoueurIA iaf = new JoueurIA(this.plateau,1,false,NumJoueur.JOUEUR2,this.adversaire);
            iaf.setPions(pions);
            return iaf.coup(insecte, cible);*/
            return false;
        }
        Insecte i;
        
        if (coup.isModePlacement()) {
            i = this.getPions().get(coup.getNumDansMain());
            //this.placementInsecte(i, coup.getCible());
            i.setEmplacement(this.plateau.getCase(coup.getCible()));
            this.plateau.ajoutInsecte(i, coup.getCible());
            
            this.incrementeTour();
            return true;
        }
        
        i = plateau.getCase(coup.getOrigine()).getInsectes().get(coup.getNiveauInsecte()-1);
        
        this.plateau.deleteInsecte(i, i.getEmplacement().getCoordonnees());
        this.plateau.deplaceInsecte(i, coup.getCible());
        //System.out.println(i.getClass().getCanonicalName() + " en " + coup.getCible());
        lastCoup = coup;
              
        this.incrementeTour();
        return true;
    }
    
    public Coup minimax() {
        Configuration parent = new Configuration(plateau, this, this.adversaire);
        Configuration meilleurConf = null;
        int oldVal = Integer.MIN_VALUE;
        int newVal;
        
        ArrayList<Configuration> x = parent.getAllCoupsPossibles(false);
        //System.out.println("Analyse de mes " + x.size() + " coups");
        //int evalParent = parent.getEvaluation();
        //System.out.println("\nPosition courante évalué à " + evalParent);
        for (Configuration c : x) {
            newVal = calculJoueurCourant(c, horizon, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (newVal > oldVal) {
                meilleurConf = c;
                oldVal = newVal;
            }
        }
        if (meilleurConf != null) {
            //System.out.println("Coup évalué à " + meilleurConf.getEvaluation() + "\n");
            return meilleurConf.getCoupJoue();
        } else {
            if (x.size() == 0) {
                System.out.println("aucune conf : Jeu aleatoire");
                return null;
            }
            return x.get(0).getCoupJoue();
        }
    }
    
    public int calculJoueurCourant(Configuration conf, int horizon, int alpha, int beta) {
        if (horizon == 0 || conf.estFeuille()) {
            int eval = conf.getEvaluation();
            conf = null;
            return eval;
        }
        
        int valeur = Integer.MAX_VALUE;
        //System.out.println(" --> Analyse de " + conf.getAllCoupsPossibles(true).size() + "coups adverses");
        for (Configuration confCourante : conf.getAllCoupsPossibles(true)) {
            valeur = Integer.min(valeur, calculAdversaire(confCourante, horizon-1, alpha, beta));
            if (alpha > valeur) return valeur;
            beta = Integer.min(beta, valeur);
        }
        
        return valeur;
    }
    
    public int calculAdversaire(Configuration conf, int horizon, int alpha, int  beta) {
        if (horizon == 0 || conf.estFeuille()) {
            int eval = conf.getEvaluation();
            conf = null;
            return eval;
        }
        
        int valeur = Integer.MIN_VALUE;
        //System.out.println(" --> Analyse de " + conf.getAllCoupsPossibles(false).size() + "coups du turfu");
        for (Configuration confCourante : conf.getAllCoupsPossibles(false)) {
            valeur = Integer.max(valeur, calculJoueurCourant(confCourante, horizon-1, alpha, beta));
            if (valeur > beta) return valeur;
            alpha = Integer.max(alpha, valeur);
        }
        
        return valeur;
    }
    
    protected boolean coupGagnant() {
        if (!adversaire.reinePresqueBloquee()) {
            return false;
        }
        Reine reine=adversaire.getReine(adversaire.pionsEnJeu());
        if(reine==null) {
        	return false;
        }
        ArrayList<Case> c1=(ArrayList<Case>) plateau.getCasesVoisines(reine.getEmplacement(), true);
        Case c = c1.get(0);
        ArrayList<Insecte> in = this.pionsEnJeu();
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).deplacementPossible(plateau).contains(c)) {
                if (!in.get(i).getEmplacement().estVoisin(c) || in.get(i).getEmplacement().getNbInsectes() > 1) {
                    this.setDernierDeplacement(new Deplacement(in.get(i), in.get(i).getEmplacement().getCoordonnees(), c.getCoordonnees()));
                    in.get(i).deplacement(plateau, c.getCoordonnees());
                    this.incrementeTour();
                    return true;
                }
            }
        }
        
        this.incrementeTour();
        return false;
    }
}
