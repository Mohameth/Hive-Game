
package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Plateau {

	private Map<Point3DH,Case> cases;

	public Plateau() {
            cases = new HashMap<Point3DH, Case>();
            Point3DH origine = new Point3DH(0,0,0);
            cases.put(origine, new Case(origine));
	}
        
        
        public Case getCase(Point3DH point) {
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
                Case voisin = cases.get(pointCourant);
                if (voisin == null)         voisins.add(new Case(pointCourant)); //Case vide
                else if (!libreSeulement)   voisins.add(voisin);
            }

            return voisins;
        }
        
        /*public boolean gateBetween(Case c1, Case c2) {
            for (Case v1 : this.getCasesVoisines(c1, false)) {
                if (!v1.equals(c2)) {
                    if (this.getCasesVoisines(c2, false).contains(v1)) 
                        
                    
                }
            }
        }*/
}