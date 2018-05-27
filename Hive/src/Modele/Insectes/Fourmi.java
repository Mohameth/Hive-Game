package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Fourmi extends Insecte {

    public Fourmi(Joueur j) {
        super(j);
    }
	
	public Insecte clone() {
    	return new Fourmi(this.getJoueur());
    	
    }
	
	public boolean equals(Insecte insecte) {
		return (insecte instanceof Fourmi);
	}
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        if (!this.getJoueur().reinePosee() || this.getEmplacement().getNbInsectes() != 1) return new ArrayList<>();
        ArrayList<Case> result = new ArrayList<>();
        LinkedList<Case> toCheck = new LinkedList<>();
        
        result.addAll(plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true));
        toCheck.addAll(result);
        while (!toCheck.isEmpty()) {
            Case courante = toCheck.removeLast();
            for (Case c : plateau.getCasesVoisinesAccessibles(courante, true)) {
                if (!result.contains(c)) {
                    ArrayList<Case> casec = (ArrayList<Case>) plateau.getCasesVoisinesOccupees(c);
                    if (!((casec.contains(this.getEmplacement())) && casec.size()==1)) {
                        result.add(c);
                        toCheck.add(c);
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.FOURMI;
    }
    
    @Override
    public String toString() {
        return "Fourmi";
    }

}