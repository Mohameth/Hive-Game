package Modele;

import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import java.util.*;

public abstract class Joueur {

    protected ArrayList<Insecte> pions;
    protected Plateau plateau;

    public abstract boolean coup(Insecte insecte, Point3DH cible); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)


    public Joueur(Plateau p) {
            this.plateau = p;
            this.pions = new ArrayList<>(); //On rentrera tous les pions ici
    }

    public boolean reinePosee() {
        Insecte reine; int i = 0;
        do {
            reine = this.pions.get(i);
            i++;
        } while(i < this.pions.size() && (!(reine instanceof Reine)));
        if (reine.getEmplacement() == null)
            return false;
        
        return true;
    }
        
    public ArrayList<Insecte> pionsEnMain() {
        ArrayList<Insecte> res = new ArrayList<>();
        for (Insecte ins : this.pions) {
            if (ins.getEmplacement() == null) {
                res.add(ins);
            }
        }
        return res;
    }   

}

