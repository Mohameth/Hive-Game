package Modele;

import Modele.Insectes.Insecte;

public class CoupleCaesInsecte {

	private Insecte insecte;
	private Case c;
	
	public CoupleCaesInsecte(Insecte insecte,Case c) {
		this.insecte=insecte;
		this.c=c;
	}
	
	public Insecte getInsecte() {
		return insecte;
	}
	
	public Case getCase() {
		return c;
	}
	
	public boolean equals(CoupleCaesInsecte e) {
		return insecte.equalsType(e.insecte) && c.equals(e.c);
	}
	
}
