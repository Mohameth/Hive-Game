package Modele;

import Modele.Insectes.Insectes;
import java.util.*;

public abstract class Joueur {

	Collection<Insectes> Pions;
	Plateau plateau;

	public abstract boolean coup();

	public Joueur() {
		// TODO - implement Joueur.Joueur
		throw new UnsupportedOperationException();
	}

}