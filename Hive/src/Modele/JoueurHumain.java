
package Modele;

import Controleur.Hive;
import Modele.Insectes.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JoueurHumain extends Joueur {


    public JoueurHumain(Plateau p) {
            super(p);
    }

    @Override
    public boolean coup(Insecte insecte, Point3DH cible) {
        if (this.pions.contains(insecte) && insecte.deplacementPossible(plateau).contains(this.plateau.getCase(cible))) {
            insecte.deplacement(plateau, cible);
            return true;
        }
        return false;
    }
    


}