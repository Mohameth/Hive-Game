package Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;

import java.util.ArrayList;
import java.util.Collection;

public class Araignee extends Insecte {
	
    public Araignee(Joueur j) {
        super(j);
    }


    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
    	
        if(plateau.rucheBrisee2(this.getEmplacement())) {
        	return new ArrayList<>();
        }
        
        Case case1=null,case2=null;
        ArrayList<Case> caseInter=new ArrayList<>();
        ArrayList<Case> casePossibles1=new ArrayList<>();
        ArrayList<Case> casePossibles2=new ArrayList<>();
        
        for(int i=0;i<3;i++) {
        	if(i==0) {
        		casePossibles1=(ArrayList<Case>) plateau.getCasesVoisines(this.getEmplacement(),true);
        		for(int j=0;j<casePossibles1.size();j++) {
        			
        			if(!caseInter.contains(casePossibles1.get(j))) {
        				caseInter.add(casePossibles1.get(j));
        			}
        			
        			if(!plateau.glissementPossibles(case1,casePossibles1.get(j))) {
        				casePossibles1.remove(j);
        			}
        		}
        		case1=null;case2=null;
        		if(casePossibles1.size()>=1) {
        			case1=casePossibles1.get(0);
        		}
        		if(casePossibles1.size()==2) {
        			case2=casePossibles1.get(1);
        		}
        		
        	}else {
        		
        		if(case1!=null) {
        			casePossibles1=(ArrayList<Case>) plateau.getCasesVoisines(case1,true);
        		}
        		if(case2!=null) {
        			casePossibles2=(ArrayList<Case>) plateau.getCasesVoisines(case2,true);
        		}
        		
        		for(int j=0;j<casePossibles1.size();j++) {
        			
        			if(!caseInter.contains(casePossibles1.get(j))) {
        				caseInter.add(casePossibles1.get(j));
        				
        				if(!plateau.glissementPossibles(case1,casePossibles1.get(j))) {
            				casePossibles1.remove(j);
            			}
        			}else {
        				casePossibles1.remove(j);;
        			}
        			
        		}
        		
        		for(int j=0;j<casePossibles2.size();j++) {
        			
        			if(!caseInter.contains(casePossibles2.get(j))) {
        				caseInter.add(casePossibles2.get(j));
        				
        				if(!plateau.glissementPossibles(case1,casePossibles2.get(j))) {
            				casePossibles2.remove(j);
            			}
        			}else {
        				casePossibles2.remove(j);;
        			}
        			
        		}
        		if(!casePossibles1.isEmpty()) {
        			case1=casePossibles1.get(0);
        		}
        		if(!casePossibles2.isEmpty()) {
        			case2=casePossibles2.get(0);
        		}
        	}
        	casePossibles1=new ArrayList<>();
    		casePossibles2=new ArrayList<>();     	 
        }
        ArrayList<Case> casePossibles=new ArrayList<>();
        if(!casePossibles1.isEmpty())
        casePossibles.add(casePossibles1.get(0));
        if(!casePossibles2.isEmpty())
        casePossibles.add(casePossibles2.get(0));
        
        return casePossibles;
    }

    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        super.deplacement(plat, cible);
    }*/

}