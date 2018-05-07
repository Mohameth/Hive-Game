package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.*;

public class Case {

    Insecte insecte;
    Insecte insecteParDessus;

    public Case(Insecte insecte) {
        this.insecte = insecte;
    }

    public boolean estVide() {
        return insecte == null;
    }

    public void addInsecte(Insecte insecte) throws Exception {
        if (!this.estVide()) throw new Exception("Ajout impossible sur case non vide");

        this.insecte = insecte;
    }

    public void addInsecteOnTop(Insecte insecte) throws Exception {
        if (this.estVide() || insecteParDessus != null) throw new Exception("Ajout par dessus impossible");

        this.insecteParDessus = insecte;
    }
    
    public void deleteInsecte() {
        this.insecte = null;
    }
    
    public void deleteInsecteOnTop() {
        this.insecteParDessus = null;
    }
    
    
    /* GETTER & SETTER */
    public Insecte getInsecte() {
        return insecte;
    }

    public void setInsecte(Insecte insecte) {
        this.insecte = insecte;
    }

    public Insecte getInsecteParDessus() {
        return insecteParDessus;
    }

    public void setInsecteParDessus(Insecte insecteParDessus) {
        this.insecteParDessus = insecteParDessus;
    }
        
        
}