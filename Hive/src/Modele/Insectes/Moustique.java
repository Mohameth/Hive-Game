package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.TypeInsecte;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Moustique extends Insecte {

    public Moustique(Joueur j) {
        super(j);
    }
	
	public Insecte clone() {
    	return new Moustique(this.getJoueur());
    	
    }

    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
        if (!this.getJoueur().tousPionsPosables() || !this.getEmplacement().getInsecteOnTop().equals(this)) return new ArrayList<>();
    	
    	if(this.getEmplacement().getInsectes().size()>1) {
        	return plateau.getCasesVoisines(this.getEmplacement(), false);
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
    	if(casePossibles.contains(this.getEmplacement())) {
    		casePossibles.remove(this.getEmplacement());
    	}
    	if(!casePossibles.isEmpty() && plateau.rucheBrisee(this.getEmplacement(),casePossibles.get(0))) {
    		return new ArrayList<>();
    	}
        
        Iterator<Case> it = casePossibles.iterator();
        while (it.hasNext()) {
            Case possibilite = it.next();
            for (Case c : plateau.getCasesVoisines(this.getEmplacement(), true)) {
                if (c.equals(possibilite)) {
                    if (!plateau.glissementPossible(this.getEmplacement(), c)) {
                        it.remove();
                    }
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

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.MOUSTIQUE;
    }

    @Override
    public String toString() {
        return "Moustique ";
    }
}