
package Modele;

import Modele.Insectes.Insecte;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plateau {

	private Map<Point,Case> cases;

	public Plateau() {
            cases = new HashMap<Point, Case>();
	}
        
        
        public Case getCase(Point point) {
            return cases.get(point);
        }
        
        public boolean caseEstVide(Point point) {
            return cases.get(point).estVide();
        }
        
        public void ajoutInsecte(Insecte insecte, Point position) {
            try {
                this.getCase(position).addInsecte(insecte);
            } catch (Exception ex) {
                System.err.println("Erreur ajout");
            }
        }
        
         public void deleteInsecte(Insecte insecte, Point position) {
            try {
                this.getCase(position).deleteInsecte();
            } catch (Exception ex) {
                System.err.println("Erreur delete");
            }
        }
}