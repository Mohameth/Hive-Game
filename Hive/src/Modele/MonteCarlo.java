package Modele;

import java.util.ArrayList;
import java.util.Random;

import Modele.Insectes.Insecte;

public class MonteCarlo {

	private Noeud racine;
	private int maxProfondeur;
	private int maxFils;
	
	public MonteCarlo(Plateau plateau,int nbTours,Noeud racine) {
		
		this.racine=racine;
		
		if(nbTours<=5) {
			maxProfondeur=4;
			maxFils=80;
		}else {
			maxProfondeur=2;
			maxFils=40;
		}
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
			
			plateau2=Pere.getPlateau().clone(mainIA2,PlateauIA2,mainAdverse2,PlateauAdverse2,Pere.getTourIA());
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
				coupleCaesInsecte=new CoupleCaesInsecte(i,c2);
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
		if(Pere.getListeFils().get(indice).getProfondeur()>maxProfondeur)
			return null;
		return Pere.getListeFils().get(indice);
	}
	
	public Noeud selection() {
		Noeud noeud=racine;
		
		while(noeud!=null && !noeud.getListeFils().isEmpty()) {
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
			
			plateau2=noeud.getPlateau().clone(mainIA2,PlateauIA2,mainAdverse2,PlateauAdverse2,noeud.getTourIA());
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
				coupleCaesInsecte=new CoupleCaesInsecte(i,c2);
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
		return false;
	}
	
}
