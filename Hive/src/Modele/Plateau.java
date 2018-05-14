package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Plateau décrit l'Etat du plateau de jeu et les actions disponible pour le modifier.
 * @author GRP3
 * 
 */

public class Plateau implements Observable {

    /**
     * ensemble des cases du plateau, évolue de façon dynamique
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
     * @param point point du plateau
     * @return la case qui se trouve au coordonées du point
     */
    public Case getCase(Point3DH point) {
        if (cases.get(point) == null) {
            cases.put(point, new Case(point));
        }
        return cases.get(point);
    }

    /**
     * ajoute un insecte à la position donné
     * @param insecte insecte à ajouter
     * @param position coordonées de la case où ajouter l'insecte
     */
    public void ajoutInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).addInsecte(insecte);
            this.nbPionsEnJeu++;
        } catch (Exception ex) {
            System.err.println("Erreur ajout : " + ex);
        }
    }
    
    /**
     * ajoute un insecte à la position donné
     * @param insecte insecte à ajouter
     * @param position coordonées de la case où ajouter l'insecte
     */
    public void deplaceInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).addInsecte(insecte);
        } catch (Exception ex) {
            System.err.println("Erreur ajout : " + ex);
        }
    }

    /**
     * indique si la ruche est vide
     * @return true si la ruche est vide false sinon
     */
    public boolean rucheVide() {
        Object[] cases = this.cases.values().toArray();
        for (int i = 0; i < this.cases.size(); i++) {
            Case c = (Case) cases[i];
            if (!c.estVide()) return false;
        }
        return true;
    }
    
    /**
     * supprime un insecte
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
     * Donne toute les cases voisine
     * @param c case d'origine
     * @param exclureCaseOccupee si true donne uniquement les cases vide si false donne toute les cases
     * @return une Collection generique contenant les cases voisine de c
     */
    public Collection<Case> getCasesVoisines(Case c, boolean exclureCaseOccupee) {
        ArrayList<Case> voisins = new ArrayList<>();
        for (Point3DH pointCourant : c.getCoordonnees().coordonneesVoisins()) {
            Case voisin = getCase(pointCourant);
            if (voisin.estVide() || !exclureCaseOccupee) {
                voisins.add(voisin); //Case vide
            }
        }

        return voisins;
    }
    
    /**
     * Donne toute les cases occupées du plateau
     * @return un ArrayList des cases occupées
     */
    public ArrayList<Case> occupees() {
        ArrayList<Case> res = new ArrayList<>();
        Iterator<Case> it = this.cases.values().iterator();
        while(it.hasNext()) {
            Case c = it.next();
            if (!c.estVide()) {
                res.add(c);
            }
        }
        return res;
    }
    
    
    public ArrayList<Case> casesVidePlacement(Joueur j) {
        ArrayList<Case> res = new ArrayList<>();
        Iterator<Case> it = this.cases.values().iterator();
        boolean joueurAdverse = false;
        while(it.hasNext()) {
            Case c = it.next();
            ArrayList<Case> voisins = (ArrayList<Case>) this.getCasesVoisinesOccupees(c);
            if (c.estVide() && !voisins.isEmpty()) {
                joueurAdverse = false;
                Iterator<Case> itv = voisins.iterator();
                while (!joueurAdverse && it.hasNext()) {
                    if (!itv.next().getInsecteOnTop().getJoueur().equals(j))
                        joueurAdverse = true;
                }
                if (!joueurAdverse)
                    res.add(c);
            }
        }
        return res;
    }
    
    /**
     * Donne toute les cases occupées voisine de c
     * @param c case d'origine
     * @return une Collection generique contenant les cases occupées voisine de c
     */
    public Collection<Case> getCasesVoisinesOccupees(Case c) {
        ArrayList<Case> voisins = new ArrayList<>();
        for (Point3DH pointCourant : c.getCoordonnees().coordonneesVoisins()) {
            Case voisin = this.getCase(pointCourant);
            if (!voisin.estVide()) {
                voisins.add(voisin);
            }
        }

        return voisins;
    }
    
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
     * @param c1 case initiale
     * @param c2 case destination
     * @return true si le glissement de c1 à c2 est possible sans casser la ruche
     */
    public boolean glissementPossible(Case c1, Case c2) {
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
     * @param ghost case à ignorer
     * @param moveDest case à considérer
     * @return true si la ruche est brisée après le déplacement (ghost->moveDest)
     *         ou sans considérer de déplacement si ghost et moveDest valent null
     */
    public boolean rucheBrisee(Case ghost, Case moveDest) { //Tester aussi avec un compteur de changements
        if (this.rucheVide()) return false;
        
        Object[] listeCases;
        //Case c;
        int i = 0;
        listeCases = this.cases.values().toArray();
        if (ghost == null && moveDest == null) {
            do {
                moveDest = (Case) listeCases[i];
                i++;
            } while (i < listeCases.length && (moveDest.estVide()));
        }
        
        Insecte ghostBug = null;
        if (ghost != null && moveDest != null) {
            ghostBug = ghost.getInsecteOnTop();
            if (ghostBug != null) {
                try {
                    ghost.removeInsecte();
                    moveDest.addInsecte(ghostBug);
                } catch (Exception e) {
                    System.err.println("ERREUR Ruche brisé debut : "+e);
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
        
        if (ghost != null && moveDest != null) {
            if (ghostBug != null) {
                try {
                    moveDest.removeInsecte();
                    ghost.addInsecte(ghostBug);
                } catch (Exception e) {
                    System.err.println("ERREUR Ruche brisé fin : "+e);
                }
            }
        }
        
        return nbInsectesVisites != this.nbPionsEnJeu;
    }

    public boolean rucheBrisee2(Case ghost) { //Tester aussi avec un compteur de changements
        if (this.rucheVide()) return false;
        
        Object[] listeCases;
        Case c;
        int i = 0;
        listeCases = this.cases.values().toArray();
        do {
            c = (Case) listeCases[i];
            i++;
        } while (i < listeCases.length && (c.estVide()));
        
        

        ArrayList<Case> visites = new ArrayList<>();
        LinkedList<Case> file = new LinkedList<>();
        visites.add(c);
        file.add(c);
        while (!file.isEmpty()) {
            Case courante = file.pollFirst();
            ArrayList<Case> voisins = (ArrayList<Case>) getCasesVoisinesOccupees(courante);
            for (Case caseC : voisins) {
                if (!visites.contains(caseC) && !caseC.equals(ghost)) {
                    visites.add(caseC);
                    file.addLast(caseC);
                }
                
            }
        }
        
        return visites.size() != this.nbPionsEnJeu-1;
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

}