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

public class Plateau {

    private Map<Point3DH, Case> cases;
    private int nbPionsEnJeu;

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
            System.err.println("Erreur ajout");
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
            System.err.println("Erreur delete");
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
            if (this.gateBetween(c, it.next())) {
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

    public boolean rucheBrisee() { //Tester aussi avec un compteur de changements
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
                if (!visites.contains(caseC)) {
                    visites.add(caseC);
                    file.addLast(caseC);
                }
                
            }
        }
        if (visites.size() == this.nbPionsEnJeu) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.cases);
        return hash;
    }

}
