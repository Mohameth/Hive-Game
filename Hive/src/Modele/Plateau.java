
package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Point3D;

public class Plateau {

	private Map<Point3D,Case> cases;

	public Plateau() {
            cases = new HashMap<Point3D, Case>();
            Point3D origine = new Point3D(0,0,0);
            cases.put(origine, new Case(origine));
	}
        
        
        public Case getCase(Point3D point) {
            return cases.get(point);
        }
        
        
        public void ajoutInsecte(Insecte insecte, Point3D position) {
            try {
                this.getCase(position).addInsecte(insecte);
            } catch (Exception ex) {
                System.err.println("Erreur ajout");
            }
        }
        
         public void deleteInsecte(Insecte insecte, Point3D position) {
            try {
                this.getCase(position).deleteInsecte();
            } catch (Exception ex) {
                System.err.println("Erreur delete");
            }
        }
}