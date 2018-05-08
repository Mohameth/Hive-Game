package Modele.Insectes;

import Modele.Joueur;
import Modele.Case;
import Modele.Plateau;

public abstract class Insecte {

	private Joueur joueur;
	private Case emplacement;
        
        public abstract void deplacementPossible(Plateau plateau);
        
	public abstract void Deplacement();

	public Insecte(Joueur j) {
            this.joueur = j;
            this.emplacement = null;
	}

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Case getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(Case emplacement) {
        this.emplacement = emplacement;
    }
        
        

}