package Modele;

import Modele.Insectes.Insecte;
import Modele.Insectes.Scarabee;
import java.awt.Point;
import java.util.ArrayList;

public class Case {

    private ArrayList<Insecte> insectes;
    private Point3DH coordonnees;

    public Case(Point3DH p, Insecte insecte) {
        this.insectes = new ArrayList<>();
        this.insectes.add(insecte);
        this.coordonnees = p;
    }
    
    public Case(Point3DH p) {
        this.coordonnees = p;
        this.insectes = new ArrayList<>();
    }

    public boolean estVide() {
        return this.insectes.isEmpty();
    }

    public void addInsecte(Insecte insecte) throws Exception {
        if (!this.estVide() && !(insecte instanceof Scarabee)) throw new Exception("Ajout impossible sur case non vide");
        if (this.insectes.size() == 5) throw new Exception("Ajout impossible -> 5 insectes maximum");
        this.insectes.add(insecte);
    }

    
    public void removeInsecte() throws Exception{
        if (this.estVide()) throw new Exception("Retrait impossible sur case vide");
        
        Insecte i = this.insectes.get(this.insectes.size()-1);
        this.insectes.remove(i);
    }
    
    public boolean insecteBloque(Insecte insecte) throws Exception {
        if (this.estVide()) throw new Exception("Case vide");
        
        
        if (!this.insectes.contains(insecte))
            throw new Exception("Cet insecte n'est pas sur cette case");
        
        if(this.insectes.contains(insecte) && !this.insectes.get(this.insectes.size()-1).equals(insecte))
            return true;
        
        return false;
    }

    public ArrayList<Insecte> getInsectes() {
        return insectes;
    }
    
    public Insecte getInsecteOnTop() {
        if (this.insectes.isEmpty()) return null;
        return this.insectes.get(this.insectes.size()-1);
    }
    

        
        
}