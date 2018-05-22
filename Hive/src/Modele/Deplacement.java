/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Insectes.Insecte;

/**
 *
 * @author GRP3
 */
public class Deplacement {
    private Insecte i;
    private HexaPoint orig;
    private HexaPoint cible;

    public Deplacement(Insecte i, HexaPoint orig, HexaPoint cible) {
        this.i = i;
        this.orig = orig;
        this.cible = cible;
    }

    public Insecte getI() {
        return i;
    }

    public void setI(Insecte i) {
        this.i = i;
    }

    public HexaPoint getOrig() {
        return orig;
    }

    public void setOrig(HexaPoint orig) {
        this.orig = orig;
    }

    public HexaPoint getCible() {
        return cible;
    }

    public void setCible(HexaPoint cible) {
        this.cible = cible;
    }

}
