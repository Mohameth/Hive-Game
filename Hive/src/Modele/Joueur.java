package Modele;

import Modele.Insectes.Insecte;
import java.util.*;

public abstract class Joueur {

	Collection<Insecte> pions;
	Plateau plateau;

	public abstract boolean coup(Point3DH p); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)
        

	public Joueur(Plateau p) {
		this.plateau = p;
                this.pions = new ArrayList<>(); //On rentrera tous les pions ici
        }

}