package Modele;

import java.util.ArrayList;
import java.util.Random;

import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;

public class MonteCarlo {

	private Noeud racine;
	private int maxFils;
	private int nbNoeuds;
	private int nbNoeudsMax;
	private JoueurIA joueurIA;
	
	public MonteCarlo(int nbTours,Noeud racine,JoueurIA joueurIA) {
		
		this.racine=racine;
		this.nbNoeuds=0;
		this.joueurIA=joueurIA;
		if(nbTours<=5) {
			this.maxFils=80;
			this.nbNoeudsMax=518481;
		}else {
			this.maxFils=30;
			this.nbNoeudsMax=837931;
		}
	}
	
	public int getNbNoeuds() {
		return nbNoeuds;
	}
	
	public int getNbNoeudsMax() {
		return nbNoeudsMax;
	}
	
	private Noeud chosirFils(Noeud Pere) {
		
		if(!Pere.getNbFilsMax()) {
			Random r=new Random();
			CoupleCaesInsecte coupleCaesInsecte;
			
			Plateau plateau2;
			ArrayList<Insecte>mainIA2=new ArrayList<>();
			ArrayList<Insecte>mainAdverse2=new ArrayList<>();
			ArrayList<Insecte>PlateauIA2=new ArrayList<>();
			ArrayList<Insecte>PlateauAdverse2=new ArrayList<>();
			
			plateau2=Pere.getPlateau().clone(mainIA2,PlateauIA2,mainAdverse2,PlateauAdverse2,joueurIA);
			ArrayList<Insecte> joueurCourant=new ArrayList<>();
			
			if(Pere.getTourIA()) {
				joueurCourant.addAll(mainIA2);
				joueurCourant.addAll(PlateauIA2);
			}else {
				joueurCourant.addAll(mainAdverse2);
				joueurCourant.addAll(PlateauAdverse2);
			}
				
			ArrayList<Case> c;
			int count=0;
			boolean b1=false,b2=false;
			Insecte i;
			Case c2=null;
			
			do {
				count++;b1=false;b2=false;
				i=joueurCourant.get(r.nextInt(joueurCourant.size()));
				
				if(i.getEmplacement()==null) {
					c=plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));;
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b1=true;
					}
				}else {
					c=(ArrayList<Case>) i.deplacementPossible(plateau2);
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b2=true;
					}
				}
				coupleCaesInsecte=new CoupleCaesInsecte(i,c2,i.getEmplacement());
			}while(!b1 && !b2 && Pere.existePossibilite(coupleCaesInsecte) && count<30);
			
			if(count==30 && !b1 && !b2) {
				Pere.setNbFilsMax();
			}else {
			Pere.ajoutPossibilite(coupleCaesInsecte);
				
				if(b1) {
					if(Pere.getTourIA()) {
						PlateauIA2.add(i);
						mainIA2.remove(i);
					}else {
						PlateauAdverse2.add(i);
						mainAdverse2.remove(i);
					}
					plateau2.ajoutInsecte(i,c2.getCoordonnees());
				}else {
					i.deplacement(plateau2,c2.getCoordonnees());
				}
				Noeud fils=new Noeud(Pere,plateau2,mainIA2,mainAdverse2,PlateauIA2,PlateauAdverse2);
				Pere.AjoutFils(fils);
				if(Pere.getNbFils()==this.maxFils) {
					Pere.setNbFilsMax();
				}
				return fils;
			}
		}
		double max=0.0;
		int indice=0;
		
		for(int i=0;i<Pere.getNbFils();i++) {
			Noeud fils=Pere.getListeFils().get(i);
			if(max<fils.getUSB()) {
				max=fils.getUSB();
				indice=i;
			}
		}
		
		return Pere.getListeFils().get(indice);
	}
	
	public Noeud selection() {
		Noeud noeud=racine;
		
		while(!noeud.getListeFils().isEmpty()) {
			noeud=chosirFils(noeud);
		}
		
		return noeud;
	}
	
	public Noeud Expansion(Noeud noeud) {
		if(noeud.getNi()==0) {
			return noeud;
		}
		
		Random r=new Random();
		CoupleCaesInsecte coupleCaesInsecte;
		
			Plateau plateau2;
			ArrayList<Insecte>mainIA2=new ArrayList<>();
			ArrayList<Insecte>mainAdverse2=new ArrayList<>();
			ArrayList<Insecte>PlateauIA2=new ArrayList<>();
			ArrayList<Insecte>PlateauAdverse2=new ArrayList<>();
			
			plateau2=noeud.getPlateau().clone(mainIA2,PlateauIA2,mainAdverse2,PlateauAdverse2,joueurIA);
			ArrayList<Insecte> joueurCourant=new ArrayList<>();
			
			if(noeud.getTourIA()) {
				joueurCourant.addAll(mainIA2);
				joueurCourant.addAll(PlateauIA2);
			}else {
				joueurCourant.addAll(mainAdverse2);
				joueurCourant.addAll(PlateauAdverse2);
			}
				
			ArrayList<Case> c;
			boolean b1=false,b2=false;
			Insecte i;
			Case c2=null;
			
			do {
				b1=false;b2=false;
				i=joueurCourant.get(r.nextInt(joueurCourant.size()));
				
				if(i.getEmplacement()==null) {
					c=plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b1=true;
					}
				}else {
					c=(ArrayList<Case>) i.deplacementPossible(plateau2);
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b2=true;
					}
				}
				coupleCaesInsecte=new CoupleCaesInsecte(i,c2,i.getEmplacement());
			}while(!b1 && !b2);
			
			noeud.ajoutPossibilite(coupleCaesInsecte);
			
			if(b1) {
				if(noeud.getTourIA()) {
					PlateauIA2.add(i);
					mainIA2.remove(i);
				}else {
					PlateauAdverse2.add(i);
					mainAdverse2.remove(i);
				}
				plateau2.ajoutInsecte(i,c2.getCoordonnees());
			}else {
				i.deplacement(plateau2,c2.getCoordonnees());
			}
			Noeud fils=new Noeud(noeud,plateau2,mainIA2,mainAdverse2,PlateauIA2,PlateauAdverse2);
			noeud.AjoutFils(fils);
		
		return fils;
	}
	
	public boolean simulation(Noeud n) {
		
		int count=0;
		Random r=new Random();
		boolean b=n.getTourIA();
		ArrayList<Insecte> joueurCourant=new ArrayList<>();
		ArrayList<Insecte>mainIA2=new ArrayList<>();
		ArrayList<Insecte>mainAdverse2=new ArrayList<>();
		ArrayList<Insecte>PlateauIA2=new ArrayList<>();
		ArrayList<Insecte>PlateauAdverse2=new ArrayList<>();
		Plateau plateau2=n.getPlateau().clone(mainIA2,PlateauIA2,mainAdverse2,PlateauAdverse2,joueurIA);
		ArrayList<Insecte> in=null;
		
		do {
			count++;
			if(b) {
				joueurCourant.addAll(mainIA2);
				joueurCourant.addAll(PlateauIA2);
			}else {
				joueurCourant.addAll(mainAdverse2);
				joueurCourant.addAll(PlateauAdverse2);
			}
			
			ArrayList<Case> c;
			boolean b1=false,b2=false;
			Insecte i;
			Case c2=null;
			
			do {
				b1=false;b2=false;
				i=joueurCourant.get(r.nextInt(joueurCourant.size()));
				
				if(i.getEmplacement()==null) {
					c=plateau2.pointVersCase(plateau2.casesVidePlacement(i.getJoueur()));
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b1=true;
					}
				}else {
					c=(ArrayList<Case>) i.deplacementPossible(plateau2);
					if(!c.isEmpty()) {
						c2=c.get(r.nextInt(c.size()));
						b2=true;
					}
				}
				
			}while(!b1 && !b2);
			
			if(b1) {
				if(b) {
					PlateauIA2.add(i);
					mainIA2.remove(i);
				}else {
					PlateauAdverse2.add(i);
					mainAdverse2.remove(i);
				}
				plateau2.ajoutInsecte(i,c2.getCoordonnees());
			}else {
				i.deplacement(plateau2,c2.getCoordonnees());
			}
			
			b=!b;
			if(b) {
				in=PlateauIA2;
			}else {
				in=PlateauAdverse2;
			}
			
		}while(!uneReineBloquee(in,plateau2) && count<=60);
		
		if(count>60)
			return false;
			
		return !b;
	}
	
	public void miseAjour(Noeud n,boolean b) {
		n.mettreAjour(b);
	}
	
	private boolean uneReineBloquee(ArrayList<Insecte> in2,Plateau plateau) {
		
		for(int i=0;i<in2.size();i++) {
			Insecte in=in2.get(i);
			int nb=plateau.getCasesVoisinesOccupees(in.getEmplacement()).size();
			if((in instanceof Reine) && (nb==6)) {
				return true;
			}
		}
		return false;
	}
	
}
