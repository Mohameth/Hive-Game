/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Insectes.Insecte;
import Modele.HexaPoint;
import java.io.Serializable;

/**
 *
 * @author moham
 */
public class Coup implements Serializable {
    private boolean modePlacement;
    private int numDansMain;
    private HexaPoint origine;
    private int niveauInsecte;
    private HexaPoint cible;

    public Coup(boolean modePlacement, int numDansMain, HexaPoint origine, int niveauInsecte, HexaPoint cible) {
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
    
    public HexaPoint getOrigine() {
        return origine;
    }
    
    public int getNiveauInsecte() {
        return niveauInsecte;
    }

    public HexaPoint getCible() {
        return cible;
    }

    @Override
    public String toString() {
        return "Coup{" + "modePlacement=" + modePlacement + ", origine=" + origine + ", niveauInsecte=" + niveauInsecte + ", cible=" + cible + '}';
    }
    
}
