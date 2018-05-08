package Modele;

import javafx.geometry.Point3D;

public class Hive {

	Plateau plateau;
	Joueur joueur1;
        Joueur joueur2;
        Joueur joueurCourant;

	public Hive() {
		this.plateau = new Plateau();
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
        
        public boolean appartient(Point3D caseCible) {
            if (plateau.getCase(caseCible).getInsecte().getJoueur().equals(joueurCourant))
                return true;
            
            return false;
        }
        
        public void selectionInsecte(Point3D caseCible) {
            plateau.getCase(caseCible).getInsecte().deplacementPossible(plateau);
        }
        
        public void deplacementInsecte(Point3D origine, Point3D cible) {
            if (!plateau.getCase(origine).estVide())
                plateau.getCase(origine).getInsecte().Deplacement(plateau, cible);
        }
        
}