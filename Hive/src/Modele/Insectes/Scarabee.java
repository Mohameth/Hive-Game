package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Scarabee extends Insecte {

    public Scarabee(Joueur j) {
        super(j);
    }
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        return plateau.getCasesVoisinesSansGates(this.getEmplacement(), false);
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}