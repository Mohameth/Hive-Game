
package Modele.Joueurs;

import Modele.Joueurs.Joueur;
import Controleur.Hive;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Insectes.*;
import Modele.Plateau;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JoueurHumain extends Joueur {

    public JoueurHumain(Plateau p, boolean extensions, NumJoueur numJoueur) {
        super(p, extensions, numJoueur);
    }

    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        if (this.pions.contains(insecte) && insecte.deplacementPossible(plateau).contains(this.plateau.getCase(cible))) {
            this.dernierDeplacement = new Deplacement(insecte,insecte.getEmplacement().getCoordonnees(),cible);
            insecte.deplacement(plateau, cible);
            this.incrementeTour();
            return true;
        }
        return false;
    }
    


}