package Modele;

import Modele.Insectes.Insecte;
import java.util.ArrayList;
import java.util.Random;

import java.util.ArrayList;
import java.util.Random;

public class JoueurIA extends Joueur {
	private int difficulte;

    public JoueurIA(Plateau p, int difficulte, boolean extensions) {
        super(p, extensions);
        this.difficulte = difficulte;
    }


    @Override
    public boolean coup(Insecte insecte, Point3DH cible) {
    	if(difficulte==1) {
    		return coupFacile();
    	}
    	else if(difficulte==2) {
    		return coupNormal();
    	}
    	return coupDifficile();
    }
    
    private boolean coupFacile() {
    	boolean b =false;
    	Random r=new Random();
    	Insecte insecte;
    	
    	do {
    		insecte=this.getPions().get(r.nextInt(this.getPions().size()));
    		if(insecte.getEmplacement()==null) {
    			ArrayList<Point3DH> coordPlacement = plateau.casesVidePlacement(this);
                        ArrayList<Case> casePlacement = new ArrayList<>();
                        for (Point3DH p : coordPlacement) {
                            casePlacement.add(this.plateau.getCase(p));
                        }
    			if(!casePlacement.isEmpty()) {
                                Point3DH p =  casePlacement.get(r.nextInt(casePlacement.size())).getCoordonnees();
    				this.plateau.ajoutInsecte(insecte, p);
                                System.out.println(insecte.getClass() + " en " + p);
    				return true;
    			}
    		}else if(this.reinePosee()){
    			b=!insecte.deplacementPossible(plateau).isEmpty();
    		}
    	}while(!b);
    	
    	ArrayList<Case> deplacement=(ArrayList<Case>) insecte.deplacementPossible(plateau);
        Point3DH p =deplacement.get(r.nextInt(deplacement.size())).getCoordonnees();
    	insecte.deplacement(plateau, p);
        System.out.println(insecte.getClass() + " en " + p);
    	
    	return true;
    }
    
    private boolean coupNormal() {
    	boolean b =false;
    	Random r=new Random();
    	Insecte insecte;
    	
    	return false;
    }
    
    private boolean coupDifficile() {
    	return false;
    }

}