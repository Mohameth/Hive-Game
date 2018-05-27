package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;

import java.util.ArrayList;
import java.util.Collection;

public class Reine extends Insecte {

    public Reine(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Reine(this.getJoueur());
    	
    }
	
	public boolean equals(Insecte insecte) {
		return (insecte.getType() == TypeInsecte.REINE);
	}
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
    	if(!this.getJoueur().reinePosee() || this.getEmplacement().getNbInsectes() != 1) {
        	return new ArrayList<>();
        }
        return plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true);
    }

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.REINE;
    }

    @Override
    public String toString() {
        return "Reine";
    }
}