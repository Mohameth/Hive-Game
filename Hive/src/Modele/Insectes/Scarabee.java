package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.TypeInsecte;
import java.util.ArrayList;
import java.util.Collection;

public class Scarabee extends Insecte {

    public Scarabee(Joueur j) {
        super(j);
    }
	
	public Insecte clone() {
    	return new Scarabee(this.getJoueur());
    }
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
        if (!this.getJoueur().tousPionsPosables()) return new ArrayList<>();
        
        ArrayList<Case> res = new ArrayList<>();
        for (Case c : plateau.getCasesVoisines(this.getEmplacement(), false)) {
            if (plateau.glissementPossible(this.getEmplacement(), c) || (this.getNiveau() == 1 && !c.estVide())) {
                res.add(c);
            } else if (this.getNiveau() > 1 && !plateau.rucheBrisee(this.getEmplacement(), c) && 
                        (c.estVide() || this.getNiveau() > c.getNbInsectes())
                    ) {
                res.add(c);
            }
        }
        
        return res;
    }

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.SCARABEE;
    }

    @Override
    public String toString() {
        return "Scarabee  ";
    }
}