package Modele;

import Modele.Insectes.Insecte;

public class CoupleCaesInsecte {

	private Insecte insecte;
	private Case c;
	private Case c2;
	
	public CoupleCaesInsecte(Insecte insecte,Case c,Case c2) {
		this.insecte=insecte;
		this.c=c;
		this.c2=c2;
	}
	
	public Insecte getInsecte() {
		return insecte;
	}
	
	public Case getCase() {
		return c;
	}
	
	public Case getAncienneCase() {
		return c2;
	}
	
	public boolean equals(CoupleCaesInsecte e) {
		if(c2==null) {
			return e.c2==null && insecte.equals(e.insecte) && insecte.getJoueur()==e.getInsecte().getJoueur() && c.equals(e.c);
		}
		return c2.equals(e.c2) && insecte.equals(e.insecte) && insecte.getJoueur()==e.getInsecte().getJoueur() && c.equals(e.c);
	}
	
}
