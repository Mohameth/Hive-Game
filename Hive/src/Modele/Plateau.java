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

/**
 * Plateau décrit l'Etat du plateau de jeu et les actions disponible pour le modifier.
 * @author GRP3
 * 
 */

public class Plateau implements Observable{

    private Map<Point3DH, Case> cases;
    private int nbPionsEnJeu;
    private Observateur observateur;

    public Plateau() {
        cases = new HashMap<Point3DH, Case>();
        Point3DH origine = new Point3DH(0, 0, 0);
        cases.put(origine, new Case(origine));
        this.nbPionsEnJeu = 0; //Peut-être à remplacer par une méthode
    }

    public Case getCase(Point3DH point) {
        if (cases.get(point) == null) {
            cases.put(point, new Case(point));
        }
        return cases.get(point);
    }

    public void ajoutInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).addInsecte(insecte);
            this.nbPionsEnJeu++;
        } catch (Exception ex) {
            System.err.println("Erreur ajout : " + ex);
        }
    }

    public boolean rucheVide() {
        Object[] cases = this.cases.values().toArray();
        for (int i = 0; i < this.cases.size(); i++) {
            Case c = (Case) cases[i];
            if (!c.estVide()) return false;
        }
        return true;
    }
    
    public void deleteInsecte(Insecte insecte, Point3DH position) {
        try {
            this.getCase(position).removeInsecte();
        } catch (Exception ex) {
            System.err.println("Erreur delete : " + ex);
        }
    }

    public Collection<Case> getCasesVoisines(Case c, boolean libreSeulement) {
        ArrayList<Case> voisins = new ArrayList<>();
        for (Point3DH pointCourant : c.getCoordonnees().coordonneesVoisins()) {
            Case voisin = getCase(pointCourant);
            if (voisin.estVide() || !libreSeulement) {
                voisins.add(voisin); //Case vide
            }
        }

        return voisins;
    }

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
    
    public Collection<Case> getCasesVoisinesSansGates(Case c, boolean exclureCaseLibre) {
        Collection<Case> dep = this.getCasesVoisines(c, exclureCaseLibre);
        Iterator<Case> it = dep.iterator();
        while (it.hasNext()) {
            Case voisin = it.next();
            if (this.gateBetween(c, voisin) || rucheBrisee(c, voisin)) {
                it.remove();
            }
        }

        return dep;
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
        

        ArrayList<Case> visites = new ArrayList<>();
        LinkedList<Case> file = new LinkedList<>();
        visites.add(moveDest);
        file.add(moveDest);
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
        
        return visites.size() != this.nbPionsEnJeu;
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
