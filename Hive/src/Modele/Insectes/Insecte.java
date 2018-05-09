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

    public void deplacement(Plateau plat, Point3DH cible) {
        try {
            this.getEmplacement().removeInsecte();
            plat.getCase(cible).addInsecte(this);
        } catch(Exception e) {
            System.err.println("ERREUR DEPLACEMENT :" + e);
        }
    }
    
    public void deplacement(Plateau plat, Case cible) {
        try {
            this.getEmplacement().removeInsecte();
            cible.addInsecte(this);
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
        try {
        if (this.emplacement.getInsecteOnTop() != this) emplacement.addInsecte(this);
        } catch (Exception e) {
            System.err.println("ERREUR SET EMPLACEMENT : " + e);
        } 
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