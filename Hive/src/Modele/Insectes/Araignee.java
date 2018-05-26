package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Araignee extends Insecte {
	
    public Araignee(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Araignee(this.getJoueur());
    }
        
    //@Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (!this.getJoueur().reinePosee() || this.getEmplacement().getNbInsectes() != 1) return new ArrayList<>();

        Collection<Case> c1 = plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true);
        Collection<Case> c2 = new ArrayList<Case>(); 
        for (Case c : c1) {
            for(Case d : plateau.getCasesLibresAccessibles(c, this.getEmplacement())) {
                if (!c1.contains(d)) {
                    c2.add(d);
                }
            }
        }
        
        Collection c3 = new ArrayList<Case>();
        for (Case c : c2) {
            for(Case d : plateau.getCasesLibresAccessibles(c, this.getEmplacement())) {
                if (!c1.contains(d) && !c2.contains(d) && !plateau.rucheBrisee(this.getEmplacement(), d)) {
                    c3.add(d);
                }
            }
        }
        
        return c3;
    }

    @Override
    public TypeInsecte getType() {
       return TypeInsecte.ARAIGNEE;
    }

    @Override
    public String toString() {
        return "Araignee";
    }
}