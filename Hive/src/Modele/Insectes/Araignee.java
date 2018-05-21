package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Araignee extends Insecte {
	
    public Araignee(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Araignee(this.getJoueur());
    }
	
    //@Override
    /*public Collection<Case> deplacementPossible(Plateau plateau) {
    	
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
        		}else{
        			case1=null;
        		}
        		
        		if(!casePossibles2.isEmpty()) {
        			case2=casePossibles2.get(0);
        		}else{
        			case2=null;
        		}
        	}
        	casePossibles1.clear();
    		casePossibles2.clear();     	 
        }
        ArrayList<Case> casePossibles=new ArrayList<>();
        if(case1!=null)
        casePossibles.add(case1);
        if(case2!=null)
        casePossibles.add(case2);
        
        return casePossibles;
    }*/

    
    //@Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (!this.getJoueur().tousPionsPosables()) return new ArrayList<>();
        ArrayList<Case> res = new ArrayList<>();
        ArrayList<Case> visitees = new ArrayList<>();
        visitees.addAll(plateau.getCasesVoisines(this.getEmplacement(), true));
        deplacementPossibleWorker(plateau, plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true), 1, res, visitees);
        return res;
    }
        
        
    private void deplacementPossibleWorker(Plateau p, Collection<Case> departs, int dist, ArrayList<Case> res, ArrayList<Case> visitees) {
        if (dist > 4) return;
        visitees.addAll(departs);
        for (Case c : departs) {
            if (dist == 3) res.addAll(departs);
            else if (dist < 3) {
                Collection<Case> aVisite = p.getCasesVoisinesAccessibles(c, true);
                aVisite.removeAll(visitees);
                deplacementPossibleWorker(p, aVisite, dist+1, res, visitees);
            }
        }
    }

    @Override
    public TypeInsecte getType() {
       return TypeInsecte.ARAIGNEE;
    }

    @Override
    public String toString() {
        return "Araignee  ";
    }
}