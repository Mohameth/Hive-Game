package Controleur;

import Modele.Insectes.*;
import Modele.Case;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurHumain;
import Modele.Joueurs.JoueurIA;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.IA.IAMinimax;
import Modele.Joueurs.NumJoueur;
import Modele.Insectes.TypeInsecte;
import Vue.Vue;
import static com.sun.javafx.PlatformUtil.isWindows;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hive implements Serializable {

    Plateau plateau;
    public Joueur joueur1;
    public Joueur joueur2;
    Joueur joueurCourant;
    boolean Undo = true;
    transient Observer o;

    public Hive(String[] args) {
        this.plateau = new Plateau();
        Vue.initFenetre(args, this);
    }

    public void setJoueurs(int cas, boolean extension) { //Création des joueurs selon le type de partie
        if (cas > 0 && cas < 5) {
            switch (cas) {
                case 1:
                    this.joueur1 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR1);
                    this.joueur2 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR2);
                    break;
                case 2:
                    this.joueur1 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR1);
                    this.joueur2 = new JoueurIA(this.plateau, 1, extension, NumJoueur.IAFACILE2, joueur1); //Easy
                    break;
                case 3:
                    this.joueur1 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR1);
                    this.joueur2 = new JoueurIA(this.plateau, 2, extension, NumJoueur.IAMOYEN2, joueur1); //Medium
                    break;
                case 4:
                    this.joueur1 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR1);
                    this.joueur2 = new JoueurIA(this.plateau, 3, extension, NumJoueur.IADIFFICILE2, joueur1); //hard
                    break;
            }
        } else if (cas < 8) {
            switch (cas) {
                case 5:
                    this.joueur2 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR2);
                    this.joueur1 = new JoueurIA(this.plateau, 1, extension, NumJoueur.IAFACILE1, joueur2);
                    break;
                case 6:
                    this.joueur2 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR2);
                    this.joueur1 = new JoueurIA(this.plateau, 2, extension, NumJoueur.IAMOYEN1, joueur2); //Easy
                    break;
                case 7:
                    this.joueur2 = new JoueurHumain(this.plateau, extension, NumJoueur.JOUEUR2);
                    this.joueur1 = new JoueurIA(this.plateau, 3, extension, NumJoueur.IADIFFICILE1, joueur2); //Medium

                    break;
            }
        } else {
            try {
                throw new Exception("Mauvaise difficulte");
            } catch (Exception ex) {
                System.err.println("ERREUR setJoueurs : " + ex);
            }
        }
        this.joueurCourant = this.joueur1;
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public boolean insecteAppartientJCourant(HexaPoint caseCible) { //permet de savoir si l'insecte le plus haut d'une case appartient au joueur dont c'est le tour
        Case c = plateau.getCase(caseCible);
        try {
            if (c == null) {
                throw new Exception("Case inexistante");
            }
            if (c.getInsecteOnTop().getJoueur().equals(joueurCourant)) {
                return true;
            }
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

    public ArrayList<Insecte> mainJoueur(NumJoueur numJoueur) { // Récupère la main du joueur choisi
        Joueur j = null;
        switch (numJoueur) {
            case JOUEUR1:
                j = this.joueur1;
                break;
            case JOUEUR2:
                j = this.joueur2;
                break;
        }
        return j.pionsEnMain();
    }

    public boolean tousPionsPosables(NumJoueur numJoueur) { //Permet de toujours vérifier la règle qui empêche les joueurs de jouer s'ils
        Joueur j = null;                           // n'ont pas encore joué leur reine au 4ème tour
        switch (numJoueur) {
            case JOUEUR1:
                j = this.joueur1;
                break;
            case JOUEUR2:
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

    public void joueurSuivant() { //Passe au joueur suivant
        if (this.joueurCourant.getNumJoueur().estHumain()) {
            this.plateau.notifieVue(-1);
        }

        if (joueurCourant.equals(this.joueur1)) {
            this.joueurCourant = this.joueur2;
        } else if (joueurCourant.equals(this.joueur2)) {
            this.joueurCourant = this.joueur1;
        }
        if (!this.joueurCourant.getNumJoueur().estHumain()) {
            ((JoueurIA) this.joueurCourant).coup(null, null);
        }

    }

    public int JoueurGagnant() {
        if (this.joueur1.reineBloquee()) {
            return 2;
        } else if (this.joueur2.reineBloquee()) {
            return 1;
        } else {
            return 0;
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

    public Joueur getJoueur(NumJoueur j) {
        if (j.estBlanc()) {
            return this.joueur1;
        }
        return this.joueur2;
    }

    public boolean save(String name) {
        String path;
        if (isWindows()) {
            path = System.getProperty("user.dir").concat("\\rsc\\SAVE\\");
        } else {
            path = System.getProperty("user.dir").concat("/rsc/SAVE/");
        }
        File f = new File(path + name);
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
        String path;
        if (isWindows()) {
            path = System.getProperty("user.dir").concat("\\rsc\\SAVE\\");
        } else {
            path = System.getProperty("user.dir").concat("/rsc/SAVE/");
        }
        File f = new File(path + name);
        if (f.exists()) {
            try {
                System.out.println("load");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Hive h = (Hive) ois.readObject();
                ois.close();
                this.reset();
                this.joueur1 = h.joueur1;
                this.joueur2 = h.joueur2;
                this.joueurCourant = h.joueurCourant;
                this.plateau = h.plateau;
                //this.plateau.setCases(h.plateau.getCases());
                //this.plateau.setNbPionsEnJeu(h.plateau.getNbPionsEnJeu());
                this.addObserverPlateau(o);
                this.joueur1.setPlateau(plateau);
                this.joueur2.setPlateau(plateau);
                this.joueurCourant.setPlateau(plateau);
                this.Undo = h.Undo;

                this.plateau.notifieVue(-1);

            } catch (ClassNotFoundException exception) {
                System.out.println("Impossible de lire l'objet : " + exception.getMessage());
            } catch (IOException exception) {
                System.out.println("Erreur lors de l'écriture : " + exception.getMessage());
            }
        }
        return true;
    }

    public boolean UndoPossible() {
        if (Undo) {
            if (this.joueur2 instanceof JoueurIA) {
                return this.joueur1.UndoPossible();
            } else if (this.joueurCourant.equals(this.joueur1)) {
                return this.joueur2.UndoPossible();
            } else {
                return this.joueur1.UndoPossible();
            }
        } else {
            return false;
        }
    }

    public void Undo() {
        if (UndoPossible()) {
            if (this.joueur2 instanceof JoueurIA) {
                this.joueur2.Undo();
                this.joueur1.Undo();
            } else if (this.joueur1 instanceof JoueurIA) {
                this.joueur2.Undo();
                this.joueur1.Undo();
            } else if (this.joueurCourant.equals(this.joueur1)) {
                this.joueur2.Undo();
                this.joueurCourant = this.joueur2;
            } else {
                this.joueur1.Undo();
                this.joueurCourant = this.joueur1;
            }
            this.Undo = false;
            this.plateau.notifieVue(-1);
        }
    }

    public boolean RedoPossible() {
        if (this.joueur2 instanceof JoueurIA) {
            return this.joueur1.RedoPossible();
        } else if (this.joueurCourant.equals(this.joueur1)) {
            return this.joueur1.RedoPossible();
        } else {
            return this.joueur2.RedoPossible();
        }
    }

    public void Redo() {
        if (RedoPossible()) {
            if (this.joueur2 instanceof JoueurIA) {
                this.joueur1.Redo();
                this.joueur2.Redo();
            } else if (this.joueur1 instanceof JoueurIA) {
                this.joueur2.Redo();
                this.joueur1.Redo();
            } else if (this.joueurCourant.equals(this.joueur1)) {
                this.joueur1.Redo();
                this.joueurCourant = this.joueur2;
            } else {
                this.joueur2.Redo();
                this.joueurCourant = this.joueur1;
            }
            this.Undo = true;
            this.plateau.notifieVue(-1);
        }
    }

    public void addObserverPlateau(Observer o) {
        this.o = o;
        this.plateau.addObserver(o);
    }

    public void setUndo(boolean Undo) {
        this.Undo = Undo;
    }
}
