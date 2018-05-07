package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;
import Modele.Plateau;

public abstract class Insecte {

	Joueur Joueur;
	Case Case;
        
        public abstract void deplacementPossible(Plateau plateau);
        
	public abstract void Deplacement();

	public Insecte() {
		// TODO - implement Insectes.Insectes
		throw new UnsupportedOperationException();
	}

}