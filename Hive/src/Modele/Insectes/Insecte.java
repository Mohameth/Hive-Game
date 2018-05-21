package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Insecte implements Cloneable {

    private Joueur joueur;
    private Case emplacement;

    public abstract Collection<Case> deplacementPossible(Plateau plateau);
    
    public abstract TypeInsecte getType();

    public void deplacement(Plateau plat, Point3DH cible) {
        try {
            //this.getEmplacement().removeInsecte();
            //plat.getCase(cible).addInsecte(this);
            plat.deleteInsecte(this, this.getEmplacement().getCoordonnees());
            plat.deplaceInsecte(this, cible);
            joueur.incrementeTour();
        } catch(Exception e) {
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
        return getEmplacement().getInsectes().indexOf(this)+1;
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
    
    public Insecte clone() {
        try {
            return (Insecte) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println("ERREUR Clonage insecte");
        }
        
        return this;
    }
}