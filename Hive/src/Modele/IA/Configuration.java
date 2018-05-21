/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

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
    private int evaluation;
    private Coup coupJoue;

    private Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire, Configuration parent, Coup coupJoue) {
        this.plateau = plateau;
        this.plateauOrigine = plateau;
        this.joueurCourant = joueurCourant;
        this.joueurCourantOrigine = joueurCourant;
        this.adversaire = adversaire;
        this.parent = parent;
        this.coupJoue = coupJoue;
    }

    public Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire) {
        this.plateau = plateau;
        this.joueurCourant = joueurCourant;
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
                    for (Point3DH p : plateau.casesVidePlacement(joueurCourant)) {
                        addFils(true, i, p);
                    }
                } else {
                    for (Case c : i.deplacementPossible(plateau)) {
                        addFils(false, i, c.getCoordonnees());
                    }
                }
            }
        }
        
        return fils;
    }
    
    private void copiePlateau(Plateau newPlateau, Joueur newCourant, Joueur newAdversaire) {
        for(Insecte insecte : plateau.getInsectes()) {
            Insecte cloneInsecte = null;
            Joueur j = null;
            if (insecte.getJoueur().equals(joueurCourant))  j = joueurCourant;
            else                                            j = adversaire;
            
            cloneInsecte = newCourant.getPions().get(j.getPions().indexOf(insecte));
            cloneInsecte.setJoueur(j);

            newPlateau.ajoutInsecte(cloneInsecte, insecte.getEmplacement().getCoordonnees());
        }
    }
            
    private void addFils(boolean modePlacement, Insecte i, Point3DH p) {
        Plateau newPlateau = (Plateau) plateau.clone();
        Joueur newCourant = joueurCourant.clone();
        Joueur newAdversaire = adversaire.clone();
        
        newCourant.setPlateau(newPlateau);
        newAdversaire.setPlateau(newPlateau);
        
        copiePlateau(newPlateau, newCourant, newAdversaire);
        
        Insecte newInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(i));
        Point3DH origine = null;
        int niveau = 1;
        if (modePlacement) newCourant.placementInsecte(newInsecte, p);
        else {
            if(newInsecte.getEmplacement() == null) newPlateau.ajoutInsecte(newInsecte, p);
            else {
                origine = newInsecte.getEmplacement().getCoordonnees();
                niveau = newInsecte.getNiveau();
                newPlateau.deplaceInsecte(newInsecte, p);
            }
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
