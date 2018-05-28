/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
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
        Coup coup = minimax();
        if (coup == null) {
            JoueurIA iaf = new JoueurIA(this.plateau,1,false,NumJoueur.JOUEUR2,this.adversaire);
            iaf.setPions(pions);
            return iaf.coup(insecte, cible);
        }
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
              
        this.tourJoueur++;
        return true;
    }
    
    public Coup minimax() {
        Configuration parent = new Configuration(plateau, this, adversaire);
        Configuration meilleurConf = null;
        int oldVal = Integer.MIN_VALUE;
        int newVal;
        
        ArrayList<Configuration> x = parent.getAllCoupsPossibles(false);
        System.out.println("Analyse de " + x.size() + " configurations");
        for (Configuration c : x) {
            newVal = calculJoueurCourant(c, horizon);
            if (newVal > oldVal) {
                meilleurConf = c;
                oldVal = newVal;
            }
        }
        if (meilleurConf != null) {
            System.out.println("Coup évalué à " + meilleurConf.getEvaluation());
            return meilleurConf.getCoupJoue();
        } else {
            System.out.println("aucune conf : Jeu aleatoire");
            return null;
        }
    }
    
    public int calculJoueurCourant(Configuration conf, int horizon) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        }
        
        int valeur = conf.getEvaluation();
        for (Configuration confCourante : conf.getAllCoupsPossibles(true)) {
            valeur = Integer.max(valeur, calculAdversaire(confCourante, horizon-1));
        }
        
        return valeur;
    }
    
    public int calculAdversaire(Configuration conf, int horizon) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        } 
        
        int valeur = Integer.MAX_VALUE;
        for (Configuration confCourante : conf.getAllCoupsPossibles(false)) {
            valeur = Integer.min(valeur, calculJoueurCourant(confCourante, horizon-1));
        }
        //conf.echangeJoueur();
        
        return valeur;
    }
    
}
