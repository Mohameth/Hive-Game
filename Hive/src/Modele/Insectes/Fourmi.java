package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Fourmi extends Insecte {

    public Fourmi(Joueur j) {
        super(j);
    }
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        LinkedList<Case> result = new LinkedList<>();
        LinkedList<Case> toCheck = new LinkedList<>();
        
        result.addAll(plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true));
        toCheck.addAll(result);
        while (!toCheck.isEmpty()) {
            Case courante = toCheck.removeLast();
            for (Case c : plateau.getCasesVoisinesAccessibles(courante, true)) {
                if (!result.contains(c) && plateau.glissementPossible(courante, c)) {
                    result.add(c);
                    toCheck.add(c);
                }
            }
        }
        
        return result;
    }
    
    /*@Override protected Collection<Case> getVoisinsAccessible(Plateau plateau) {
        Collection<Case> dep = super.getVoisinsAccessible(plateau);
        
        

        return dep;
    }*/

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.FOURMI;
    }

}