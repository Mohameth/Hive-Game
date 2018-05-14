package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.ArrayList;
import java.util.Collection;

public class Coccinelle extends Insecte {

    public Coccinelle(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
    	
    	if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
    	
    	   ArrayList<Case> casePossibles=new ArrayList<>();
    	casePossibles.add(this.getEmplacement());
    	
    	for(int i=0;i<2;i++) {
    		ArrayList<Case> casePossibles2=new ArrayList<>();
    		
    		for(int j=0;j<casePossibles.size();j++) {
    	    	ArrayList<Case> caseVoisinesOccupees=(ArrayList<Case>) plateau.getCasesVoisinesOccupees(casePossibles.get(j));

    	    	for(int z=0;z<caseVoisinesOccupees.size();z++) {
    	    		if(!casePossibles2.contains(caseVoisinesOccupees.get(z))) {
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
    	return casePossibles3;
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}