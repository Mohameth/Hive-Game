package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Coccinelle extends Insecte {

    public Coccinelle(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Coccinelle(this.getJoueur());
    	
    }
	
	public boolean equals(Insecte insecte) {
		return (insecte instanceof Coccinelle);
	}

    @Override
   public Collection<Case> deplacementPossible(Plateau plateau) {
    	
    	if(!this.getJoueur().reinePosee() || this.getEmplacement().getNbInsectes() != 1 || 
    			plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
    	
    	ArrayList<Case> casePossibles=new ArrayList<>();
    	casePossibles.add(this.getEmplacement());
    	Case c=this.getEmplacement();
    	
    	for(int i=0;i<2;i++) {
    		ArrayList<Case> casePossibles2=new ArrayList<>();
    		
    		for(int j=0;j<casePossibles.size();j++) {
    	    	ArrayList<Case> caseVoisinesOccupees=(ArrayList<Case>) plateau.getCasesVoisinesOccupees(casePossibles.get(j));

    	    	for(int z=0;z<caseVoisinesOccupees.size();z++) {
    	    		if(!casePossibles2.contains(caseVoisinesOccupees.get(z)) && !caseVoisinesOccupees.get(z).equals(c)) {
    	    			casePossibles2.add(caseVoisinesOccupees.get(z));
    	    		}
    	    	}
    		}
    		casePossibles=casePossibles2;
    	}
    	ArrayList<Case> casePossibles3=new ArrayList<>();
    	
    	for(int i=0;i<casePossibles.size();i++) {
    		ArrayList<Case> caseVoisinesVides=(ArrayList<Case>) plateau.getCasesVoisines(casePossibles.get(i),true);
    		
    		for(int j=0;j<caseVoisinesVides.size();j++) {
    			if(!casePossibles3.contains(caseVoisinesVides.get(j))) {
	    			casePossibles3.add(caseVoisinesVides.get(j));
	    		}
    		}
    	}
    	
    	if(casePossibles3.contains(this.getEmplacement())) {
    		casePossibles3.remove(this.getEmplacement());
    	}
        
    	return casePossibles3;
    }


    @Override
    public TypeInsecte getType() {
        return TypeInsecte.COCCINELLE;
    }

    @Override
    public String toString() {
        return "Coccinelle";
    }
}