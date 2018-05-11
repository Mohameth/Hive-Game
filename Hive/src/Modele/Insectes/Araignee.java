package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
        		case1=this.getEmplacement();
        		casePossibles1=(ArrayList<Case>) plateau.getCasesVoisines(case1,true);
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
        	casePossibles1.clear();
    		casePossibles2.clear();     	 
        }
        ArrayList<Case> casePossibles=new ArrayList<>();
        if(!casePossibles1.isEmpty())
        casePossibles.add(casePossibles1.get(0));
        if(!casePossibles2.isEmpty())
        casePossibles.add(casePossibles2.get(0));
        
        return casePossibles;
    }

    
    //@Override
    public Collection<Case> deplacementPossible2(Plateau plateau) {
        if (plateau.rucheBrisee2(this.getEmplacement()))
            return null;
        ArrayList<Case> res = new ArrayList<>();
        res.addAll(deplacementPossibleworker(plateau, this.getEmplacement(), 1, new ArrayList<>()));
        return res;
    }
        
        
    private HashSet<Case> deplacementPossibleworker(Plateau p, Case origine, int dist, ArrayList<Case> visitees) {
        HashSet<Case> res = new HashSet<>();
        ArrayList<Case> voisins = (ArrayList<Case>) p.getCasesVoisines(origine, true);
        for (Case c : voisins) {
            if (!p.getCasesVoisinesOccupees(c).isEmpty() && !p.gateBetween(this.getEmplacement(), c)) {
                if (dist < 3){
                    visitees.add(c);
                    res.addAll(deplacementPossibleworker(p, c, dist++, visitees));
                } else {
                    if (!visitees.contains(c))
                        res.add(c);
                }
            } else {
                
            }
        }
        return res;
}
    
    /*@Override
    public void deplacement(Plateau plat, Point3DH cible) {
        super.deplacement(plat, cible);
    }*/

}