package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Cloporte extends Insecte {

    public Cloporte(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) { //Creer un HASh pour éviter de recalculer tout le temps la liste
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}