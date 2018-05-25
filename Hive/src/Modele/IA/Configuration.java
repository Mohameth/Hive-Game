
package Modele.IA;

import Modele.Joueurs.Joueur;
import Modele.*;
import Modele.Insectes.Insecte;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author GRP3
 */
public class Configuration {
    Plateau plateau;
    Plateau plateauOrigine;
    Joueur joueurCourant;
    Joueur joueurCourantOrigine;
    Joueur adversaire;
    Configuration parent;
    private ArrayList<Configuration> fils = null;
    boolean dejaEvalue = false;
    private Integer evaluation = null;
    private Coup coupJoue;
    private boolean tourAdversaire = false;
    
    /**
     * Constructeur interne, crée un fils à partir des paramètre
     * 
     * @param plateau le nouveau plateau
     * @param joueurCourant le joueur courant
     * @param adversaire le joeuur adverse
     * @param tourAdversaire indique si l'adversaire doit jouer
     * @param parent configuration précédente
     * @param coupJoue les coup joue
     */
    private Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire, boolean tourAdversaire, Configuration parent, Coup coupJoue) {
        this.plateau = plateau;
        this.joueurCourant = joueurCourant;
        this.adversaire = adversaire;
        this.tourAdversaire = tourAdversaire;
        this.parent = parent;
        this.coupJoue = coupJoue;
    }
    
    /**
     * Cette classe permet de stocker l'état du plateau 
     * 
     * @param plateau le plateau original
     * @param joueurCourant le joueur courant (IA)
     * @param adversaire le joeuur adverse 
     */
    public Configuration(Plateau plateau, Joueur joueurCourant, Joueur adversaire) {
        this.plateau = plateau;
        this.plateauOrigine = plateau;
        this.joueurCourant = joueurCourant;
        this.joueurCourantOrigine = joueurCourant;
        this.adversaire = adversaire;
    }
    
    /**
     * 
     * @return L'évaluation de la config., utilise IAEvaluation
     */
    public int getEvaluation() {
        if (!dejaEvalue) {        
            evaluation = new IAEvaluation(plateau, joueurCourant, adversaire).getEvaluation();
            dejaEvalue = true;
        }
        
        return evaluation;
    }
    
    /**
     * Indique si cette configuration est finale
     * 
     * @return true si la configuration est finale
     */
    public boolean estFeuille() {
        return joueurCourant.reineBloquee() || adversaire.reineBloquee() || (dejaEvalue && fils.size() == 0);
    }
    
    /**
     * Renvoie toutes les configurations du plateau à partir de celle-ci
     * 
     * @param tourAdversaire indique si les coups possibles sont ceux de l'adversaire
     * @return une liste de configurations issue de la courante
     */
    public ArrayList<Configuration> getAllCoupsPossibles(boolean tourAdversaire) {
        this.tourAdversaire = tourAdversaire;
        if (fils == null) {
            fils = new ArrayList<>();
            Joueur j = tourAdversaire ? adversaire : joueurCourant;
            
            for (Insecte i : j.getPions()) {
                if (i.getEmplacement() == null) {
                    for (HexaPoint p : plateau.casesVidePlacement(j)) {
                        addFils(true, i, p);
                    }
                } else {
                    for (Case c : i.deplacementPossible(plateau)) {
                        addFils(false, i, c.getCoordonnees());
                    }
                }
            }
        }
        
        return fils;
    }
    
    /**
     * Ajoute les insectes du plateau de la config. dans newPlateau
     * Utilise les insectes présents dans les clones des joueurs
     * 
     * @param newPlateau clone de this.plateau, contient autant de cases que plateau
     * @param newCourant clone de joueurCourant
     * @param newAdversaire clone d'adversaire
     */
    private void ajoutInsectesPlateau(Plateau newPlateau, Joueur newCourant, Joueur newAdversaire) {
        for(Insecte insecte : plateau.getInsectes()) {
            Insecte cloneInsecte = null;
            if (insecte.getJoueur().getNumJoueur() == joueurCourant.getNumJoueur())  {
                cloneInsecte = newCourant.getPions().get(joueurCourant.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(joueurCourant);
            }
            else {
                cloneInsecte = newAdversaire.getPions().get(adversaire.getPions().indexOf(insecte));
                cloneInsecte.setJoueur(adversaire);
            }
            
            newPlateau.ajoutInsecte(cloneInsecte, insecte.getEmplacement().getCoordonnees());
        }
    }
    
    /**
     * Crée une nouvelle config. en appliquant le déplacement ou placement passé en paramètre
     * 
     * @param modePlacement indique s'il s'agit d'un placement
     * @param i insecte à déplcer
     * @param p cible du déplacement
     */
    private void addFils(boolean modePlacement, Insecte i, HexaPoint p) {
        //Création d'un clone du plateau avec le même nombre de cases sans insectes
        Plateau newPlateau = (Plateau) plateau.clone();
        
        //Clone des joueurs et des insectes qu'ils contiennent
        Joueur newCourant = joueurCourant.clone();
        Joueur newAdversaire = adversaire.clone();
        
        newCourant.setPlateau(newPlateau);
        newAdversaire.setPlateau(newPlateau);
        
        //Ajout des insectes provenant des clones des joueurs
        ajoutInsectesPlateau(newPlateau, newCourant, newAdversaire);
        
        //Récupération de l'insecte à placer ou déplacer en fonction de qui joue
        Joueur j = tourAdversaire ? adversaire : joueurCourant;
        Joueur newJ = tourAdversaire ? newAdversaire : newCourant;
        Insecte newInsecte = newJ.getPions().get(j.getPions().indexOf(i));
        
        HexaPoint origine = null;
        int niveau = 1;// --> Permet la gestion d'insectes allant par-dessus d'autres insectes
        if (modePlacement) newJ.placementInsecte(newInsecte, p);
        else {
            origine = newInsecte.getEmplacement().getCoordonnees();
            niveau = newInsecte.getNiveau();
            newPlateau.deleteInsecte(newInsecte, origine);
            newPlateau.deplaceInsecte(newInsecte, p);
        }
        
        fils.add(new Configuration(
                newPlateau, 
                newCourant,
                newAdversaire,
                tourAdversaire,
                this, 
                new Coup(modePlacement, j.getPions().indexOf(i), origine, niveau, p)
        ));
    }

    public Coup getCoupJoue() {
        return coupJoue;
    }
}
