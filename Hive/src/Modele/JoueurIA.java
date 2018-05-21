package Modele;

import Modele.Insectes.Insecte;
import java.util.ArrayList;
import java.util.Random;

import java.util.ArrayList;
import java.util.Random;


public class JoueurIA extends Joueur {
	private int difficulte;
	private Joueur adverse;

    public JoueurIA(Plateau p, int difficulte, boolean extensions,Joueur adverse) {
        super(p, extensions);
        this.difficulte = difficulte;
        this.adverse=adverse;
    }


    public JoueurIA(Plateau p, int difficulte, boolean extensions) {
        super(p, extensions);
        this.difficulte = difficulte;
        this.adverse = null;
    }

    public void addJoueurAdverse(Joueur j) {
        this.adverse = j;
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
    				this.plateau.ajoutInsecte(insecte, casePlacement.get(r.nextInt(casePlacement.size())).getCoordonnees());
    				return true;
    			}
    		}else if(this.reinePosee()){
    			b=!insecte.deplacementPossible(plateau).isEmpty();
    		}
    	}while(!b);
    	
    	ArrayList<Case> deplacement=(ArrayList<Case>) insecte.deplacementPossible(plateau);
    	insecte.deplacement(plateau,deplacement.get(r.nextInt(deplacement.size())).getCoordonnees());
    	
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