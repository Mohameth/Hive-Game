package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;
import java.util.Objects;

public abstract class Insecte {

    private Joueur joueur;
    private Case emplacement;

    public abstract Collection<Case> deplacementPossible(Plateau plateau);

    public void deplacement(Plateau plat, Point3DH cible) {
        try {
            //this.getEmplacement().removeInsecte();
            //plat.getCase(cible).addInsecte(this);
            plat.deleteInsecte(this, this.getEmplacement().getCoordonnees());
            plat.deplaceInsecte(this, cible);
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.emplacement);
        return hash;
    }

}