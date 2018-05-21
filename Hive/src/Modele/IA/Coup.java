/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Insectes.Insecte;
import Modele.Point3DH;

/**
 *
 * @author moham
 */
public class Coup {
    private boolean modePlacement;
    private int numDansMain;
    private Point3DH origine;
    private int niveauInsecte;
    private Point3DH cible;

    public Coup(boolean modePlacement, int numDansMain, Point3DH origine, int niveauInsecte, Point3DH cible) {
        this.modePlacement = modePlacement;
        this.origine = origine;
        this.niveauInsecte = niveauInsecte;
        this.cible = cible;
        this.numDansMain = numDansMain;
    }

    public boolean isModePlacement() {
        return modePlacement;
    }

    public int getNumDansMain() {
        return numDansMain;
    }
    
    public Point3DH getOrigine() {
        return origine;
    }
    
    public int getNiveauInsecte() {
        return niveauInsecte;
    }

    public Point3DH getCible() {
        return cible;
    }

    @Override
    public String toString() {
        return "Coup{" + "modePlacement=" + modePlacement + ", origine=" + origine + ", niveauInsecte=" + niveauInsecte + ", cible=" + cible + '}';
    }
    
}
