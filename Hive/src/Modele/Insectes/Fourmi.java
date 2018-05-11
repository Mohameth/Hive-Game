package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Fourmi extends Insecte {

    public Fourmi(Joueur j) {
        super(j);
    }
    
    private boolean estConnecte(Plateau p, Collection<Case> resultat, Case c) {
        for (Case r : resultat) {
            for (Case resVoisin : p.getCasesVoisines(r, true)) {
                if (p.getCasesVoisines(c, true).contains(resVoisin)) return true;
            }
        }
        
        return false;
    }
    
    private boolean hasGate(Plateau p, Case c) {
        for (Case d : p.getCasesVoisines(c, true)) {
            if (!p.gateBetween(c, d)) return false;
        }
        
        return true;
    }
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        LinkedList<Case> result = new LinkedList<>();
        LinkedList<Case> toCheck = new LinkedList<>();
        LinkedList<Case> alreadyChecked = new LinkedList<>();
        
        toCheck.add(this.getEmplacement());
        while (!toCheck.isEmpty()) {
            Case courante = toCheck.removeLast();
            alreadyChecked.add(courante);
            
            for (Case c : plateau.getCasesVoisines(courante, false)) {
                if (c.estVide()) {
                    if (!result.contains(c)) {
                        if (!hasGate(plateau, c)) result.add(c);
                    }
                }
                else {
                    if (!alreadyChecked.contains(c)) {
                        toCheck.add(c);
                    }
                }
            }
        }
        
        return result;
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    /*@Override protected Collection<Case> getVoisinsAccessible(Plateau plateau) {
        Collection<Case> dep = super.getVoisinsAccessible(plateau);
        
        

        return dep;
    }*/

}