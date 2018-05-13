package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import java.util.Collection;

public class Moustique extends Insecte {

    public Moustique(Joueur j) {
        super(j);
    }


    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
    	
    	if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
    	
    	ArrayList<Case> caseVoisinesOccupees=(ArrayList<Case>) plateau.getCasesVoisinesOccupees(this.getEmplacement());
    	ArrayList<Case> casePossibles=new ArrayList<>();
    	
    	for(int i=0;i<caseVoisinesOccupees.size();i++) {
    		ArrayList<Case> casePossibles2=copieDeplacement(caseVoisinesOccupees.get(i).getInsecteOnTop(),plateau);
    		
    		for(int j=0;j<casePossibles2.size();j++) {
    			if(!casePossibles.contains(casePossibles2.get(j))) {
    				casePossibles.add(casePossibles2.get(j));
    			}
    		}
    	}
    	
    	return casePossibles;
    }
    
    private ArrayList<Case> copieDeplacement(Insecte insecte,Plateau plateau) {
    	Insecte insecte2=null;
    	
    	if(insecte instanceof Araignee) {
    		insecte2=new Araignee(this.getJoueur());
    	}
    	else if(insecte instanceof Coccinelle) {
    		insecte2=new Coccinelle(this.getJoueur());
    	}
    	else if(insecte instanceof Fourmi) {
    		insecte2=new Fourmi(this.getJoueur());
    	}
    	else if(insecte instanceof Reine) {
    		insecte2=new Reine(this.getJoueur());
    	}
    	else if(insecte instanceof Sauterelle) {
    		insecte2=new Sauterelle(this.getJoueur());
    	}
    	else if(insecte instanceof Scarabee) {
    		insecte2=new Scarabee(this.getJoueur());
    	}
    	else {
    		return new ArrayList<>();
    	}
    	
    	insecte2.setEmplacement(this.getEmplacement());
    	return (ArrayList<Case>) insecte2.deplacementPossible(plateau);
    }
    

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}