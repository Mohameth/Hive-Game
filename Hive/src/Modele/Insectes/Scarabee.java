package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
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
        
        return plateau.getCasesVoisinesAccessibles(this.getEmplacement(), false);
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