package Modele;

import Controleur.Hive;
import Modele.Insectes.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Joueur {

    protected ArrayList<Insecte> pions;
    protected Plateau plateau;
    protected Insecte dernierDeplacement;

    public abstract boolean coup(Insecte insecte, Point3DH cible); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)


    public Joueur(Plateau p, boolean extensions) {
            this.plateau = p;
            this.dernierDeplacement = null;
            this.pions = new ArrayList<>(); //On rentrera tous les pions ici
            
            this.initInsectes(extensions);
    }

    public boolean reinePosee() {
        Insecte reine; int i = 0;
        do {
            reine = this.pions.get(i);
            i++;
        } while(i < this.pions.size() && (!(reine instanceof Reine)));
        if (reine.getEmplacement() == null)
            return false;
        
        return true;
    }
        
    public ArrayList<Insecte> pionsEnMain() {
        ArrayList<Insecte> res = new ArrayList<>();
        for (Insecte ins : this.pions) {
            if (ins.getEmplacement() == null) {
                res.add(ins);
            }
        }
        return res;
    }   

    
    public void placementInsecte(Insecte insecte, Case caseCible) {
        try {
            caseCible.addInsecte(insecte);
            this.dernierDeplacement = insecte;
        } catch (Exception ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Insecte> getPions() {
        return pions;
    }

    private void initInsectes(boolean extensions) {
        this.pions.add(new Reine(this));
        for (int i = 0; i < 2; i++) {
            this.pions.add(new Scarabee(this));
            this.pions.add(new Araignee(this));
        }
        for (int i = 0; i < 3; i++) {
            this.pions.add(new Fourmi(this));
            this.pions.add(new Sauterelle(this));
        }
        if (extensions) {
            this.pions.add(new Moustique(this));
            this.pions.add(new Cloporte(this));
            this.pions.add(new Coccinelle(this));
        }
        
    }

    
    
    
}

