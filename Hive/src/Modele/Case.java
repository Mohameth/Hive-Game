package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import javafx.geometry.Point3D;

public class Case {

    Insecte insecte;
    Insecte insecteParDessus;
    Point3D coordonnees;

    public Case(Point3D p, Insecte insecte) {
        this.insecte = insecte;
        this.insecteParDessus = null;
        this.coordonnees = p;
    }
    
    public Case(Point3D p) {
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