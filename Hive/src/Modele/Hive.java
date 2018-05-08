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
        
        public boolean appartient(Point3DH caseCible) {
            if (plateau.getCase(caseCible).getInsecteOnTop().getJoueur().equals(joueurCourant))
                return true;
            
            return false;
        }
        
        public void selectionInsecte(Point3DH caseCible) {
            plateau.getCase(caseCible).getInsecteOnTop().deplacementPossible(plateau);
        }
        
        public void deplacementInsecte(Point3DH origine, Point3DH cible) {
            if (!plateau.getCase(origine).estVide())
                plateau.getCase(origine).getInsecteOnTop().Deplacement(plateau, cible);
        }
        
}