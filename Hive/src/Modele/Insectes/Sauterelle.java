package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Sauterelle extends Insecte {

    public Sauterelle(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}