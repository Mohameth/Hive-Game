/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Insectes.Insecte;
import java.util.HashMap;

/**
 *
 * @author moham
 */
public class IAEvaluation {
    Plateau jeu;
    Joueur joueur;
    Integer evaluationPlateau;
    HashMap<Insecte, Integer> myPiecesValues;
    HashMap<Insecte, Integer> opponentPiecesValues;
    
    //Stockage des reines pour récup. rapide de leurs positions
    private Insecte maReine;
    private Insecte reineAdverse;

    public IAEvaluation(Plateau jeu, Joueur joueur) {
        this.jeu = jeu;
        this.joueur = joueur;
        
        myPiecesValues = new HashMap<>();
        opponentPiecesValues = new HashMap<>();
    }
    
    public int getEvaluation() {
        initPiecesValues(myPiecesValues, false);
        initPiecesValues(opponentPiecesValues, true);
        updatePositionValues(myPiecesValues);
        updatePositionValues(opponentPiecesValues);
        
        return getSumEvaluation(myPiecesValues) + getSumEvaluation(opponentPiecesValues);
    }
    
    private Boolean insecteBelongsToOpponent(Insecte i) {
        return !i.getJoueur().equals(joueur);
    }
    
    private void initPiecesValues(HashMap<Insecte, Integer> piecesValues, Boolean opponent) {
        for (Case c : jeu.occupees()) {
            for (Insecte i : c.getInsectes()) {
                if (insecteBelongsToOpponent(i).equals(opponent)) {
                    switch(i.getType()) {
                        case REINE: 
                            piecesValues.put(i, 50);
                            if (opponent) reineAdverse = i;
                            else maReine = i;
                            break;
                        case SCARABEE: 
                            piecesValues.put(i, 20);
                            break;
                        case SAUTERELLE: 
                            piecesValues.put(i, 20);
                            break;
                        case ARAIGNEE: 
                            piecesValues.put(i, 10);
                            break;    
                        case FOURMI: 
                            piecesValues.put(i, 30);
                            break;

                        case MOUSTIQUE: 
                            piecesValues.put(i, 20);//Dépend de l'insecte qu'il copie
                            break;    
                        case CLOPORTE: 
                            piecesValues.put(i, 15);
                            break;
                        case COCCINELLE: 
                            piecesValues.put(i, 15);
                            break;
                    }
                }
            }
        }
    }
    
    private Boolean estRecouvert(Insecte i) {
        return !i.getEmplacement().getInsecteOnTop().equals(i);
    }
    
    private boolean estImmobilise(Insecte i) {
        return i.deplacementPossible(jeu).isEmpty();
    }
    
    private boolean entoureReineAdverse(Insecte i) {
        //En train de bloquer la reine adverse + 15
        Insecte reine;
        if (insecteBelongsToOpponent(i))    reine = maReine;
        else                                reine = reineAdverse;
        
        return (jeu.estVoisin(i, reine));
    }
    
    private int mobilite(Insecte i) {
        return i.deplacementPossible(jeu).size();
    }
    
    private boolean aGagne() {
        return false;
    }
    
    private boolean aPerdu() {
        return false;
    }
    
    private void updatePositionValues(HashMap<Insecte, Integer> piecesValues) {
        for (Insecte i : piecesValues.keySet()) {
            Integer value = piecesValues.get(i);
            if (estImmobilise(i))       value -= value + 5;
            if (estRecouvert(i))        value -= 20;
            if (entoureReineAdverse(i)) value += 15;
            if (aGagne())                 value += 100;
            if (aPerdu())                 value -= 100;
            value += mobilite(i);
            /*if (bloqueFourmiAdverse(i)) value += 5;
            value += capaciteABloquerReineAdverse(i);
            */
            
            //TODO: Gérer les pouvoirs spéciaux et ne pas mettre lse valeurs en brut.
            piecesValues.put(i, value);
        }
    }
    
    private int getSumEvaluation(HashMap<Insecte, Integer> piecesValues) {
        int sum = 0;
        for (int v : piecesValues.values()) {
            sum += v;
        }
        
        return sum;
    }
}
