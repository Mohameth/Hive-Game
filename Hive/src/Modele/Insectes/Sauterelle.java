package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.TypeInsecte;

import java.util.ArrayList;
import java.util.Collection;

public class Sauterelle extends Insecte {

    public Sauterelle(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Sauterelle(this.getJoueur());
    	
    }

    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (!this.getJoueur().tousPionsPosables()) return new ArrayList<>();
    	ArrayList<Case> caseVoisins=(ArrayList<Case>) plateau.getCasesVoisinesOccupees(this.getEmplacement());
    	ArrayList<Case> casePossibles=new ArrayList<>();
    	
    	for(int i=0;i<caseVoisins.size();i++) {
    		Case c=caseVoisins.get(i);
    		
    		if(this.getEmplacement().estVoisinHaut(c)) {
    			casePossibles.add(extremiteHaut(c,plateau));
    		}
    		else if(this.getEmplacement().estVoisinDroiteHaut(c)) {
    			casePossibles.add(extremiteDroiteHaut(c,plateau));
    		}
    		else if(this.getEmplacement().estVoisinDroiteBas(c)) {
    			casePossibles.add(extremiteDroiteBas(c,plateau));
    		}
    		else if(this.getEmplacement().estVoisinBas(c)) {
    			casePossibles.add(extremiteBas(c,plateau));
    		}
    		else if(this.getEmplacement().estVoisinGaucheBas(c)) {
    			casePossibles.add(extremiteGaucheBas(c,plateau));
    		}
    		else {
    			casePossibles.add(extremiteGaucheHaut(c,plateau));
    		}
    	}
    	
    	return casePossibles;
    }
    
    private Case extremiteHaut(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinHaut());
    	}while(!c2.estVide());
    	
    	return c2;
    }
    
    private Case extremiteDroiteHaut(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinDroiteHaut());
    	}while(!c2.estVide());
    	
    	return c2;
    }
    
    private Case extremiteDroiteBas(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinDroiteBas());
    	}while(!c2.estVide());
    	
    	return c2;
    }
    
    private Case extremiteBas(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinBas());
    	}while(!c2.estVide());
    	
    	return c2;
    }
    
    private Case extremiteGaucheBas(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinGaucheBas());
    	}while(!c2.estVide());
    	
    	return c2;
    }
    
    private Case extremiteGaucheHaut(Case c,Plateau plateau) {
    	
    	Case c2=c;
    	
    	do {
    		c2=plateau.getCase(c2.getCoordonnees().voisinGaucheHaut());
    	}while(!c2.estVide());
    	
    	return c2;
    }

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.SAUTERELLE;
    }

    @Override
    public String toString() {
        return "Sauterelle";
    }
}