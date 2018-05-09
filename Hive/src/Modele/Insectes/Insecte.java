package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public abstract class Insecte {

    private Joueur joueur;
    private Case emplacement;

    public abstract Collection<Case> deplacementPossible(Plateau plateau);

    public abstract void Deplacement(Plateau plat, Point3DH cible);

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
        
    
    protected Collection<Case> getVoisinsAccessible(Plateau plateau) {
        Collection<Case> dep = plateau.getCasesVoisines(getEmplacement(), true);
        for (Case c : plateau.getCasesVoisines(getEmplacement(), true)) {
            if (plateau.gateBetween(getEmplacement(), c)) {
                dep.remove(c);
            } 
        }

        return dep;
    }

}