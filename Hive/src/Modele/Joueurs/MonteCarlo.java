package Modele.Joueurs;

import Modele.Case;
import Modele.CoupleCaesInsecte;
import Modele.HexaPoint;
import java.util.ArrayList;
import java.util.Random;

import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import Modele.Plateau;

public class MonteCarlo {

    private Noeud racine;
    private int maxFils;
    private int nbNoeuds;
    private int nbNoeudsMax;

    public MonteCarlo(int nbTours, Noeud racine) {

        this.racine = racine;
        this.nbNoeuds = 1;
        if (nbTours <= 5) {
            this.maxFils = 30;
            this.nbNoeudsMax = 931;
        } else {
            this.maxFils = 40;
            this.nbNoeudsMax = 1641;
        }
    }

    public int getNbNoeuds() {
        return nbNoeuds;
    }

    public int getNbNoeudsMax() {
        return nbNoeudsMax;
    }

    private Noeud chosirFils(Noeud Pere) {

        if (!Pere.getNbFilsMax()) {
        	
            CoupleCaesInsecte coupleCaesInsecte=null;

            Plateau plateau2;
            ArrayList<Insecte> mainIA2 =cloneInsecte(Pere.getMainIA());
            ArrayList<Insecte> mainAdverse2 =cloneInsecte(Pere.getMainAdverse());
            ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
            ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();
            plateau2 = Pere.getPlateau().clone(PlateauIA2,PlateauAdverse2);
            //System.out.println(PlateauIA2);System.out.println(PlateauAdverse2);
            ArrayList<Insecte> joueurCourant = new ArrayList<>();
            
            if (Pere.getTourIA()) {
                joueurCourant.addAll(mainIA2);
                joueurCourant.addAll(PlateauIA2);
            } else {
                joueurCourant.addAll(mainAdverse2);
                joueurCourant.addAll(PlateauAdverse2);
            }

            ArrayList<HexaPoint> c;
            int count = 0;
            Insecte i=null;
            Case c2 = null;

            while((c2==null ||Pere.existePossibilite(coupleCaesInsecte))&& count<60) {
            	if((Pere.getTourIA() && Pere.getNbTourIA()==4 && (i=getReine(mainIA2))!=null)||
            			(!Pere.getTourIA() && Pere.getNbTourAdverse()==4 && (i=getReine(mainAdverse2))!=null)) {
            			Random r=new Random();        			
            			c=plateau2.casesVidePlacement(i.getJoueur());
            			HexaPoint p=c.get(r.nextInt(c.size()));
            			c2 = plateau2.getCase(p);count++;
            	}
            	
            	else {
            		count++;Random r=new Random();
                    i = joueurCourant.get(r.nextInt(joueurCourant.size()));

                    if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
                        c=plateau2.casesVidePlacement(i.getJoueur());
                        if (!c.isEmpty()) {
                        	HexaPoint p=c.get(r.nextInt(c.size()));
                            c2=plateau2.getCase(p);
                        }
                    } else if((Pere.getTourIA() && this.getReine(mainIA2)==null)||(!Pere.getTourIA() && this.getReine(mainAdverse2)==null)){
                    	ArrayList<Case> c3 = (ArrayList<Case>) i.deplacementPossible(plateau2);
                        if (!c3.isEmpty()) {
                            c2 = c3.get(r.nextInt(c3.size()));
                        }
                    }
            	}
            	
                coupleCaesInsecte = new CoupleCaesInsecte(i, c2, i.getEmplacement());
            }

            if (count==60) {
                Pere.setNbFilsMax();
            } else {
                Pere.ajoutPossibilite(coupleCaesInsecte);

                if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
                    if (Pere.getTourIA()) {
                        PlateauIA2.add(i);
                        mainIA2.remove(i);
                    } else {
                        PlateauAdverse2.add(i);
                        mainAdverse2.remove(i);
                    }
                    plateau2.ajoutInsecte(i, c2.getCoordonnees());
                } else {
                    i.deplacement(plateau2, c2.getCoordonnees());
                }
                Noeud fils = new Noeud(Pere, plateau2, mainIA2, mainAdverse2, PlateauIA2, PlateauAdverse2);
                Pere.AjoutFils(fils);
                nbNoeuds++;
                if (Pere.getNbFils() == this.maxFils) {
                    Pere.setNbFilsMax();
                }
                //System.out.println("fils cree");
                return fils;
            }
        }
        double max = 0.0;
        int indice = 0;

        for (int i = 0; i < Pere.getNbFils(); i++) {
            Noeud fils = Pere.getListeFils().get(i);
            if (max < fils.getUSB()) {
                max = fils.getUSB();
                indice = i;
            }
        }
        
        return Pere.getListeFils().get(indice);
    }

    public Noeud selection() {
        Noeud noeud = racine;

        while (noeud.getNbFils() != 0) {
            noeud = chosirFils(noeud);
        }

        return noeud;
    }

    public Noeud Expansion(Noeud noeud) {
        if (noeud.getNi() == 0) {
            return noeud;
        }
        
        CoupleCaesInsecte coupleCaesInsecte=null;

        Plateau plateau2;
        ArrayList<Insecte> mainIA2 =cloneInsecte(noeud.getMainIA());
        ArrayList<Insecte> mainAdverse2 =cloneInsecte(noeud.getMainAdverse());
        ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
        ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();
        plateau2 = noeud.getPlateau().clone(PlateauIA2, PlateauAdverse2);
        ArrayList<Insecte> joueurCourant = new ArrayList<>();
        
        if (noeud.getTourIA()) {
            joueurCourant.addAll(mainIA2);
            joueurCourant.addAll(PlateauIA2);
        } else {
            joueurCourant.addAll(mainAdverse2);
            joueurCourant.addAll(PlateauAdverse2);
        }

        ArrayList<HexaPoint> c;
        Insecte i=null;
        Case c2 = null;

        while(c2==null) {
        	
        	if((noeud.getTourIA() && noeud.getNbTourIA()==4 && (i=getReine(mainIA2))!=null)||
        			(!noeud.getTourIA() && noeud.getNbTourAdverse()==4 && (i=getReine(mainAdverse2))!=null)) {
        			Random r=new Random();        			
        			c=plateau2.casesVidePlacement(i.getJoueur());
        			HexaPoint p=c.get(r.nextInt(c.size()));
        			c2 = plateau2.getCase(p);
        	}	
        
        else {
        	Random r=new Random();
            i = joueurCourant.get(r.nextInt(joueurCourant.size()));         
            
            if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
                c = plateau2.casesVidePlacement(i.getJoueur());
                if (!c.isEmpty()) {
                	HexaPoint p=c.get(r.nextInt(c.size()));
        			c2 = plateau2.getCase(p);
                }
            } else if((noeud.getTourIA() && this.getReine(mainIA2)==null)||(!noeud.getTourIA() && this.getReine(mainAdverse2)==null)){
            	ArrayList<Case> c3 = (ArrayList<Case>) i.deplacementPossible(plateau2);
                if (!c3.isEmpty()) {
                    c2 = c3.get(r.nextInt(c3.size()));
                }
            }
        }
        	
            coupleCaesInsecte = new CoupleCaesInsecte(i, c2, i.getEmplacement());
        }

        noeud.ajoutPossibilite(coupleCaesInsecte);

        if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
            if (noeud.getTourIA()) {
                PlateauIA2.add(i);
                mainIA2.remove(i);
            } else {
                PlateauAdverse2.add(i);
                mainAdverse2.remove(i);
            }
            plateau2.ajoutInsecte(i, c2.getCoordonnees());
        } else {
            i.deplacement(plateau2, c2.getCoordonnees());
        }
        Noeud fils = new Noeud(noeud, plateau2, mainIA2, mainAdverse2, PlateauIA2, PlateauAdverse2);
        noeud.AjoutFils(fils);
        nbNoeuds++;

        return fils;
    }

    public double simulation(Noeud n) {

        int count = 0;
        int nbTourIA=n.getNbTourIA();
        int nbTourAdverse=n.getNbTourAdverse();
        boolean b = n.getTourIA();
        ArrayList<Insecte> joueurCourant;
        ArrayList<Insecte> mainIA2 =cloneInsecte(n.getMainIA());
        ArrayList<Insecte> mainAdverse2 =cloneInsecte(n.getMainAdverse());
        ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
        ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();
        Plateau plateau2 = n.getPlateau().clone(PlateauIA2,PlateauAdverse2);
        ArrayList<Insecte> in1 = null;
        ArrayList<Insecte> in2 = null;
        if(b) {
        	in1=PlateauIA2;
            in2 =PlateauAdverse2;
        }else {
        	in1=PlateauAdverse2;
            in2 =PlateauIA2;
        }       
        
        while (!coupGagnat(in1,in2,plateau2) && count<60){
        	joueurCourant = new ArrayList<>();
            if (b) {
                joueurCourant.addAll(mainIA2);
                joueurCourant.addAll(PlateauIA2);
            } else {
                joueurCourant.addAll(mainAdverse2);
                joueurCourant.addAll(PlateauAdverse2);
            }

            ArrayList<HexaPoint> c =new ArrayList<>();
            Insecte i=null;
            Case c2 = null;
            
            while(c2==null && !joueurCourant.isEmpty()) {
            	Random r=new Random();
            	
            	if((b && nbTourIA==4 && (i=getReine(mainIA2))!=null)||(!b && nbTourAdverse==4 &&
            			(i=getReine(mainAdverse2))!=null)) {
            			c=plateau2.casesVidePlacement(i.getJoueur());
            			HexaPoint p=c.get(r.nextInt(c.size()));
            			c2 = plateau2.getCase(p);
            	}  	
            else {
                int res =r.nextInt(joueurCourant.size());
                i = joueurCourant.get(res);

                if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
                    c =plateau2.casesVidePlacement(i.getJoueur());
                    if (!c.isEmpty()) {
            			HexaPoint p=c.get(r.nextInt(c.size()));
            			c2 = plateau2.getCase(p);
                    }
                    
                } else if((b && this.getReine(mainIA2)==null)||(!b && this.getReine(mainAdverse2)==null)){
                	ArrayList<Case> c3 = (ArrayList<Case>) i.deplacementPossible(plateau2);
                    if (!c3.isEmpty()) {
                        c2 = c3.get(r.nextInt(c3.size()));
                    }
                }
            }
             if(c2==null) {
            	 joueurCourant.remove(i);
             }
            }
            
            if(c2==null) {
            	if( plateau2.getCasesVoisinesOccupees(this.getReine(PlateauIA2).getEmplacement()).size()<
            	plateau2.getCasesVoisinesOccupees(this.getReine(PlateauAdverse2).getEmplacement()).size()) {
            		return 0.25;
            	}
            	return 0.0;
            }
          
            if (mainIA2.contains(i) || mainAdverse2.contains(i)) {
                if (b) {
                    PlateauIA2.add(i);
                    mainIA2.remove(i);
                } else {
                    PlateauAdverse2.add(i);
                    mainAdverse2.remove(i);
                }
                plateau2.ajoutInsecte(i, c2.getCoordonnees());
            } else {
            	i.deplacement(plateau2,c2.getCoordonnees());
            }
            b = !b;
            if(b) {
            	in1=PlateauIA2;
                in2 =PlateauAdverse2;
                nbTourAdverse++;
            }else {
            	in1=PlateauAdverse2;
                in2 =PlateauIA2;
                nbTourIA++;
            }
            count++;
        }

        if (count == 60) {
        	if( plateau2.getCasesVoisinesOccupees(this.getReine(PlateauIA2).getEmplacement()).size()<
                	plateau2.getCasesVoisinesOccupees(this.getReine(PlateauAdverse2).getEmplacement()).size()) {
                		return 0.5;
                	}
                	return 0.0;
    
        }
        if(b) {
        	return 1.0; 
        }
        return 0.0;
    }

    public void miseAjour(Noeud n, double b) {
        n.mettreAjour(b);
    }

    private Case uneReinePresqueBloquee(ArrayList<Insecte> in2, Plateau plateau) {

    	boolean ok=true;
    	Insecte in=null;
    	
        for (int i = 0; i < in2.size() && ok; i++) {
            if(in2.get(i) instanceof Reine) {
            	in=in2.get(i);
            	ok=false;
            }
        }
        if((in!=null) && (plateau.getCasesVoisinesOccupees(in.getEmplacement()).size()==5)) {
        	ArrayList<Case>c=(ArrayList<Case>) plateau.getCasesVoisines(in.getEmplacement(),true);
        	return c.get(0);
        }
        return null;
    }
    
    private ArrayList<Insecte> cloneInsecte(ArrayList<Insecte> insecte){
    	ArrayList<Insecte> in=new ArrayList<>();
    	for(int i=0;i<insecte.size();i++) {
    		Insecte ii=insecte.get(i).clone();
    		in.add(ii);
    	}
    	return in;
    }
    
    private boolean coupGagnat(ArrayList<Insecte> in1,ArrayList<Insecte> in2, Plateau plateau) {
    	Case c=uneReinePresqueBloquee(in2,plateau);
    	if(c==null) {
    		return false;
    	}
    	for(int i=0;i<in1.size();i++) {
    		if(in1.get(i).deplacementPossible(plateau).contains(c)) {
    			if (!in1.get(i).getEmplacement().estVoisin(c) || in1.get(i).getEmplacement().getNbInsectes() > 1) {
    				in1.get(i).deplacement(plateau,c.getCoordonnees());
        			return true;
    			}
    		}
    	}
    	return false;
    }
    
    private Reine getReine(ArrayList<Insecte> insecte) {
    	for(int i=0;i<insecte.size();i++) {
    		if(insecte.get(i) instanceof Reine) {
    			return (Reine) insecte.get(i);
    		}
    	}
    	return null;
    }

}

