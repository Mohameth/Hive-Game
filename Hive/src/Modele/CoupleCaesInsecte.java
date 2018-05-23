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
		return insecte.equalsType(e.insecte) && c.equals(e.c);
	}
	
}
