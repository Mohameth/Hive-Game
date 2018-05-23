package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.TypeInsecte;

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
        if (!this.getJoueur().tousPionsPosables()) return new ArrayList<>();
        ArrayList<Case> res = new ArrayList<>();
        ArrayList<Case> visitees = new ArrayList<>();
        Collection<Case> init = plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true);
        Case caseInit = this.getEmplacement();
        
        visitees.add(this.getEmplacement());
        for (Case c : init) {
            this.deplacement(plateau, c.getCoordonnees());
            deplacementWorker(plateau, c, visitees, res, 1);
            this.deplacement(plateau, caseInit.getCoordonnees());
        }
        
        Collection<Case> voisins = plateau.getCasesVoisines(this.getEmplacement(), true);
        Iterator<Case> it = res.iterator();
        while (it.hasNext()) {
            Case c = it.next();
            if (voisins.contains(c) || plateau.rucheBrisee(this.getEmplacement(), c)) it.remove();
        }
        
        return res;
    }
        
    private void deplacementWorker(Plateau p, Case courante, Collection<Case> visitees, Collection<Case> res, int dist) {
        if (dist > 4) return;
        if (dist == 3) res.add(courante);
        
        for (Case c : p.getCasesVoisinesAccessibles(courante, true)) {
            if (!visitees.contains(c)) {
                deplacementWorker(p, c, visitees, res, dist+1);
            }
        }
        visitees.add(courante);
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