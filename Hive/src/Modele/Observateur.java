/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import javafx.geometry.Point3D;

/**
 *
 * @author Jeremy
 */
public interface Observateur {
    
    void coupJoue(Plateau p);
    void coupsPossibles(ArrayList<Point3D> coups);
    
}
