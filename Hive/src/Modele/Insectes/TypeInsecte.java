/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Insectes;

/**
 *
 * @author Jeremy
 */
public enum TypeInsecte {
    ARAIGNEE(0), CLOPORTE(2), REINE(4), COCCINELLE(8), FOURMI(16), SAUTERELLE(32),
    SCARABEE(64), MOUSTIQUE(128);
    
    int contenu;
    
    TypeInsecte(int i) {
        contenu = i;
    }
}
