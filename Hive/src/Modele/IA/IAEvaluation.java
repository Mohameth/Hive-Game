
package Modele.IA;

import Modele.Case;
import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.Insectes.TypeInsecte;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author GRP3
 */
public class IAEvaluation {

    Plateau jeu;
    Joueur joueur;
    Joueur adversaire;
    Integer evaluationPlateau;
    HashMap<Insecte, Float> myPiecesValues;
    HashMap<Insecte, Float> opponentPiecesValues;

    //Stockage des reines pour récup. rapide de leurs positions
    private Insecte maReine;
    private Insecte reineAdverse;

    /**
     * Permet d'évaluer une configuration de jeu
     * 
     * @param jeu le plateau à évaluer
     * @param joueur le joueur courant
     * @param adversaire le joueur adverse
     */
    public IAEvaluation(Plateau jeu, Joueur joueur, Joueur adversaire) {
        this.jeu = jeu;
        this.joueur = joueur;
        this.adversaire = adversaire;

        myPiecesValues = new HashMap<>();
        opponentPiecesValues = new HashMap<>();
    }

    /**
     *
     * @return l'évaluation courante de la position
     */
    public int getEvaluation() {
        /*if (aGagne() && aPerdu()) {
            return 0;
        }*/
        //else {
        if (aGagne()) {
            return Integer.MAX_VALUE;
        }
        if (aPerdu()) {
            return Integer.MIN_VALUE;
        }
        //}

        initMapInsectes();
        initPiecesValues(myPiecesValues, false);
        initPiecesValues(opponentPiecesValues, true);
        updatePositionValues(myPiecesValues);
        updatePositionValues(opponentPiecesValues);
        
        int me = getSumEvaluation(myPiecesValues);
        int him = getSumEvaluation(opponentPiecesValues);
        
        return me - him;
        /*return getSumEvaluation(myPiecesValues)
                - getSumEvaluation(opponentPiecesValues);*/
    }
    
    /**
     * Indique s'il s'agit d'un insecte adverse
     * 
     * @param i insecte
     * @return true si l'insecte appartient au joueur adverse, false sinon
     */
    private Boolean insecteBelongsToOpponent(Insecte i) {
        return !(i.getJoueur().getNumJoueur() == joueur.getNumJoueur());
    }
    
    /**
     * Initialise la listes des pièces ainsi que leurs valeurs
     * 
     * @param piecesValues liste des pieces d'un joueur
     * @param opponent true si on considère le joueur adverse
     */
    private void initPiecesValues(HashMap<Insecte, Float> piecesValues, Boolean opponent) {
        for (Insecte i : piecesValues.keySet()) {
            switch (i.getType()) {
                case REINE:
                    piecesValues.put(i, 50f);
                    if (opponent) {
                        reineAdverse = i;
                    } else {
                        maReine = i;
                    }
                    break;
                case SCARABEE:
                    piecesValues.put(i, 20f);
                    break;
                case SAUTERELLE:
                    piecesValues.put(i, 20f);
                    break;
                case ARAIGNEE:
                    piecesValues.put(i, 20f);
                    break;
                case FOURMI:
                    piecesValues.put(i, 30f);
                    break;

                case MOUSTIQUE:
                    piecesValues.put(i, 20f);//Dépend de l'insecte qu'il copie
                    break;
                case CLOPORTE:
                    piecesValues.put(i, 15f);
                    break;
                case COCCINELLE:
                    piecesValues.put(i, 15f);
                    break;
            }
        }
    }
    
    /**
     * Liste de tous les insectes (à partir des joueurs)
     * 
     * @return une liste de tous les insectes
     */
    /*private Collection<Insecte> getTousInsecte() {
        Collection<Insecte> res = new ArrayList<>();
        //Ajout des insectes du joueur
        for (Insecte i : joueur.getPions()) {
            res.add(i);
        }
        //Ajout des insectes du joueur adverse
        for (Insecte i : adversaire.getPions()) {
            res.add(i);
        }

        return res;
    }*/
    
    private void initMapInsectes() {
        for (Insecte i : joueur.getPions()) {
            myPiecesValues.put(i, 0f);
        }
        
        for (Insecte i : adversaire.getPions()) {
            opponentPiecesValues.put(i, 0f);
        }
    } 
    
