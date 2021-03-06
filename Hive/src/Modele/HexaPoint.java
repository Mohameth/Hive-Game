/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * la classe décrit un case du plateau selon un syteme de coordonées pour les plateaux en hexagone (voir https://www.redblobgames.com/grids/hexagons/ )
 * @author GRP3
 */
public class HexaPoint implements Serializable {
    
    int x;
    int y;
    int z;

    public HexaPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
    
    public void move(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void translate(int dx, int dy, int dz)  {
        this.x = x+dx;
        this.y = y+dy;
        this.z = z+dz;
    }
    
    /**
     * Donne les 6 points voisins du point actuel
     * @return les 6 points voisins
     */
    public Collection<HexaPoint> coordonneesVoisins() { //Voisins en sens horaire en partant du haut (face plate)
        ArrayList<HexaPoint> resultat = new ArrayList<>();
        resultat.add(new HexaPoint(this.x, this.y+1, this.z-1));
        resultat.add(new HexaPoint(this.x+1, this.y, this.z-1));
        resultat.add(new HexaPoint(this.x+1, this.y-1, this.z));
        resultat.add(new HexaPoint(this.x, this.y-1, this.z+1));
        resultat.add(new HexaPoint(this.x-1, this.y, this.z+1));
        resultat.add(new HexaPoint(this.x-1, this.y+1, this.z));
        return resultat;
    }
    
    /**
     * Point situé en haut de l'hexagone actuel
     * @return le voisin haut du point actuel
     */
    public HexaPoint voisinHaut() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x, this.y+1, this.z-1);
    }

    /**
     * Point situé en haut à droite de l'hexagone actuel
     * @return le voisin haut droite du point actuel
     */
    public HexaPoint voisinDroiteHaut() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x+1, this.y, this.z-1);
    }

    /**
     * Point situé en bas à droite de l'hexagone actuel
     * @return le voisin bas droite du point actuel
     */
    public HexaPoint voisinDroiteBas() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x+1, this.y-1, this.z);
    }

    /**
     * Point situé en bas de l'hexagone actuel
     * @return le voisin bas du point actuel
     */
    public HexaPoint voisinBas() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x, this.y-1, this.z+1);
    }

    /**
     * Point situé en bas à gauche de l'hexagone actuel
     * @return le voisin bas gauche du point actuel
     */
    public HexaPoint voisinGaucheBas() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x-1, this.y, this.z+1);
    }

    /**
     * Point situé en haut à gauche de l'hexagone actuel
     * @return le voisin haut gauche du point actuel
     */
    public HexaPoint voisinGaucheHaut() { //Voisins en sens horaire en partant du haut (face plate)
        return new HexaPoint(this.x-1, this.y+1, this.z);
    }
    
    
    @Override
    public String toString() {
        return "HexaPoint{" + "X: " + x + ", Y: " + y + ", Z: " + z + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        hash = 71 * hash + this.z;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HexaPoint other = (HexaPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }
    
	public HexaPoint clone() {
    	return new HexaPoint(this.getX(),this.getY(),this.getZ());
    }
    
}
