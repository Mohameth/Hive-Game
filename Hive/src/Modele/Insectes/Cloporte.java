package Modele.Insectes;

import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.HexaPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Cloporte extends Insecte {
    HashMap<Insecte, Case> insectesADeplacer; //Maj dans deplacement possible, couple insecte-destination.
    public Cloporte(Joueur j) {
        super(j);
    }

	public Insecte clone() {
    	return new Cloporte(this.getJoueur());
    	
    }

    @Override
    public Collection<Case> deplacementPossible(Plateau plateau) { //Creer un HASh pour éviter de recalculer tout le temps la liste
        insectesADeplacer = new HashMap<>();
        if (!this.getJoueur().reinePosee()) return new ArrayList<>();
        Collection<Case> casesVoisinesLibre = plateau.getCasesVoisinesAccessibles(this.getEmplacement(), true);//Récup. des cases vides accessibles
        
        //Ajout des cases déplacables
        /*for (Case c : plateau.getCasesVoisinesOccupees(this.getEmplacement())) {
            for (Case r : casesVoisinesLibre) {
                if (plateau.glissementPossible(c, this.getEmplacement()) && plateau.glissementPossible(this.getEmplacement(), r)) {
                    insectesADeplacer.put(c.getInsecteOnTop(), r);
                }
            }
        }*/
        
        return casesVoisinesLibre;
    }


    @Override
    public TypeInsecte getType() {
        return TypeInsecte.CLOPORTE;
    }
    
    @Override
    public String toString() {
        return "Cloporte";
    }

    @Override
    public boolean equals(Insecte insecte) {
        return (insecte.getType() == TypeInsecte.CLOPORTE);
    }
    
}