    /**
     * Indique si l'insecte i est recouvert (en dessous d'un autre insecte)
     * 
     * @param i insecte
     * @return si l'insecte i est recouvert
     */
    private Boolean estRecouvert(Insecte i) {
        return !i.getEmplacement().getInsecteOnTop().equals(i);
    }

    private boolean estImmobilise(Insecte i) {//Couteux
        return i.deplacementPossible(jeu).isEmpty();
    }

    private boolean estDansLaMain(Insecte i) {
        return i.getEmplacement() == null;
    }
    /**
     * Indique si l'insecte i entoure la reine adverse
     * 
     * @param i insecte
     * @return true si i et la reine adverse sont voisin
     */
    private boolean entoureReineAdverse(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? maReine : reineAdverse;
        
        if (reine == null || reine.getEmplacement() == null) return false;
        
        return jeu.estVoisin(reine, i);
    }
    
    private boolean entoureMaReine(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? reineAdverse : maReine;
        
        if (reine == null || reine.getEmplacement() == null) return false;
        
        return jeu.estVoisin(reine, i);
    }
    
    /**
     * Indique la mobilité d'un insecte
     * 
     * @param i insecte
     * @return le nombre de cases vers lequel l'insecte i peut se déplacer 
     */
    private int mobilite(Insecte i) {
        return i.deplacementPossible(jeu).size();
    }
    
    /**
     * Indique si la configuration du plateau est gagnante
     * 
     * @return true si le joueur courant a gagné
     */
    private boolean aGagne() {
        return adversaire.reineBloquee();
    }
    
    /**
     * Indique si la configuration du plateau est perdant
     * 
     * @return true si l'adversaire a gagné
     */
    private boolean aPerdu() {
        return joueur.reineBloquee();
    }
    
    private boolean recouvreReineAdverse(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? maReine : reineAdverse;
        
        if (reine == null || reine.getEmplacement() == null) return false;
        
        return i.getNiveau() > 1 && reine.getEmplacement().equals(i.getEmplacement());
    }
    
    private boolean reineAdverseImmobilise(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? maReine : reineAdverse;
        
        if (reine == null || reine.getEmplacement() == null) return false;
        
        return reine.deplacementPossible(jeu).isEmpty();
    }
    
    /**
     * Met à jour les valeurs des pièces selon leurs positions (par rapport aux autres pièces)
     * 
     * @param piecesValues liste des pieces + valeurs d'un joueur
     */
    private void updatePositionValues(HashMap<Insecte, Float> piecesValues) {
        HashMap<Insecte, Float> piecesAjout = new HashMap<>();
        Iterator<Insecte> it = piecesValues.keySet().iterator();
        while (it.hasNext()) {
            Insecte i = it.next();
            Float value = piecesValues.get(i);
            
            if (estDansLaMain(i)) {
                value /= 3;
            } else {
                /*if (i.getType() == TypeInsecte.SAUTERELLE) {
                    System.out.println("kmdjlgksv;v,");
                }*/
                //if (i.getType() == TypeInsecte.MOUSTIQUE) value = getMoustiqueValue();
                if (estRecouvert(i)) value /= 5;
                else {
                    //value += mobilite(i)*2;
                    if (recouvreReineAdverse(i)) {
                        if (!reineAdverseImmobilise(i)) value *= 5;
                        else value *= 3;
                    }
                }
                if (entoureReineAdverse(i)) {
                    value *= 4;
                    //if (!entoureMaReine(i) && estImmobilise(i)) value *= 2;// --> Demande trop de ressources (fonction deplacementPossible)
                }
            }
            
            piecesAjout.put(i, value);
        }
        piecesValues.putAll(piecesAjout);
    }
    
    /**
     * Calcule la somme des valeurs des pièces 
     * 
     * @param piecesValues liste des pieces + valeurs d'un joueur
     * @return la somme des valeurs des pièces
     */
    private int getSumEvaluation(HashMap<Insecte, Float> piecesValues) {
        float sum = 0;
        for (float v : piecesValues.values()) {
            sum += v;
        }

        return Math.round(sum);
    }
}
