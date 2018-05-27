package Modele.Joueurs;

import Modele.Case;
import Modele.CoupleCaesInsecte;
import Modele.Joueurs.JoueurIA;
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
    private JoueurIA joueurIA;

    public MonteCarlo(int nbTours, Noeud racine, JoueurIA joueurIA) {

        this.racine = racine;
        this.nbNoeuds = 1;
        this.joueurIA = joueurIA;
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
        	
            CoupleCaesInsecte coupleCaesInsecte;

            Plateau plateau2;
            Random r=new Random();
            ArrayList<Insecte> mainIA2 =cloneInsecte(Pere.getMainIA());
            ArrayList<Insecte> mainAdverse2 =cloneInsecte(Pere.getMainAdverse());
            ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
            ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();

            plateau2 = Pere.getPlateau().clone(PlateauIA2,PlateauAdverse2,joueurIA);
            ArrayList<Insecte> joueurCourant = new ArrayList<>();

            if (Pere.getTourIA()) {
                joueurCourant.addAll(mainIA2);
                joueurCourant.addAll(PlateauIA2);
            } else {
                joueurCourant.addAll(mainAdverse2);
                joueurCourant.addAll(PlateauAdverse2);
            }

            ArrayList<Case> c;
            int count = 0;
            boolean b1 = false, b2 = false;
            Insecte i;
            Case c2 = null;

            do {
                count++;b1=false;b2=false;
                i = joueurCourant.get(r.nextInt(joueurCourant.size()));

                if (i.getEmplacement() == null) {
                    c = plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));;
                    if (!c.isEmpty()) {
                        c2 = c.get(r.nextInt(c.size()));
                        b1 = true;
                    }
                } else {
                    c = (ArrayList<Case>) i.deplacementPossible(plateau2);
                    if (!c.isEmpty()) {
                        c2 = c.get(r.nextInt(c.size()));
                        b2 = true;
                    }
                }
                coupleCaesInsecte = new CoupleCaesInsecte(i, c2, i.getEmplacement());
            } while (((!b1 && !b2) || Pere.existePossibilite(coupleCaesInsecte)) && count < 60);

            if (count == 60 && !b1 && !b2) {
                Pere.setNbFilsMax();
            } else {
                Pere.ajoutPossibilite(coupleCaesInsecte);

                if (b1) {
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
        
        CoupleCaesInsecte coupleCaesInsecte;

        Plateau plateau2;
        ArrayList<Insecte> mainIA2 =cloneInsecte(noeud.getMainIA());
        ArrayList<Insecte> mainAdverse2 =cloneInsecte(noeud.getMainAdverse());
        ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
        ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();
        Random r=new Random();

        plateau2 = noeud.getPlateau().clone(PlateauIA2, PlateauAdverse2, joueurIA);
        ArrayList<Insecte> joueurCourant = new ArrayList<>();
        
        if (noeud.getTourIA()) {
            joueurCourant.addAll(mainIA2);
            joueurCourant.addAll(PlateauIA2);
        } else {
            joueurCourant.addAll(mainAdverse2);
            joueurCourant.addAll(PlateauAdverse2);
        }

        ArrayList<Case> c;
        boolean b1 = false, b2 = false;
        Insecte i;
        Case c2 = null;

        do {
            i = joueurCourant.get(r.nextInt(joueurCourant.size()));

            if (i.getEmplacement() == null) {
                c = plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));
                if (!c.isEmpty()) {
                    c2 = c.get(r.nextInt(c.size()));
                    b1 = true;
                }
            } else {
                c = (ArrayList<Case>) i.deplacementPossible(plateau2);
                if (!c.isEmpty()) {
                    c2 = c.get(r.nextInt(c.size()));
                    b2 = true;
                }
            }
            coupleCaesInsecte = new CoupleCaesInsecte(i, c2, i.getEmplacement());
        } while (!b1 && !b2);

        noeud.ajoutPossibilite(coupleCaesInsecte);

        if (b1) {
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
        Random r=new Random();
        ArrayList<Insecte> joueurCourant;
        ArrayList<Insecte> mainIA2 =cloneInsecte(n.getMainIA());
        ArrayList<Insecte> mainAdverse2 =cloneInsecte(n.getMainAdverse());
        ArrayList<Insecte> PlateauIA2 = new ArrayList<>();
        ArrayList<Insecte> PlateauAdverse2 = new ArrayList<>();
        Plateau plateau2 = n.getPlateau().clone(PlateauIA2,PlateauAdverse2,joueurIA);
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

            ArrayList<Case> c =new ArrayList<>();
            boolean b1=false,b2=false;
            Insecte i;
            Case c2 = null;
            do {
            	b1=false;b2=false;
            	
            	if((b && nbTourIA==4 && (i=getReine(mainIA2))!=null)||(!b && nbTourAdverse==4 &&
            			(i=getReine(mainAdverse2))!=null)) {
            			c=plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));
            			c2 = c.get(r.nextInt(c.size()));
                        b1 = true;
            	}  	
            else {
                int res =r.nextInt(joueurCourant.size());
                i = joueurCourant.get(res);

                if (i.getEmplacement() == null) {
                    c = plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));
                    if (!c.isEmpty()) {
                        c2 = c.get(r.nextInt(c.size()));
                        b1 = true;
                    }else if(b){
                    	joueurCourant.removeAll(mainIA2);
                    }else {
                    	joueurCourant.removeAll(mainAdverse2);
                    }
                } else if((b && this.getReine(mainIA2)==null)||(!b && this.getReine(mainAdverse2)==null)){
                    c = (ArrayList<Case>) i.deplacementPossible(plateau2);
                    if (!c.isEmpty()) {
                    	int j=r.nextInt(c.size());
                        c2 = c.get(j);
                        b2 = true;
                    }else {
                    	joueurCourant.remove(i);
                    }
                }
            }
                
            } while (!b1 && !b2 && !joueurCourant.isEmpty());
            
            if(!b1 && !b2 && joueurCourant.isEmpty()) {
            	if( plateau2.getCasesVoisinesOccupees(this.getReine(PlateauIA2).getEmplacement()).size()<
            	plateau2.getCasesVoisinesOccupees(this.getReine(PlateauAdverse2).getEmplacement()).size()) {
            		return 0.25;
            	}
            	return 0.0;
            }
          
            if (b1) {
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

