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
                if (joueurCourant.pionsEnMain().contains(i)) {
                    for (Point3DH p : plateau.casesVidePlacement(joueurCourant)) {
                        addFils(true, i, p);
                    }
                } else {
                    for (Case c : i.deplacementPossible(plateau)) {
                        if (c.getCoordonnees().equals(new Point3DH(-1, 2, -1))) 
                            System.out.println("DACODAC");
                        addFils(false, i, c.getCoordonnees());
                    }
                }
            }
        }
        
        return fils;
    }
    
    private void addFils(boolean modePlacement, Insecte i, Point3DH p) {
        Plateau newPlateau = (Plateau) plateau.clone();
        Joueur newCourant = joueurCourant.clone();
        Joueur newAdversaire = adversaire.clone();
        
        newCourant.setPlateau(newPlateau);
        newAdversaire.setPlateau(newPlateau);
        
        for(Pair<Insecte, Integer> val : plateau.getInsectes()) {
            Insecte insecte = val.getKey();
            int niveau = val.getValue();
            Insecte cloneInsecte = null;
            
            if (insecte.getJoueur().equals(joueurCourant)) {
                cloneInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(joueurCourant);
            } else {
                cloneInsecte = newAdversaire.getPions().get(adversaire.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(adversaire);
            }
            
            newPlateau.ajoutInsecte(cloneInsecte, insecte.getEmplacement().getCoordonnees());
        }
        Insecte newInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(i));
        if (modePlacement) newCourant.placementInsecte(newInsecte, p);
        else {
            newPlateau.ajoutInsecte(newInsecte, p);
        }
        fils.add(new Configuration(
                newPlateau, 
                newCourant,
                newAdversaire, 
                this, 
                new Coup(modePlacement, joueurCourant.getPions().indexOf(i), newInsecte.getEmplacement().getCoordonnees(), newInsecte.getNiveau(), p)
        ));
    }

    public Coup getCoupJoue() {
        return coupJoue;
    }
}
