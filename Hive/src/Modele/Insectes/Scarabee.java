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
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        /*if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }*/
        
        return plateau.getCasesVoisinesAccessibles(this.getEmplacement(), false);
		
		/*if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
    	
    	ArrayList<Case> casePossibles=(ArrayList<Case>) plateau.getCasesVoisinesOccupees(this.getEmplacement());
    	ArrayList<Case> casePossibles2=(ArrayList<Case>) plateau.getCasesVoisines(this.getEmplacement(),true);
    	
    	for(int j=0;j<casePossibles2.size();j++) {
			
			if(plateau.glissementPossibles(this.getEmplacement(),casePossibles2.get(j))) {
				casePossibles.add(casePossibles2.get(j));
			}
		}
    	return casePossibles;*/
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