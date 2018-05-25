package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Scarabee extends Insecte {

    public Scarabee(Joueur j) {
        super(j);
    }
	
	public Insecte clone() {
    	return new Scarabee(this.getJoueur());
    }
    
    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) {
        
        if (!this.getJoueur().reinePosee() || (this.getEmplacement().getNbInsectes() != 1 && 
                !this.getEmplacement().getInsecteOnTop().equals(this))) return new ArrayList<>();
        
        ArrayList<Case> res = new ArrayList<>();
        for (Case c : plateau.getCasesVoisines(this.getEmplacement(), false)) {
            if (plateau.glissementPossible(this.getEmplacement(), c) || 
                    (!plateau.rucheBrisee(this.getEmplacement(), c) && (this.getNiveau() > 1 || c.getNbInsectes() != 0))) {
                res.add(c);
            }
        }
        
        Iterator<Case> it = res.iterator();
        while (it.hasNext()) {
            Case cible = it.next();
            if (plateau.rucheBrisee(this.getEmplacement(), cible)) {
                it.remove();
            }
        }
        
        return res;
    }

    @Override
    public TypeInsecte getType() {
        return TypeInsecte.SCARABEE;
    }

    @Override
    public String toString() {
        return "Scarabee";
    }
}