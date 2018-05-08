package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;

public class Case {

    private Insecte insecte;
    private Insecte insecteParDessus;
    private Point3DH coordonnees;

    public Case(Point3DH p, Insecte insecte) {
        this.insecte = insecte;
        this.insecteParDessus = null;
        this.coordonnees = p;
    }
    
    public Case(Point3DH p) {
        this.coordonnees = p;
        this.insecte = null;
        this.insecteParDessus = null;
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
    
    public boolean insecteBloque(Insecte insecte) throws Exception {
        if (this.estVide()) throw new Exception("Case vide");
        
        if (insecte == this.insecte && this.insecteParDessus != null)
            return true;
        
        if (this.insecte ==  insecte && this.insecteParDessus == null)
            return true;
        
        if (this.insecteParDessus == insecte)
            return true;
        
        if (insecte != this.insecte && insecte != this.insecteParDessus)
            throw new Exception("Cet insecte n'est pas sur cette case");
        
        return false;
    }
    
    
    /* GETTER & SETTER */
    public Insecte getInsecte() {
        if (this.insecteParDessus == null)
            return this.insecte;
        return this.insecteParDessus;
    }

    public Insecte getInsecteParDessus() {
        return insecteParDessus;
    }

        
        
}