package Modele;

import Modele.Insectes.Insecte;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * Plateau décrit l'Etat du plateau de jeu et les actions disponible pour le
 * modifier.
 *
 * @author GRP3
 *
 */
public class Plateau implements Observable, Serializable {

    /**
     * ensemble des cases du plateau, évolue de façon dynamique
     *
     * @see Case
     * @see Point3DH
     */
    private Map<Point3DH, Case> cases;
    private int nbPionsEnJeu;
    private Observateur observateur;

    /**
     * construit un plateau avec une seul case en 0,0,0
     */
    public Plateau() {
        cases = new HashMap<Point3DH, Case>();
        Point3DH origine = new Point3DH(0, 0, 0);
        cases.put(origine, new Case(origine));

        this.nbPionsEnJeu = 0; //Peut-être à remplacer par une méthode
    }

    /**
     * renvoi la case demander, elle est crée si elle n'existe pas
     *
     * @param point point du plateau
     * @return la case qui se trouve au coordonées du point
     */
    public Case getCase(Point3DH point) {
        try {    
            if (!this.cases.containsKey(point))
                throw new Exception("Case" + point.toString() + "inexistante");
        } catch (Exception ex) {
            System.err.println("Erreur getCase : " + ex);
        }
        return cases.get(point);
    }

    public void setCases(Map<Point3DH, Case> cases) {
        this.cases = cases;
    }

    public void setNbPionsEnJeu(int nbPionsEnJeu) {
        this.nbPionsEnJeu = nbPionsEnJeu;
    }

    public Map<Point3DH, Case> getCases() {
        return cases;
    }

    public int getNbPionsEnJeu() {
        return nbPionsEnJeu;
    }

    
    public Plateau clone(ArrayList<Insecte> EnmainIA, ArrayList<Insecte> EnjeuIA,
            ArrayList<Insecte> EnmainAdverse, ArrayList<Insecte> EnjeuAdverse, boolean b) {
        Plateau plateau = new Plateau();
        plateau.cases = cloneCases(EnmainIA, EnjeuIA, EnmainAdverse, EnjeuAdverse, b);
        plateau.nbPionsEnJeu = this.nbPionsEnJeu;
        return plateau;
    }

    public Map<Point3DH, Case> cloneCases(ArrayList<Insecte> Enmain, ArrayList<Insecte> Enjeu,
            ArrayList<Insecte> EnmainAdverse, ArrayList<Insecte> EnjeuAdverse, boolean b) {
        HashMap<Point3DH, Case> cases2 = new HashMap<>();

        for (Map.Entry<Point3DH, Case> e : cases.entrySet()) {
            Point3DH p = e.getKey().clone();
            cases2.put(p, e.getValue().clone(p, Enmain, Enjeu, EnmainAdverse, EnjeuAdverse, b));
        }
        return cases2;
    }

    /**
     * crée les cases voisines de la case origine et les ajoutent dans le
     * plateau
     *
     * @param origine case d'origine
     */
    public void ajoutCasesVoisines(Point3DH origine) {
        for (Point3DH p : origine.coordonneesVoisins()) {
            if (!this.cases.containsKey(p)) {
                this.cases.put(p, new Case(p));
            }
        }
    }

