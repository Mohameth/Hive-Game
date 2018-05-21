package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Araignee extends Insecte {
	
    public Araignee(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Araignee(this.getJoueur());
    }
        
    //@Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (!this.getJoueur().tousPionsPosables()) return new ArrayList<>();
        ArrayList<Case> res = new ArrayList<>();
        ArrayList<Case> visitees = new ArrayList<>();
        visitees.addAll(plateau.getCasesVoisines(this.getEmplacement(), true));
        deplacementPossibleWorker(plateau, plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true), 1, res, visitees);
        return res;
    }
        
        
    private void deplacementPossibleWorker(Plateau p, Collection<Case> departs, int dist, ArrayList<Case> res, ArrayList<Case> visitees) {
        if (dist > 4) return;
        visitees.addAll(departs);
        for (Case c : departs) {
            if (dist == 3) res.addAll(departs);
            else if (dist < 3) {
                Collection<Case> aVisite = p.getCasesVoisinesAccessibles(c, true);
                aVisite.removeAll(visitees);
                deplacementPossibleWorker(p, aVisite, dist+1, res, visitees);
            }
        }
    }

    @Override
    public TypeInsecte getType() {
       return TypeInsecte.ARAIGNEE;
    }

    @Override
    public String toString() {
        return "Araignee  ";
    }
}