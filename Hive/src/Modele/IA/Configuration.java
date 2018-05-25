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
    private boolean tourAdversaire = false;

    private Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire, boolean tourAdversaire, Configuration parent, Coup coupJoue) {
        this.plateau = plateau;
        this.joueurCourant = joueurCourant;
        this.adversaire = adversaire;
        this.tourAdversaire = tourAdversaire;
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
        return joueurCourant.reineBloquee() || adversaire.reineBloquee();// || (dejaEvalue && fils.size() == 0);
    }
    
    public ArrayList<Configuration> getAllCoupsPossibles(boolean tourAdversaire) {
        this.tourAdversaire = tourAdversaire;
        if (fils == null) {
            fils = new ArrayList<>();
            Joueur j = tourAdversaire ? adversaire : joueurCourant;
            
            for (Insecte i : j.getPions()) {
                if (i.getEmplacement() == null) {
                    for (HexaPoint p : plateau.casesVidePlacement(j)) {
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
            //System.err.println("ERRRRRRRRRRRRRRRRRRRREEEUUUUUUUUUUUURRR");
        }
        
        return fils;
    }
    
    /*public ArrayList<Configuration> getAllCoupsPossiblesAdversaire() {
        this.echangeJoueur();
        getAllCoupsPossibles();
        this.echangeJoueur();
        
        return fils;
    }*/
    
    public void echangeJoueur() {
        Joueur j = joueurCourant;
        this.joueurCourant = adversaire;
        this.adversaire = j;
    }
    
    private void copiePlateau(Plateau newPlateau, Joueur newCourant, Joueur newAdversaire) {
        if (newPlateau.getInsectes().size() != 0) {
            System.err.println("no");
        }
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
        
        Joueur j = tourAdversaire ? adversaire : joueurCourant;
        Joueur newJ = tourAdversaire ? newAdversaire : newCourant;
        
        Insecte newInsecte = newJ.getPions().get(j.getPions().indexOf(i));
        HexaPoint origine = null;
        int niveau = 1;
        if (modePlacement) newJ.placementInsecte(newInsecte, p);
        
        /*if (plateau.getInsectes().size() == 3 && modePlacement (i.getType() == TypeInsecte.REINE)) {
            System.err.println("");
        }*/
        
        else {
            origine = newInsecte.getEmplacement().getCoordonnees();
            niveau = newInsecte.getNiveau();
            newPlateau.deplaceInsecte(newInsecte, p);
        }
        
        if (origine == null) origine = newInsecte.getEmplacement().getCoordonnees();
        
        /*if (!newInsecte.deplacementPossible(newPlateau).isEmpty()) {
            System.err.println("-------------> " + newInsecte.getType() + " " + newInsecte.deplacementPossible(newPlateau).size());
        }*/
        
        fils.add(new Configuration(
                newPlateau, 
                newCourant,
                newAdversaire,
                tourAdversaire,
                this, 
                new Coup(modePlacement, j.getPions().indexOf(i), origine, niveau, p)
        ));
    }

    public Coup getCoupJoue() {
        return coupJoue;
    }
}
