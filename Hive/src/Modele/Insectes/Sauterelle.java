package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;

import java.util.ArrayList;
import java.util.Collection;

public class Sauterelle extends Insecte {

    public Sauterelle(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
    	if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
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
    	int x=c.getCoordonnees().getX();
    	int y=c.getCoordonnees().getY()+1;
    	int z=c.getCoordonnees().getZ()-1;
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		y++;z--;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }
    
    private Case extremiteDroiteHaut(Case c,Plateau plateau) {
    	int x=c.getCoordonnees().getX()+1;
    	int y=c.getCoordonnees().getY();
    	int z=c.getCoordonnees().getZ()-1;
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		x++;z--;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }
    
    private Case extremiteDroiteBas(Case c,Plateau plateau) {
    	int x=c.getCoordonnees().getX()+1;
    	int y=c.getCoordonnees().getY()-1;
    	int z=c.getCoordonnees().getZ();
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		x++;y--;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }
    
    private Case extremiteBas(Case c,Plateau plateau) {
    	int x=c.getCoordonnees().getX();
    	int y=c.getCoordonnees().getY()-1;
    	int z=c.getCoordonnees().getZ()+1;
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		y--;z++;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }
    
    private Case extremiteGaucheBas(Case c,Plateau plateau) {
    	int x=c.getCoordonnees().getX()-1;
    	int y=c.getCoordonnees().getY();
    	int z=c.getCoordonnees().getZ()+1;
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		x--;z++;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }
    
    private Case extremiteGaucheHaut(Case c,Plateau plateau) {
    	int x=c.getCoordonnees().getX()-1;
    	int y=c.getCoordonnees().getY()+1;
    	int z=c.getCoordonnees().getZ();
    	Case c2=plateau.getCase(new Point3DH(x,y,z));
    	
    	while(!c2.estVide()) {
    		y++;x--;
    		c2=plateau.getCase(new Point3DH(x,y,z));
    	}
    	return c2;
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}