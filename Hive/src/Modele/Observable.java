/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

/**
 *
 * @author Jeremy
 */
public interface Observable {
    
    public void addObserver(Observateur newobserver);
    public void notifyListeners();
}