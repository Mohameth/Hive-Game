package Modele;

import Modele.Insectes.Insecte;
import java.util.*;

public abstract class Joueur {

	Collection<Insecte> Pions;
	Plateau plateau;

	public abstract boolean coup();

	public Joueur() {
		// TODO - implement Joueur.Joueur
		throw new UnsupportedOperationException();
	}

}