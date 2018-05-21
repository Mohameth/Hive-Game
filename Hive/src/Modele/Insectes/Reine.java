package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;

import java.util.ArrayList;
import java.util.Collection;

public class Reine extends Insecte {

    public Reine(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Reine(this.getJoueur());
    	
    }

    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
    	/*if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }*/
        
        return plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true);
    }
    	/*ArrayList<Case> casePossibles=(ArrayList<Case>) plateau.getCasesVoisines(this.getEmplacement(),true);
		
    	for(int j=0;j<casePossibles.size();j++) {
			
			if(!plateau.glissementPossibles(this.getEmplacement(),casePossibles.get(j))) {
				casePossibles.remove(j);
			}
		}
    	ArrayList<Case> casePossibles2=new ArrayList<>();
    	
		for(int i=0;i<casePossibles.size();i++) {
			casePossibles2.add(casePossibles.get(i));
		}
		
		return casePossibles2;
    }*/

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.REINE;
    }

    @Override
    public String toString() {
        return "Reine     ";
    }
}