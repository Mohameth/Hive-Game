
package Modele.Joueurs.ThreadCoups;

import Modele.HexaPoint;
import Modele.IA.Configuration;
import Modele.IA.Coup;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.NumJoueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;

/**
 *
 * @author GRP3
 */
public class CoupMoyen extends AbstractCoup{
    int horizon = 2;
    public Coup lastCoup = null;
    
    /**
     * Joue un coup en utilisant l'algo du minimax
     * 
     * @param plateau le plateau avec les pions
     * @param joueur le joueur courant
     * @param adverse le joueur adverse
     */
    public CoupMoyen(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        super(plateau, joueur, adverse);
    }
    
    /**
     * Màj la profondeur de calcul
     * 
     * @param horizon 
     */
    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }
    
    /**
     * Joue un coup avec le minimax
     * 
     * @return true si la génération de coup a réussi
     */
    @Override
    protected boolean coup() {
        System.out.println("IA MOYEN");
        Coup coup = minimax();
        if (coup == null) {//Si tous les coups sont perdant, on joue au hasard
            CoupFacile cf = new CoupFacile(plateau, joueur, adverse);
            return cf.coup();
        }
        Insecte i;
        
        if (coup.isModePlacement()) {
            i = joueur.getPions().get(coup.getNumDansMain());
            //joueur.placementInsecte(i, coup.getCible());
            System.out.println(i.getClass().getName() + " en " + coup.getCible() + " (placement)");
            joueur.coupChoisi(i, coup.getCible(), true);
            return true;
        }
        
        i = plateau.getCase(coup.getOrigine()).getInsectes().get(coup.getNiveauInsecte()-1);
        //i.deplacement(plateau, coup.getCible()); 
        joueur.coupChoisi(i, coup.getCible(), false);
        System.out.println(i.getClass().getName() + " en " + coup.getCible());
        lastCoup = coup;
              
        //joueur.
        return true;
    }
    
    /**
     * 
     * @return le coup trouvé
     */
    public Coup minimax() {
        Configuration parent = new Configuration(plateau, joueur, adverse);
        Configuration meilleurConf = null;
        int oldVal = Integer.MIN_VALUE;
        int newVal;
        
        ArrayList<Configuration> x = parent.getAllCoupsPossibles(false);
        //System.out.println("Analyse de mes " + x.size() + " coups");
        int evalParent = parent.getEvaluation();
        System.out.println("\nPosition courante évalué à " + evalParent);
        for (Configuration c : x) {
            newVal = calculJoueurCourant(c, horizon, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (newVal > oldVal) {
                meilleurConf = c;
                oldVal = newVal;
            }
        }
        if (meilleurConf != null) {
            System.out.println("Coup évalué à " + meilleurConf.getEvaluation() + "\n");
            return meilleurConf.getCoupJoue();
        } else {
            System.out.println("aucune conf : Jeu aleatoire");
            return null;
        }
    }
    
    public int calculJoueurCourant(Configuration conf, int horizon, int alpha, int beta) {
        if (horizon == 0 || conf.estFeuille()) {
            int eval = conf.getEvaluation();
            conf = null;
            return eval;
        }
        
        int valeur = Integer.MAX_VALUE;
        //System.out.println(" --> Analyse de " + conf.getAllCoupsPossibles(true).size() + "coups adverses");
        for (Configuration confCourante : conf.getAllCoupsPossibles(true)) {
            valeur = Integer.min(valeur, calculAdversaire(confCourante, horizon-1, alpha, beta));
            if (alpha > valeur) return valeur;
            beta = Integer.min(beta, valeur);
        }
        
        return valeur;
    }
    
    public int calculAdversaire(Configuration conf, int horizon, int alpha, int  beta) {
        if (horizon == 0 || conf.estFeuille()) {
            int eval = conf.getEvaluation();
            conf = null;
            return eval;
        } 
        
        int valeur = Integer.MIN_VALUE;
        //System.out.println(" --> Analyse de " + conf.getAllCoupsPossibles(false).size() + "coups du turfu");
        for (Configuration confCourante : conf.getAllCoupsPossibles(false)) {
            valeur = Integer.max(valeur, calculJoueurCourant(confCourante, horizon-1, alpha, beta));
            if (valeur > beta) return valeur;
            alpha = Integer.max(alpha, valeur);
        }
        
        return valeur;
    }
    
}
