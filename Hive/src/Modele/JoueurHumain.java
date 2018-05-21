
package Modele;

import Controleur.Hive;
import Modele.Insectes.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JoueurHumain extends Joueur {

    public JoueurHumain(Plateau p, boolean extensions) {
        super(p, extensions);
    }

    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        if (this.pions.contains(insecte) && insecte.deplacementPossible(plateau).contains(this.plateau.getCase(cible))) {
            insecte.deplacement(plateau, cible);
            this.tourJoueur++;
            return true;
        }
        return false;
    }
    


}