package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;

public abstract class Insectes {

	Joueur Joueur;
	Case Case;

	public abstract void Deplacement();

	public Insectes() {
		// TODO - implement Insectes.Insectes
		throw new UnsupportedOperationException();
	}

}