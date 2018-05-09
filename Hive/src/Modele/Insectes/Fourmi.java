package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Fourmi extends Insecte {

    public Fourmi(Joueur j) {
        super(j);
    }

    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
        Case debut = getEmplacement();
        Case courante = debut;
        
        /*do {
            P
        } while (!courante.equals(debut))*/
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}