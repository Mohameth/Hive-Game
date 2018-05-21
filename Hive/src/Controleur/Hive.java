package Controleur;

import Modele.Insectes.*;
import Modele.Case;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurIA;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.TypeInsecte;
import Vue.Vue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Hive implements Serializable {

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

    public boolean insecteAppartientJCourant(HexaPoint caseCible) { //permet de savoir si l'insecte le plus haut d'une case appartient au joueur dont c'est le tour
        Case c = plateau.getCase(caseCible);
        try {
            if (c == null)
                throw new Exception("Case inexistante");
            if (c.getInsecteOnTop().getJoueur().equals(joueurCourant))
                return true;
        } catch (Exception ex) {
            System.err.println("Erreur appartient : " + ex);
        }
        return false;
    }

    public void selectionInsecte(HexaPoint caseCible) { //Je sais plus ce qu'elle fait
        plateau.getCase(caseCible).getInsecteOnTop().deplacementPossible(plateau);
    }

    public boolean deplacementInsecte(HexaPoint origine, HexaPoint cible) {
        if (!plateau.getCase(origine).estVide() && insecteAppartientJCourant(origine)) {
            joueurCourant.coup(plateau.getCase(origine).getInsecteOnTop(), cible);
            this.joueurSuivant();
        }
        return true;
    }

    public ArrayList<HexaPoint> deplacementsPossibles(HexaPoint insecte) { //Pour un insecte défini par son emplacement retourne ses déplacements possibles
        ArrayList<HexaPoint> res = new ArrayList<>();
        if (this.plateau.getCase(insecte).getInsecteOnTop().getJoueur().equals(this.joueurCourant)) {
            ArrayList<Case> cases = (ArrayList) this.plateau.getCase(insecte).getInsecteOnTop().deplacementPossible(plateau);
            for (Case c : cases) {
                if (!res.contains(c.getCoordonnees())) {
                    res.add(c.getCoordonnees());
                }
            }
        }
        return res;
    }

    public boolean pionsDeplaceables() { //Booleen permettant à la vue de savoir si les pions du plateau peuvent se déplacer ou non
        return this.joueurCourant.reinePosee();
    }
    
    public ArrayList<HexaPoint> placementsPossibles() { //Liste des cases pouvant accueilir un insecte du joueur courant
        return this.plateau.casesVidePlacement(this.joueurCourant);
    }

    public void joueurPlaceInsecte(TypeInsecte insecte, HexaPoint cible) { //Placement d'un insecte sur la case cible du plateau par le joueur courant
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

    public boolean save(String name) {
        String path = System.getProperty("user.dir").concat("/rsc/SAVE/");
        File f = new File(path + name + ".txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(this);
            oos.close();
        } catch (IOException exception) {
            System.out.println("Erreur lors de l'écriture : " + exception.getMessage());
        }
        return true;
    }

    public boolean load(String name) {
        File f = new File(name + ".txt");
        if (f.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Hive h = (Hive) ois.readObject();
                ois.close();
                this.reset();
                this.joueur1 = h.joueur1;
                this.joueur2 = h.joueur2;
                this.joueurCourant = h.joueurCourant;
                
                this.plateau.setCases(h.plateau.getCases());
                this.plateau.setNbPionsEnJeu(h.plateau.getNbPionsEnJeu());
                
                //TODO : dire à la vue d'afficher le nouveaux plateau

            } catch (ClassNotFoundException exception) {
                System.out.println("Impossible de lire l'objet : " + exception.getMessage());
            } catch (IOException exception) {
                System.out.println("Erreur lors de l'écriture : " + exception.getMessage());
            }
        }
        return true;
    }
}
