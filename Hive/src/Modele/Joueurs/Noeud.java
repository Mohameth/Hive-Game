package Modele.Joueurs;

import Modele.CoupleCaesInsecte;
import Modele.IA.IAEvaluation;
import java.util.ArrayList;

import Modele.Insectes.Insecte;
import Modele.Plateau;

public class Noeud {

	private int ni;
	private double ti;
	private int nbTourIA;
	private int nbTourAdverse;
	private ArrayList<Noeud> Fils;
	private int nbFils;
	private Noeud Pere;
	private boolean nbFilsMax;
	private boolean tourIA;
	private int profondeur;
	private Plateau plateau;
	private ArrayList<CoupleCaesInsecte> possibilite;
	private ArrayList<Insecte> mainIA;
	private ArrayList<Insecte> mainAdverse;
	private ArrayList<Insecte> PlateauIA;
	private ArrayList<Insecte> PlateauAdverse;
	
	public Noeud(int nbTour,int nbTourAdverse,Plateau plateau,ArrayList<Insecte>mainIA,ArrayList<Insecte>mainAdverse
			,ArrayList<Insecte>PlateauIA,ArrayList<Insecte>PlateauAdverse) {
		this.ni=1;
		this.ti=1;
		this.nbFils=0;
		this.nbFilsMax=false;
		this.profondeur=0;
		this.Pere=null;
		this.plateau=plateau;
		tourIA=true;
		this.possibilite=new ArrayList<>();
		this.mainIA=mainIA;
		this.mainAdverse=mainAdverse;
		this.PlateauIA=PlateauIA;
		this.PlateauAdverse=PlateauAdverse;
        this.Fils = new ArrayList<>();
        this.nbTourIA=nbTour;
        this.nbTourAdverse=nbTourAdverse;
	}
	
	public Noeud(Noeud Pere,Plateau plateau,ArrayList<Insecte>mainIA,ArrayList<Insecte>mainAdverse
			,ArrayList<Insecte>PlateauIA,ArrayList<Insecte>PlateauAdverse) {
		this.ni=0;
		this.ti=0.0;
		this.nbFils=0;
		this.nbFilsMax=false;
		this.profondeur=Pere.getProfondeur()+1;
		this.Pere=Pere;
		this.plateau=plateau;
		tourIA=!Pere.getTourIA();
		this.possibilite=new ArrayList<>();
		this.mainIA=mainIA;
		this.mainAdverse=mainAdverse;
		this.PlateauIA=PlateauIA;
		this.PlateauAdverse=PlateauAdverse;
        this.Fils = new ArrayList<>();
        if(!tourIA) {
        	this.nbTourIA=Pere.getNbTourIA()+1;
        	this.nbTourAdverse=Pere.getNbTourAdverse();
        }else {
        	this.nbTourIA=Pere.getNbTourIA();
        	this.nbTourAdverse=Pere.getNbTourAdverse()+1;
        }     
	}
	
	public int getNi() {
                return ni;
	}
	
	public double getTi() {
		return ti;
	}
	
	public boolean getNbFilsMax() {
		return nbFilsMax;
	}
	
	public void setNbFilsMax() {
		nbFilsMax=true;
	}
	
	public int getNbFils() {
		return nbFils;
	}
	
	public ArrayList<Noeud> getListeFils(){
		return Fils;
	}
	
	public int getProfondeur() {
		return this.profondeur;
	}
	
	public ArrayList<Insecte> getMainIA(){
		return mainIA;
	}
	
	public ArrayList<Insecte> getMainAdverse(){
		return mainAdverse;
	}
	
	public ArrayList<Insecte> getPlateauIA(){
		return PlateauIA;
	}
	
	public ArrayList<Insecte> getPlateauAdverse(){
		return PlateauAdverse;
	}
	
	public Plateau getPlateau() {
		return plateau;
	}
	
	public double getUSB() {
		double Vi=ti/ni;
		double s=2.0*Math.sqrt(Math.log((double) Pere.getNi())/ni);
		return Vi+s;
	}
	
	public void mettreAjour(double b) {
		ni++;
		ti+=b;
		if(Pere!=null) {
			Pere.mettreAjour(b);
		}
	}
	
	public void AjoutFils(Noeud fils) {
		Fils.add(fils);
		nbFils++;
	}
	
	public boolean getTourIA() {
		return tourIA;
	}
	
	public void ajoutPossibilite(CoupleCaesInsecte e) {
		this.possibilite.add(e);
	}
	
	public boolean existePossibilite(CoupleCaesInsecte e) {
		for(int i=0;i<this.possibilite.size();i++) {
			if(this.possibilite.get(i).equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<CoupleCaesInsecte> getPossiblilites(){
		return possibilite;
	}
	
	public int getNbTourIA() {
		return nbTourIA;
	}
	
	public int getNbTourAdverse() {
		return nbTourAdverse;
	}
	
}
