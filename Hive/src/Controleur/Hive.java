package Controleur;

import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurIA;
import Modele.Plateau;
import Modele.Point3DH;
import Vue.Vue;
import javafx.geometry.Point3D;

public class Hive {

	Plateau plateau;
	Joueur joueur1;
        Joueur joueur2;
        Joueur joueurCourant;

	public Hive(String[] args) {
		this.plateau = new Plateau();
                Vue.initFenetre(args, this);
	}
        
        public void setJoueurs(int cas){
            switch(cas) {
                case 1:
                    this.joueur1 = new JoueurHumain(this.plateau);
                    this.joueur2 = new JoueurHumain(this.plateau);
                break;
                case 2:
                    this.joueur1 = new JoueurHumain(this.plateau);
                    this.joueur2 = new JoueurIA(this.plateau);
                break;
                case 3:
                    this.joueur1 = new JoueurHumain(this.plateau);
                    this.joueur2 = new JoueurIA(this.plateau);
                break;
                case 4:
                    this.joueur1 = new JoueurHumain(this.plateau);
                    this.joueur2 = new JoueurIA(this.plateau);
                break;
            }
        }
        
        public boolean appartient(Point3DH caseCible) {
            if (plateau.getCase(caseCible).getInsecteOnTop().getJoueur().equals(joueurCourant))
                return true;
            
            return false;
        }
        
        public void selectionInsecte(Point3DH caseCible) {
            plateau.getCase(caseCible).getInsecteOnTop().deplacementPossible(plateau);
        }
        
        public void deplacementInsecte(Point3DH origine, Point3DH cible) {
            if (!plateau.getCase(origine).estVide() && appartient(origine)) {
                joueurCourant.coup(plateau.getCase(origine).getInsecteOnTop(),cible);
                this.joueurSuivant();
            }
            
        }
        
        public void placementInsecte() {
            
        }
        
        private void joueurSuivant() {
            if (joueurCourant.equals(this.joueur1))
                this.joueurCourant = this.joueur2;
            else if (joueurCourant.equals(this.joueur2))
                this.joueurCourant = this.joueur1;
        }
}