    /**
     * ajoute un insecte à la position donné
     *
     * @param insecte insecte à ajouter
     * @param position coordonées de la case où ajouter l'insecte
     */
    public void ajoutInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).addInsecte(insecte);
            this.nbPionsEnJeu++;
            this.ajoutCasesVoisines(position);
        } catch (Exception ex) {
            System.err.println("Erreur ajout : " + ex);
        }
    }

    /**
     * ajoute un insecte à la position donné
     *
     * @param insecte insecte à ajouter
     * @param position coordonées de la case où ajouter l'insecte
     */
    public void deplaceInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).addInsecte(insecte);
            this.ajoutCasesVoisines(position);
        } catch (Exception ex) {
            System.err.println("Erreur ajout : " + ex);
        }
    }

    /**
     * indique si la ruche est vide
     *
     * @return true si la ruche est vide false sinon
     */
    public boolean rucheVide() {
        for (Case c : this.cases.values()) {
            if (!c.estVide()) {
                return false;
            }
        }
        return true;
    }

    /**
     * indique si la ruche a un seul insecte
     *
     * @return true si la ruche a un seul insecte false sinon
     */
    public boolean rucheAUnSeulInsecte() {
        int nbNonVide = 0;
        for (Case c : this.cases.values()) {
            if (!c.estVide()) {
                nbNonVide++;
            }
        }
        return nbNonVide == 1;
    }

    /**
     * supprime un insecte
     *
     * @param insecte insecte à supprimer
     * @param position coordonées de la case où suprimer l'insecte
     */
    public void deleteInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).removeInsecte();
        } catch (Exception ex) {
            System.err.println("Erreur delete : " + ex);
        }
    }

    /**
     * Donne toute les cases voisines
     *
     * @param c case d'origine
     * @param exclureCaseOccupee si true donne uniquement les cases vide si
     * false donne toute les cases
     * @return une Collection generique contenant les cases voisine de c
     */
    public Collection<Case> getCasesVoisines(Case c, boolean exclureCaseOccupee) {
        ArrayList<Case> voisins = new ArrayList<>();
        for (Point3DH pointCourant : c.getCoordonnees().coordonneesVoisins()) {
            Case voisin = getCase(pointCourant);
            if (voisin != null && (voisin.estVide() || !exclureCaseOccupee)) {
                voisins.add(voisin); //Case vide
            }
        }

        return voisins;
    }

    /**
     * Indique si i1 et i2 sont voisins
     *
     * @param i1 insecte i1
     * @param i2 insecte i2
     * @return true si i2 est dans le voisinage de i1
     */
    public boolean estVoisin(Insecte i1, Insecte i2) {
        return getCasesVoisinesOccupees(i1.getEmplacement()).contains(i2.getEmplacement());
    }

    /**
     * Donne toute les cases occupées du plateau
     *
     * @return un ArrayList des cases occupées
     */
    public ArrayList<Case> occupees() {
        ArrayList<Case> res = new ArrayList<>();
        Iterator<Case> it = this.cases.values().iterator();
        while (it.hasNext()) {
            Case c = it.next();
            if (!c.estVide()) {
                res.add(c);
            }
        }
        return res;
    }

    /**
     * Donne les cases où le joueur j peut placer un pions sur le plateau
     *
     * @param j Joueur voulant placer un pions
     * @return liste des cases vide sur les quels le joueur j peut placer un
     * pions
     */
    public ArrayList<Point3DH> casesVidePlacement(Joueur j) {
        ArrayList<Point3DH> res = new ArrayList<>();
        if (this.rucheVide()) {
            res.add(new Point3DH(0, 0, 0));
            return res;
        } else if (this.rucheAUnSeulInsecte()) {
            res.addAll(new Point3DH(0, 0, 0).coordonneesVoisins());
            return res;
        }
        Iterator<Case> it = this.cases.values().iterator();
        boolean joueurAdverse = false;
        while (it.hasNext()) {
            Case c = it.next();
            ArrayList<Case> voisins = (ArrayList<Case>) this.getCasesVoisinesOccupees(c);
            if (c.estVide() && !voisins.isEmpty()) {
                joueurAdverse = false;
                Iterator<Case> itv = voisins.iterator();
                while (!joueurAdverse && itv.hasNext()) {
                    if (!itv.next().getInsecteOnTop().getJoueur().equals(j)) {
                        joueurAdverse = true;
                    }
                }
                if (!joueurAdverse && !res.contains(c.getCoordonnees())) {
                    res.add(c.getCoordonnees());
                }
            }
        }
        return res;
    }

    public ArrayList<Case> pointVersCase(ArrayList<Point3DH> p) {
        ArrayList<Case> c = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            c.add(this.getCase(p.get(i)));
        }
        return c;
    }

    /**
     * Donne toute les cases occupées voisine de c
     *
     * @param c case d'origine
     * @return une Collection generique contenant les cases occupées voisine de
     * c
     */
    public Collection<Case> getCasesVoisinesOccupees(Case c) {
        ArrayList<Case> voisins = new ArrayList<>();
        for (Point3DH pointCourant : c.getCoordonnees().coordonneesVoisins()) {
            Case voisin = this.getCase(pointCourant);
            if (voisin != null && !voisin.estVide()) {
                voisins.add(voisin);
            }
        }

        return voisins;
    }

    /**
     * Donne les cases voisine de c qui sont accessible pour un insecte (sans
     * porte)
     *
     * @param c case de départ
     * @param exclureCaseOccupee si vraie on enleve les cases occupee par un
     * insecte si faux on donne toute les cases accessibles
     * @return la liste des cases accessibles
     */
    public Collection<Case> getCasesVoisinesAccessibles(Case c, boolean exclureCaseOccupee) {
        Collection<Case> dep = this.getCasesVoisines(c, exclureCaseOccupee);
        Iterator<Case> it = dep.iterator();
        while (it.hasNext()) {
            Case voisin = it.next();
            if (!this.glissementPossible(c, voisin)) {
                it.remove();
            }
        }

        return dep;
    }

    /**
     * Test du glissement à partir de la case c1 vers c2
     *
     * @param c1 case initiale
     * @param c2 case destination
     * @return true si le glissement de c1 à c2 est possible sans casser la
     * ruche
     */
    public boolean glissementPossible(Case c1, Case c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        int nombreCasesAdjacentesNonVide = 0;
        Collection<Case> voisinsC1 = getCasesVoisines(c1, false);
        voisinsC1.remove(c2);
        Collection<Case> voisinsC2 = getCasesVoisines(c2, false);
        voisinsC2.remove(c1);

        for (Case voisinCourant : voisinsC1) {
            if (voisinsC2.contains(voisinCourant)) {//Voisin en commun de c1 et c2
                if (!voisinCourant.estVide() && voisinCourant.getNbInsectes() >= c1.getNbInsectes()) {
                    nombreCasesAdjacentesNonVide++;
                }
            }
        }

        return nombreCasesAdjacentesNonVide == 1 && !rucheBrisee(c1, c2);
    }

    public boolean gateBetween(Case c1, Case c2) {
        int nombreCasesAdjacentesNonVide = 0;
        Collection<Case> voisinsC1 = getCasesVoisines(c1, false);
        voisinsC1.remove(c2);
        Collection<Case> voisinsC2 = getCasesVoisines(c2, false);
        voisinsC2.remove(c1);

        for (Case v1 : voisinsC1) {
            if (voisinsC2.contains(v1)) {
                if (!v1.estVide()) {
                    nombreCasesAdjacentesNonVide++;
                }
            }
        }

        return nombreCasesAdjacentesNonVide == 2;
    }

    /**
     * Test sur l'unicité de la ruche
     *
     * @param ghost case à ignorer
     * @param moveDest case à considérer
     * @return true si la ruche est brisée après le déplacement (ghost moveDest)
     * ou sans considérer de déplacement si ghost et moveDest valent null
     */
    public boolean rucheBrisee(Case ghost, Case moveDest) { //Tester aussi avec un compteur de changements
        if (this.rucheVide()) {
            return false;
        }

        int i = 0;
        ArrayList<Case> listeCases = new ArrayList<>(this.cases.values());
        if (ghost == null && moveDest == null) {
            do {
                moveDest = listeCases.get(i);
                i++;
            } while (i < this.cases.values().size() && (moveDest.estVide()));
        }

        //Sauvegarde du ghost et ajout de l'insecte sur moveDest (si moveDest != null)
        Insecte ghostBug = null;
        if (ghost != null) {
            ghost = this.getCase(ghost.getCoordonnees());
            ghostBug = ghost.getInsecteOnTop();
            if (ghostBug != null) {
                try {
                    ghost.removeInsecte();
                    if (moveDest != null) {
                        moveDest.addInsecte(ghostBug);
                    }
                } catch (Exception e) {
                    System.err.println("ERREUR Ruche brisé debut : " + e);
                }
            }
        }

        ArrayList<Case> visites = new ArrayList<>();
        int nbInsectesVisites = moveDest.getNbInsectes();
        LinkedList<Case> file = new LinkedList<>();
        visites.add(moveDest);
        file.add(moveDest);
        while (!file.isEmpty()) {
            Case courante = file.pollFirst();
            ArrayList<Case> voisins = (ArrayList<Case>) getCasesVoisinesOccupees(courante);
            for (Case caseC : voisins) {
                if (!visites.contains(caseC)) {
                    visites.add(caseC);
                    nbInsectesVisites += caseC.getNbInsectes();
                    file.addLast(caseC);
                }

            }
        }

        //Restauration du ghost et de moveDest
        if (ghost != null) {
            if (ghostBug != null) {
                try {
                    if (moveDest != null) {
                        moveDest.removeInsecte();
                    }
                    ghost.addInsecte(ghostBug);
                } catch (Exception e) {
                    System.err.println("ERREUR Ruche brisé fin : " + e);
                }
            }
        }

        return nbInsectesVisites != this.nbPionsEnJeu;
    }

    public boolean rucheBrisee2(Case ghost) { //Tester aussi avec un compteur de changements
        if (this.rucheVide()) {
            return false;
        }
        ArrayList<Case> caseOccupe;
        if (ghost == null) {
            ghost = this.cases.get(new Point3DH(0, 0, 0));
        }

        if (ghost.estVide()) {
            caseOccupe = (ArrayList<Case>) this.getCasesVoisinesOccupees(ghost);
        } else {
            caseOccupe = new ArrayList<>();
            caseOccupe.add(ghost);
        }

        ArrayList<Case> caseOccupe2 = new ArrayList<>();
        if (!caseOccupe.isEmpty()) {
            caseOccupe2.add(caseOccupe.get(0));
        }
        caseOccupe.clear();

        while (!caseOccupe2.isEmpty()) {
            Case c = caseOccupe2.get(0);
            caseOccupe2.remove(c);
            if (!caseOccupe.contains(c)) {
                caseOccupe.add(c);
            }
            ArrayList<Case> caseVoisines = (ArrayList<Case>) this.getCasesVoisinesOccupees(c);
            for (int i = 0; i < caseVoisines.size(); i++) {
                if (!caseOccupe.contains(caseVoisines.get(i)) && !caseVoisines.get(i).equals(ghost)) {
                    caseOccupe2.add(caseVoisines.get(i));
                }
            }

        }

        return caseOccupe.size() != this.nbPionsEnJeu;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.cases);
        return hash;
    }

    @Override
    public void addObserver(Observateur newobserver) {
        this.observateur = newobserver;
    }

    @Override
    public void notifyListeners() {
        this.observateur.coupJoue(this);
    }

    public void afficherGrille() {
        for (Case c : this.cases.values()) {
            if (!c.estVide()) {
                System.out.println(c.toString());
            }
        }
    }
}
