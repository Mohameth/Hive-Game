package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Araignee extends Insecte {

    public Araignee(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (plateau.rucheBrisee2(this.getEmplacement()))
            return null;
        ArrayList<Case> res = new ArrayList<>();
        res.addAll(deplacementPossibleworker(plateau, this.getEmplacement(), 1, new ArrayList<>()));
        return res;
    }
        
        
    private HashSet<Case> deplacementPossibleworker(Plateau p, Case origine, int dist, ArrayList<Case> visitees) {
        HashSet<Case> res = new HashSet<>();
        ArrayList<Case> voisins = (ArrayList<Case>) p.getCasesVoisines(origine, true);
        for (Case c : voisins) {
            if (!p.getCasesVoisinesOccupees(c).isEmpty() && !p.gateBetween(this.getEmplacement(), c)) {
                if (dist < 3){
                    visitees.add(c);
                    res.addAll(deplacementPossibleworker(p, c, dist++, visitees));
                } else {
                    if (!visitees.contains(c))
                        res.add(c);
                }
            } else {
                
            }
        }
        return res;
    }
    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        super.deplacement(plat, cible);
    }*/

}