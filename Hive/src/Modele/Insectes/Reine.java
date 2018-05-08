package Modele.Insectes;

import Modele.Joueur;
import Modele.Plateau;

public class Reine extends Insecte {

    public Reine(Joueur j) {
        super(j);
    }

        @Override
	public void Deplacement() {
		// TODO - implement Reine.Deplacement
		throw new UnsupportedOperationException();
	}

    @Override
    public void deplacementPossible(Plateau plateau) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}