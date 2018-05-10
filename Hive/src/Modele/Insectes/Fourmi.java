package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Fourmi extends Insecte {

    public Fourmi(Joueur j) {
        super(j);
    }

    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        Collection<Case> result = new LinkedList<>();
        LinkedList<Case> toCheck = new LinkedList<>();
        LinkedList<Case> alreadyChecked = new LinkedList<>();
        
        toCheck.add(this.getEmplacement());
        while (!toCheck.isEmpty()) {
            Case courante = toCheck.removeLast();
            alreadyChecked.add(courante);
            
            for (Case c : plateau.getCasesVoisinesSansGates(courante, false)) {
                if (c.estVide()) {
                    if (!result.contains(c)) result.add(c);
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