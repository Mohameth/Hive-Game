/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Insectes.Insecte;
import Modele.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import java.util.ArrayList;

/**
 *
 * @author moham
 */
public class IAMinimax extends Joueur {
    Joueur adversaire;
    int horizon = 0;
    public Coup lastCoup = null;
    
    public IAMinimax(Plateau p, boolean extensions) {
        super(p, extensions);
    }

    public void setAdversaire(Joueur adversaire) {
        this.adversaire = adversaire;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }
    
    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        Coup coup = minimax();
        Insecte i;
        
        if (coup.isModePlacement()) {
            i = this.getPions().get(coup.getNumDansMain());
            this.placementInsecte(i, coup.getCible());
            System.out.println(i.getClass().getCanonicalName() + " en " + coup.getCible() + " (placement)");
            return true;
        }
        
        i = plateau.getCase(coup.getOrigine()).getInsectes().get(coup.getNiveauInsecte()-1);
        i.deplacement(plateau, coup.getCible()); 
        System.out.println(i.getClass().getCanonicalName() + " en " + coup.getCible());
        lastCoup = coup;
        
       
        return true;
    }
    
    public Coup minimax() {
        Configuration parent = new Configuration(plateau, this, adversaire);
        Configuration meilleurConf = null;
        int oldVal = Integer.MIN_VALUE;
        int newVal;
        
        ArrayList<Configuration> x = parent.getAllCoupsPossibles();
        System.out.println("Analyse de " + x.size() + " configurations");
        for (Configuration c : x) {
            newVal = calculJoueurCourant(c, horizon);
            if (newVal > oldVal) {
                meilleurConf = c;
                oldVal = newVal;
            }
        }
        System.out.println("Coup évalué à " + meilleurConf.getEvaluation());
        
        return meilleurConf.getCoupJoue();
    }
    
    public int calculJoueurCourant(Configuration conf, int horizon) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        }
        
        int valeur = Integer.MIN_VALUE;
        //conf.echangeJoueur();
        for (Configuration confCourante : conf.getAllCoupsPossibles()) {
            valeur = Integer.max(conf.getEvaluation(), calculAdversaire(confCourante, horizon-1));
        }
        
        return valeur;
    }
    
    public int calculAdversaire(Configuration conf, int horizon) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        } 
        
        int valeur = Integer.MAX_VALUE;
        //conf.echangeJoueur();
        for (Configuration confCourante : conf.getAllCoupsPossibles()) {
            valeur = Integer.min(valeur, calculJoueurCourant(confCourante, horizon-1));
        }
        
        return valeur;
    }
    
}
