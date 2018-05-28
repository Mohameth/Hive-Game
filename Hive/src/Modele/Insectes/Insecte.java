package Modele.Insectes;

import Modele.Joueurs.Joueur;
import Modele.Case;
import Modele.Plateau;
import Modele.HexaPoint;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Insecte implements Cloneable, Serializable {

    private Joueur joueur;
    private Case emplacement;

    public abstract Collection<Case> deplacementPossible(Plateau plateau);

    public abstract Insecte clone();

    public abstract TypeInsecte getType();

    public boolean equalsType(Insecte i) {
        return getType() == i.getType();
    }
    
    public abstract boolean equals(Insecte insecte);

    public void deplacement(Plateau plat, HexaPoint cible) {
        try {
            //this.getEmplacement().removeInsecte();
            //plat.getCase(cible).addInsecte(this);
            plat.deleteInsecte(this, this.getEmplacement().getCoordonnees());
            plat.deplaceInsecte(this, cible);
        } catch (Exception e) {
            System.err.println("ERREUR DEPLACEMENT :" + e);
        }
    }

    public Insecte(Joueur j) {
        this.joueur = j;
        this.emplacement = null;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Case getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(Case emplacement) {
        this.emplacement = emplacement;
    }

    public int getNiveau() {
        return getEmplacement().getInsectes().indexOf(this) + 1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.emplacement);
        return hash;
    }

    @Override
    public String toString() {
        return "Insecte";
    }
}
