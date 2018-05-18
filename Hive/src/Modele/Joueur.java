package Modele;

import Controleur.Hive;
import Modele.Insectes.*;
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
public abstract class Joueur {

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
    protected Insecte dernierDeplacement;

    /**
     * Coup d'un joueur
     *
     * @param insecte insecte déplacé
     * @param cible Case cible du déplacement
     * @return vrai si le coup est jouer faux sinon
     */
    public abstract boolean coup(Insecte insecte, Point3DH cible); //Joueur connait le plateau -> appelle déplacement sur insecte avec plateau (insect sait où il est)

    public Joueur(Plateau p, boolean extensions) {
        this.plateau = p;
        this.dernierDeplacement = null;
        this.pions = new ArrayList<>(); //On rentrera tous les pions ici

        this.initInsectes(extensions);
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
        } while (i < this.pions.size() && (!(reine instanceof Reine)));
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
            } while (i < this.pions.size() && (!(reine instanceof Reine)));

            return plateau.getCasesVoisinesOccupees(reine.getEmplacement()).size() == 6;
        } else {
            return false;
        }
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

    /**
     * Place un insecte sur la case caseCible
     *
     * @param insecte insecte à placer
     * @param caseCible case où placer l'insecte
     */
    public void placementInsecte(Insecte insecte, Point3DH caseCible) {
        try {
            //this.plateau.ajoutInsecte(insecte, caseCible.getCoordonnees());
            this.plateau.ajoutInsecte(insecte, caseCible);
            this.dernierDeplacement = insecte;
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
            this.pions.add(new Cloporte(this));
            this.pions.add(new Coccinelle(this));
        }

    }

}
