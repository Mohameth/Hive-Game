package Controleur;

import Modele.Insectes.*;
import Modele.Case;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurIA;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;
import Vue.Vue;
import java.util.ArrayList;
import java.util.HashMap;

public class Hive {

    Plateau plateau;
    Joueur joueur1;
    Joueur joueur2;
    Joueur joueurCourant;

    public Hive(String[] args) {
        this.plateau = new Plateau();
        Vue.initFenetre(args, this);
    }

    public void setJoueurs(int cas, boolean extension) { //Création des joueurs selon le type de partie 
        switch (cas) {
            case 1:
                this.joueur1 = new JoueurHumain(this.plateau, extension);
                this.joueur2 = new JoueurHumain(this.plateau, extension);
                break;
            case 2:
                this.joueur1 = new JoueurHumain(this.plateau, extension);
                this.joueur2 = new JoueurIA(this.plateau, 1, extension, joueur1); //Easy
                break;
            case 3:
                this.joueur1 = new JoueurHumain(this.plateau, extension);
                this.joueur2 = new JoueurIA(this.plateau, 2, extension, joueur1); //Medium
                break;
            case 4:
                this.joueur1 = new JoueurHumain(this.plateau, extension);
                this.joueur2 = new JoueurIA(this.plateau, 3, extension, joueur1); //hard
                break;
        }
        this.joueurCourant = this.joueur1;
    }

    public boolean appartient(Point3DH caseCible) { //permet de savoir si l'insecte le plus haut d'une case appartient au joueur dont c'est le tour
        if (plateau.getCase(caseCible).getInsecteOnTop().getJoueur().equals(joueurCourant)) {
            return true;
        }

        return false;
    }

    public void selectionInsecte(Point3DH caseCible) { //Je sais plus ce qu'elle fait
        plateau.getCase(caseCible).getInsecteOnTop().deplacementPossible(plateau);
    }

    public boolean deplacementInsecte(Point3DH origine, Point3DH cible) {
        if (!plateau.getCase(origine).estVide() && appartient(origine)) {
            joueurCourant.coup(plateau.getCase(origine).getInsecteOnTop(), cible);
            this.joueurSuivant();
        }
        return true;
    }

    public ArrayList<Point3DH> deplacementsPossibles(Point3DH insecte) { //Pour un insecte défini par son emplacement retourne ses déplacements possibles
        ArrayList<Point3DH> res = new ArrayList<>();
        if (this.plateau.getCase(insecte).getInsecteOnTop().getJoueur().equals(this.joueurCourant)) {
            ArrayList<Case> cases = (ArrayList) this.plateau.getCase(insecte).getInsecteOnTop().deplacementPossible(plateau);
            for (Case c : cases) {
                if (!res.contains(c.getCoordonnees()))
                    res.add(c.getCoordonnees());
            }
        } 
        return res;
    }
    
    public boolean pionsDeplaceables() { //Booleen permettant à la vue de savoir si les pions du plateau peuvent se déplacer ou non
        return this.joueurCourant.reinePosee();
    }
    
    public ArrayList<Point3DH> placementsPossibles() { //Liste des cases pouvant accueilir un insecte du joueur courant
        return this.plateau.casesVidePlacement(this.joueurCourant);
    }

    public void joueurPlaceInsecte(TypeInsecte insecte, Point3DH cible) { //Placement d'un insecte sur la case cible du plateau par le joueur courant
        int i = 0;
        Insecte ins = null;

        do {
            ins = this.joueurCourant.pionsEnMain().get(i);
            i++;
        } while (ins.getType() != insecte);

        this.joueurCourant.placementInsecte(ins, cible);
        this.joueurSuivant();
    }

    public ArrayList<Insecte> mainJoueur(int joueur) { // Récupère la main du joueur choisi
        Joueur j = null;
        switch (joueur) {
            case (1):
                j = this.joueur1;
                break;
            case (2):
                j = this.joueur2;
                break;
        }
        return j.pionsEnMain();
    }

    public boolean tousPionsPosables(int joueur) { //Permet de toujours vérifier la règle qui empêche les joueurs de jouer s'ils
        Joueur j = null;                           // n'ont pas encore joué leur reine au 4ème tour
        switch (joueur) {
            case (1):
                j = this.joueur1;
                break;
            case (2):
                j = this.joueur2;
                break;
        }
        return j.tousPionsPosables();
    }

    public ArrayList<Insecte> mainsInit() { // Récupération de la main de base, en fonction du choix des joueurs de jouer avec l'extension ou non
        return this.joueur1.pionsEnMain();
    }

    public void reset() { // Quand on veut rejouer on réinitialise entièrement le jeu
        this.plateau = new Plateau();
        this.joueur1 = null;
        this.joueur2 = null;
        this.joueurCourant = null;
    }

    public boolean tourJoueurBlanc() { //Permet à la vue de savoir à quel joueur peut jouer
        if (this.joueurCourant == this.joueur1) {
            return true;
        }

        return false;
    }

    private void joueurSuivant() { //Passe au joueur suivant
        if (joueurCourant.equals(this.joueur1)) {
            this.joueurCourant = this.joueur2;
        } else if (joueurCourant.equals(this.joueur2)) {
            this.joueurCourant = this.joueur1;
        }
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public int tourJoueur(int joueur) { //Permet de savoir combien de coups chaque joueur à joué
        Joueur j = null;
        switch (joueur) {
            case (1):
                j = this.joueur1;
                break;
            case (2):
                j = this.joueur2;
                break;
        }
        return j.getTourJoueur();
    }

}
