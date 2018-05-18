package Controleur;

import Modele.Insectes.*;
import Modele.Case;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurIA;
import Modele.Plateau;
import Modele.Point3DH;
import Modele.TypeInsecte;
import Vue.Vue;
import java.util.ArrayList;
import java.util.HashMap;


public class Hive {

	Plateau plateau;
	Joueur joueur1;
        Joueur joueur2;
        Joueur joueurCourant;
        int nbtours;

        
	public Hive(String[] args) {
		this.plateau = new Plateau();
                Vue.initFenetre(args, this);
                this.nbtours = 0;
	}
        
        public void setJoueurs(int cas, boolean extension){
            switch(cas) {
                case 1:
                    this.joueur1 = new JoueurHumain(this.plateau, extension);
                    this.joueur2 = new JoueurHumain(this.plateau, extension);
                break;
                case 2:
                    this.joueur1 = new JoueurHumain(this.plateau, extension);
                    this.joueur2 = new JoueurIA(this.plateau,1, extension); //Easy
                break;
                case 3:
                    this.joueur1 = new JoueurHumain(this.plateau, extension);
                    this.joueur2 = new JoueurIA(this.plateau,2 , extension); //Medium
                break;
                case 4:
                    this.joueur1 = new JoueurHumain(this.plateau, extension);
                    this.joueur2 = new JoueurIA(this.plateau,3, extension); //hard
                break;
            }
            this.joueurCourant = this.joueur1;
        }
        
        public boolean appartient(Point3DH caseCible) {
            if (plateau.getCase(caseCible).getInsecteOnTop().getJoueur().equals(joueurCourant))
                return true;
            
            return false;
        }
        
        public void selectionInsecte(Point3DH caseCible) {
            plateau.getCase(caseCible).getInsecteOnTop().deplacementPossible(plateau);
        }
        
        public boolean deplacementInsecte(Point3DH origine, Point3DH cible) {
            if (!plateau.getCase(origine).estVide() && appartient(origine)) {
                joueurCourant.coup(plateau.getCase(origine).getInsecteOnTop(),cible);
                this.joueurSuivant();
            }
            return true;
        }
        
        public ArrayList<Case> placementsPossibles() {
            return this.plateau.casesVidePlacement(this.joueurCourant);
        }
                
        public void joueurPlaceInsecte(TypeInsecte insecte, Point3DH cible) {
            int i = 0; Insecte ins = null;
            
            do {
                ins = this.joueurCourant.pionsEnMain().get(i);
                i++;
            }
            while (ins.getType() != insecte);
            
            this.joueurCourant.placementInsecte(ins, cible);
            this.joueurSuivant();
        }

        
        private ArrayList<Insecte> pionsPosables(int joueur) {
            ArrayList<Insecte> mainJoueur = null;
            switch (joueur) {
                case(1):
                    mainJoueur = this.joueur1.pionsEnMain();
                break;
                case(2):
                    mainJoueur = this.joueur2.pionsEnMain();
                break;
            }
             
            
            if (this.nbtours == 4) {
                int i = 0; boolean reinePres = false; Insecte reine = null;
                do {
                    if (mainJoueur.get(i) instanceof Reine) {
                        reinePres = true;
                        reine = mainJoueur.get(i);
                    }
                } while (i< mainJoueur.size() && !reinePres);
                
                if (reinePres) {
                    ArrayList<Insecte> res = new ArrayList<>();
                    res.add(reine);
                    return res;
                }
            }
            return mainJoueur;
        }
        
        public HashMap<Insecte, Boolean> mainJoueur(int joueur) {
            ArrayList<Insecte> posables = this.pionsPosables(joueur);
            ArrayList<Insecte> main = this.joueurCourant.pionsEnMain();
            HashMap<Insecte, Boolean> res = new HashMap<>();

            if (posables.equals(main)) {
                for (Insecte ins : main) {
                    res.put(ins, Boolean.TRUE);
                }
            } else {
                for (Insecte ins : main) {
                    if (posables.contains(ins)) {
                        res.put(ins, Boolean.TRUE);
                    } else {
                        res.put(ins, Boolean.FALSE);
                    }
                }
            }
                
            
            return res;
        }
        
        public ArrayList<Insecte> mainsInit() {
            return this.joueur1.pionsEnMain();
        }
        
        public void reset() {
            this.plateau = new Plateau();
            this.nbtours = 0;
            this.joueur1 = null;
            this.joueur2 = null;
            this.joueurCourant = null;
        }

        
        public boolean tourJoueurBlanc() {
            if (this.joueurCourant == this.joueur1)
                return true;
            
            return false;
        }
        
        private void joueurSuivant() {
            if (joueurCourant.equals(this.joueur1))
                this.joueurCourant = this.joueur2;
            else if (joueurCourant.equals(this.joueur2))
                this.joueurCourant = this.joueur1;
        }

        public Joueur getJoueurCourant() {
            return joueurCourant;
        }
        
        
        
}