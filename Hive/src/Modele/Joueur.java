package Modele;

import Modele.Insectes.Insecte;
import java.util.*;
import javafx.geometry.Point3D;

public abstract class Joueur {

	Collection<Insecte> Pions;
	Plateau plateau;

	public abstract boolean coup(Point3D p); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)
        

	public Joueur() {
		// TODO - implement Joueur.Joueur
                throw new UnsupportedOperationException();
	}

}