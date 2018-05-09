package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Reine extends Insecte {

    public Reine(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        return getVoisinsAccessible(plateau);
    }

    @Override
    public void Deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}