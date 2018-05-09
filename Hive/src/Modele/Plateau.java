package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Plateau {

    private Map<Point3DH, Case> cases;

    public Plateau() {
        cases = new HashMap<Point3DH, Case>();
        Point3DH origine = new Point3DH(0, 0, 0);
        cases.put(origine, new Case(origine));
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
        } catch (Exception ex) {
            System.err.println("Erreur ajout");
        }
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
        Object[] listeCases;
        Case c;
        int i = 0;
        listeCases = this.cases.values().toArray();
        do {
            c = (Case) listeCases[i];
            i++;
        } while (c.estVide());

        ArrayList<Case> visites = new ArrayList<>();
        LinkedList<Case> file = new LinkedList<>();
        visites.add(c);
        file.add(c);
        while (!file.isEmpty()) {
            Case courante = file.pollFirst();
            ArrayList<Case> voisins = (ArrayList<Case>) getCasesVoisinesOccupees(courante);
            for (Case caseC : voisins) {
                if (!visites.contains(c)) {
                    visites.add(caseC);
                }
                file.addLast(caseC);
            }
        }
        if (visites.size() == this.cases.size()) {
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
