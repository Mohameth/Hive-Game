/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Joueurs.Joueur;
import Modele.*;
import Modele.Insectes.Insecte;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author moham
 */
public class Configuration {
    Plateau plateau;
    Plateau plateauOrigine;
    Joueur joueurCourant;
    Joueur joueurCourantOrigine;
    Joueur adversaire;
    Configuration parent;
    private ArrayList<Configuration> fils = null;
    boolean dejaEvalue = false;
    private Integer evaluation = null;
    private Coup coupJoue;

    private Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire, Configuration parent, Coup coupJoue) {
        this.plateau = plateau;
        this.joueurCourant = joueurCourant;
        this.adversaire = adversaire;
        this.parent = parent;
        this.coupJoue = coupJoue;
    }

    public Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire) {
        this.plateau = plateau;
        this.plateauOrigine = plateau;
        this.joueurCourant = joueurCourant;
        this.joueurCourantOrigine = joueurCourant;
        this.adversaire = adversaire;
    }
    
    public int getEvaluation() {
        if (!dejaEvalue) {        
            evaluation = new IAEvaluation(plateau, joueurCourant, adversaire).getEvaluation();
            dejaEvalue = true;
        }
        
        return evaluation;
    }
    
    public boolean estFeuille() {
        //return plateau.estGagnant() || plateau.estPerdant();
        return false;
    }
    
    public ArrayList<Configuration> getAllCoupsPossibles() {
        if (fils == null) {
            fils = new ArrayList<>();

            for (Insecte i : joueurCourant.getPions()) {
                if (i.getEmplacement() == null) {
                    for (HexaPoint p : plateau.casesVidePlacement(joueurCourant)) {
                        addFils(true, i, p);
                    }
                } else {
                    for (Case c : i.deplacementPossible(plateau)) {
                        addFils(false, i, c.getCoordonnees());
                    }
                }
            }
        }
        if (fils.size() == 0) {
            System.err.println("ERRRRRRRRRRRRRRRRRRRREEEUUUUUUUUUUUURRR");
        }
        
        return fils;
    }
    
    public void echangeJoueur() {
        Joueur j = joueurCourant;
        this.joueurCourant = adversaire;
        this.adversaire = j;
    }
    
    private void copiePlateau(Plateau newPlateau, Joueur newCourant, Joueur newAdversaire) {
        for(Insecte insecte : plateau.getInsectes()) {
            Insecte cloneInsecte = null;
            if (insecte.getJoueur().getNumJoueur() == joueurCourant.getNumJoueur())  {
                cloneInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(joueurCourant);
            }
            else {
                cloneInsecte = newAdversaire.getPions().get(adversaire.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(adversaire);
            }

            newPlateau.ajoutInsecte(cloneInsecte, insecte.getEmplacement().getCoordonnees());
        }
    }
    
    private void addFils(boolean modePlacement, Insecte i, HexaPoint p) {
        Plateau newPlateau = (Plateau) plateau.clone();
        Joueur newCourant = joueurCourant.clone();
        Joueur newAdversaire = adversaire.clone();
        
        newCourant.setPlateau(newPlateau);
        newAdversaire.setPlateau(newPlateau);
        
        copiePlateau(newPlateau, newCourant, newAdversaire);
        
        Insecte newInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(i));
        HexaPoint origine = null;
        int niveau = 1;
        if (modePlacement) newCourant.placementInsecte(newInsecte, p);
        else {
            origine = newInsecte.getEmplacement().getCoordonnees();
            niveau = newInsecte.getNiveau();
            newPlateau.deplaceInsecte(newInsecte, p);
        }
        
        if (origine == null) origine = newInsecte.getEmplacement().getCoordonnees();
        
        fils.add(new Configuration(
                newPlateau, 
                newCourant,
                newAdversaire, 
                this, 
                new Coup(modePlacement, joueurCourant.getPions().indexOf(i), origine, niveau, p)
        ));
    }

    public Coup getCoupJoue() {
        return coupJoue;
    }
}
