
package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.HashMap;
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
                this.getCase(position).deleteInsecte();
            } catch (Exception ex) {
                System.err.println("Erreur delete");
            }
        }
}