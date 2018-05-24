package Modele.Joueurs;

import Controleur.Hive;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Insectes.*;
import Modele.Plateau;
import Modele.Insectes.TypeInsecte;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decrit le Joueur General, ses pions et action. Elle est specialisé en 2
 * classes, JoueurHumain et JoueurIA
 *
 * @see JoueurHumain
 * @see JoueurIA
 * @author GRP3
 */
public abstract class Joueur implements Cloneable, Serializable {

    /**
     * Pions du joueur
     *
     * @see Insecte
     */
    protected ArrayList<Insecte> pions;

    /**
     * Plateau au quel le joueur est lié
     *
     * @see Plateau
     */
    protected Plateau plateau;

    /**
     * Dernier insecte déplacé
     *
     * @see Insecte
     */
    protected Deplacement dernierDeplacement;

    protected int tourJoueur;

    
    protected NumJoueur numJoueur;
    /**
     * Coup d'un joueur
     *
     * @param insecte insecte déplacé
     * @param cible Case cible du déplacement
     * @return vrai si le coup est jouer faux sinon
     */
    public abstract boolean coup(Insecte insecte, HexaPoint cible); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)

    public Joueur(Plateau p, boolean extensions, NumJoueur numJoueur) {
        this.plateau = p;
        this.dernierDeplacement = null;
        this.pions = new ArrayList<>(); //On rentrera tous les pions ici
        this.tourJoueur = 1;
        this.initInsectes(extensions);
        this.numJoueur = numJoueur;
    }

    /**
     * Verifie si la reine du joueur est posé
     *
     * @return vrai si la reine est posé faux sinon
     */
    public boolean reinePosee() {
        Insecte reine;
        int i = 0;
        do {
            reine = this.pions.get(i);
            i++;
        } while (i < this.pions.size() && ((reine.getType() != TypeInsecte.REINE)));
        if (reine.getEmplacement() == null) {
            return false;
        }

        return true;
    }

    public boolean reineBloquee() {
        if (this.reinePosee()) {
            Insecte reine;
            int i = 0;
            do {
                reine = this.pions.get(i);
                i++;
            } while (i < this.pions.size() && ((reine.getType() != TypeInsecte.REINE)));

            return plateau.getCasesVoisinesOccupees(reine.getEmplacement()).size() == 6;
        } else {
            return false;
        }
    }
	
	public boolean reinePresqueBloquee() {
        if (this.reinePosee()) {
            Insecte reine;
            int i = 0;
            do {
                reine = this.pions.get(i);
                i++;
            } while (i < this.pions.size() && ((reine.getType() != TypeInsecte.REINE)));

            return plateau.getCasesVoisinesOccupees(reine.getEmplacement()).size() == 5;
        } else {
            return false;
        }
    }
    
    protected Reine getReine() {
    	ArrayList<Insecte> liste=this.pionsEnMain();
    	for(int i=0;i<liste.size();i++) {
    		if(liste.get(i) instanceof Reine) {
    			return (Reine) liste.get(i);
    		}
    	}
    	return null;
    }

    public NumJoueur getNumJoueur() {
        return numJoueur;
    }

    /**
     * Donne la liste des pions que le joueur à encore en main
     *
     * @return liste des pions en main
     */
    public ArrayList<Insecte> pionsEnMain() {
        ArrayList<Insecte> res = new ArrayList<>();
        for (Insecte ins : this.pions) {
            if (ins.getEmplacement() == null) {
                res.add(ins);
            }
        }
        return res;
    }
	
	public ArrayList<Insecte> pionsEnJeu() {
        ArrayList<Insecte> res = new ArrayList<>();
        for (Insecte ins : this.pions) {
            if (ins.getEmplacement() != null) {
                res.add(ins);
            }
        }
        return res;
    }

    public void setPions(ArrayList<Insecte> pions) {
        this.pions = pions;
    }

    
    /**
     * Place un insecte sur la case caseCible
     *
     * @param insecte insecte à placer
     * @param caseCible case où placer l'insecte
     */
    public void placementInsecte(Insecte insecte, HexaPoint caseCible) {
        try {
            this.plateau.ajoutInsecte(insecte, caseCible);
            this.dernierDeplacement = new Deplacement(insecte, null, caseCible);
            this.tourJoueur++;
        } catch (Exception ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * donne tout les pions du joueur
     *
     * @return liste des pions du joueur
     */
    public ArrayList<Insecte> getPions() {
        return pions;
    }

    /**
     * initialise les pions du joueur
     */
    private void initInsectes(boolean extensions) {

        this.pions.add(new Reine(this));
        for (int i = 0; i < 2; i++) {
            this.pions.add(new Scarabee(this));
            this.pions.add(new Araignee(this));
        }
        for (int i = 0; i < 3; i++) {
            this.pions.add(new Fourmi(this));
            this.pions.add(new Sauterelle(this));
        }
        if (extensions) {
            this.pions.add(new Moustique(this));
            //this.pions.add(new Cloporte(this));
            this.pions.add(new Coccinelle(this));
        }

    }

    public boolean tousPionsPosables() {
        if (!this.reinePosee() && this.tourJoueur == 4) {
            return false;
        }
        return true;
    }

    public int getTourJoueur() {
        return tourJoueur;
    }

    public void incrementeTour() {
        tourJoueur++;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    @Override
    public Joueur clone() {// Copie tous sauf le plateau (utiliser setPlateau)
        try {
            Joueur joueur = (Joueur) super.clone();
            //joueur.dernierDeplacement = this.dernierDeplacement.clone();
            joueur.pions = cloneList(pions, joueur);
            joueur.tourJoueur = this.tourJoueur;
            joueur.numJoueur = this.numJoueur;
            
            return joueur;
        } catch (CloneNotSupportedException e) {
            System.err.println("ERREUR Clone Joueur : " + e);
        }

        return null;
    }

    public ArrayList<Insecte> cloneList(ArrayList<Insecte> pions, Joueur j) {
        ArrayList<Insecte> clone = new ArrayList<>(pions.size());
        for (Insecte insecte : pions) {
            Insecte cloneInsecte = insecte.clone();
            cloneInsecte.setJoueur(j);;
            clone.add(cloneInsecte);
        }

        return clone;
    }

    public boolean UndoPossible() {
        if (this.dernierDeplacement == null) {
            return false;
        } else if (this.dernierDeplacement.getI().getEmplacement() == null) {
            return false;
        } else {
            return !this.dernierDeplacement.getI().getEmplacement().equals(this.dernierDeplacement.getOrig());
        }
    }

    public void Undo() {
        if (UndoPossible()) {
            if (this.dernierDeplacement.getOrig() == null) {
                this.plateau.deleteInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getCible());
                this.dernierDeplacement.getI().setEmplacement(null);
            } else {
                this.plateau.deleteInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getCible());
                this.plateau.deplaceInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getOrig());
            }
            this.tourJoueur--;
        }
    }

    public boolean RedoPossible() {
        if (this.dernierDeplacement == null) {
            return false;
        } else if (this.dernierDeplacement.getI().getEmplacement() == null) {
            return false;
        } else {
            return !this.dernierDeplacement.getI().getEmplacement().equals(this.dernierDeplacement.getCible());
        }
    }

    public void Redo() {
        if (RedoPossible()) {
            if (this.dernierDeplacement.getOrig() == null) {
                this.plateau.ajoutInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getCible());
            } else {
                this.plateau.deleteInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getOrig());
                this.plateau.deplaceInsecte(this.dernierDeplacement.getI(), this.dernierDeplacement.getCible());
            }
            this.tourJoueur++;
        }
    }
}
