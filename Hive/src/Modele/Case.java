package Modele;

import Modele.Insectes.Insecte;
import Modele.Insectes.Scarabee;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Case implements Serializable{

    private ArrayList<Insecte> insectes;
    private HexaPoint coordonnees;

    public Case(HexaPoint p, Insecte insecte) {
        this.insectes = new ArrayList<>();
        this.insectes.add(insecte);
        this.coordonnees = p;
    }
    
    public Case(HexaPoint p) {
        this.coordonnees = p;
        this.insectes = new ArrayList<>();
    }
	
	public Case clone(HexaPoint p,ArrayList<Insecte> EnmainIA,ArrayList<Insecte> EnjeuIA,
    		ArrayList<Insecte> EnmainAdverse,ArrayList<Insecte> EnjeuAdverse,boolean b){
    	Case c=new Case(p);
    	
    	for(int i=0;i<insectes.size();i++) {
    		Insecte in=this.insectes.get(i).clone();
    		c.insectes.add(in);
    		in.setEmplacement(c);
    		
    		if(b) {
    			if(in.getEmplacement()==null) {
        			EnmainIA.add(in);
        		}else {
        			EnjeuIA.add(in);
        		}
    		}else {
    			if(in.getEmplacement()==null) {
    				EnmainAdverse.add(in);
        		}else {
        			EnjeuAdverse.add(in);
        		}
    		}
    		
    	}
    	return c;
    }
    
    public ArrayList<Insecte> getInsectesClone(){
    	ArrayList<Insecte> liste=new ArrayList<>();
    	
    	for(int i=0;i<this.insectes.size();i++) {
    		liste.add(this.insectes.get(i).clone());
    	}
    	return liste;
    }

    public boolean estVide() {
        return this.insectes.isEmpty();
    }

    public void addInsecte(Insecte insecte) throws Exception {
        if (!this.estVide() && !((insecte.getType() == TypeInsecte.SCARABEE) || (insecte.getType() == TypeInsecte.MOUSTIQUE)))
                throw new Exception("Ajout impossible sur case non vide");
        if (this.insectes.size() == 7) throw new Exception("Ajout impossible -> 7 insectes maximum");
        this.insectes.add(insecte);
        insecte.setEmplacement(this);
    }

    
    public void removeInsecte() throws Exception{
        if (this.estVide()) throw new Exception("Retrait impossible sur case vide");
        
        Insecte i = this.insectes.get(this.insectes.size()-1);
        this.insectes.remove(i);
    }
    
    public boolean insecteBloque(Insecte insecte) throws Exception {
        if (this.estVide()) throw new Exception("Case vide");
        
        
        if (!this.insectes.contains(insecte))
            throw new Exception("Cet insecte n'est pas sur cette case");
        
        if(this.insectes.contains(insecte) && !this.getInsecteOnTop().equals(insecte))
            return true;
        
        return false;
    }

    public ArrayList<Insecte> getInsectes() {
        return insectes;
    }
    
    public Insecte getInsecteOnTop() {
        if (this.insectes.isEmpty()) return null;
        return this.insectes.get(this.insectes.size()-1);
    }
    
    public Integer getNbInsectes() {
        return insectes.size();
    }

    public HexaPoint getCoordonnees() {
        return coordonnees;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return ((Case) obj).getCoordonnees().equals(this.getCoordonnees());
    }

    /*@Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.insectes);
        hash = 71 * hash + Objects.hashCode(this.coordonnees);
        return hash;
    }*/
    
    public boolean estVoisinHaut(Case c) {
    	return this.getCoordonnees().voisinHaut().equals(c.getCoordonnees());
    }
    
    public boolean estVoisinDroiteHaut(Case c) {
    	return this.getCoordonnees().voisinDroiteHaut().equals(c.getCoordonnees());
    }
    
    public boolean estVoisinDroiteBas(Case c) {
    	return this.getCoordonnees().voisinDroiteBas().equals(c.getCoordonnees());
    }
    
    public boolean estVoisinBas(Case c) {
    	return this.getCoordonnees().voisinBas().equals(c.getCoordonnees());
    }
    
    public boolean estVoisinGaucheBas(Case c) {
    	return this.getCoordonnees().voisinGaucheBas().equals(c.getCoordonnees());
    }
    
    public boolean estVoisinGaucheHaut(Case c) {
    	return this.getCoordonnees().voisinGaucheHaut().equals(c.getCoordonnees());
    }
    
    @Override
    public String toString() {
        String res = "Case[" + coordonnees + ']' + ":\n";
        for (Insecte i : this.insectes) {
            res += "\t " + i + "\n";
        }
        return res;
    }

    public void setInsectes(ArrayList<Insecte> insectes) {
        this.insectes = insectes;
    }
    
}