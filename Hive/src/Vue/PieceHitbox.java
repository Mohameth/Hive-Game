/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sylve
 */
package Vue;

public class PieceHitbox {

    private Piece p;
    private double posX, posY;  //posX = position du voisin pour l'image
    private int X, Y, Z; //X et Y coordonnée que prendra la piece
    private int numCoin;
    private boolean libre;

    public PieceHitbox(Piece newp, int numCoin) {
        this.p = newp;
        this.posX = 0;
        this.posY = 0;
        this.libre = true;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.numCoin = numCoin;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getZ() {
        return Z;
    }

    public void setXYZ(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }

    public void affiche() {
        System.out.println("HITBOX X: " + X + " Y:" + Y + " Z:" + Z + " \tPOS: " + numCoin + "IMGX: " + getPosX() + " IMGY: " + getPosY());
    }

    public boolean isLibre() {
        return libre;
    }

    public void updateCoordZoom(double zoomFactor) {
        posX *= zoomFactor;
        posY *= zoomFactor;
    }

    public void updateCoordMove(double deltaX, double deltaY) {
        posX += deltaX;
        posY += deltaY;
    }

    public double[] getCenterOfHitbox(double totZoom, double scale) {
        double x = p.getImgPion().getX();
        double y = p.getImgPion().getY();
        switch (numCoin) {
            case 0:
                x = 0;
                y = y - 0.87 * scale * totZoom;
                break;

            case 1:
                x = x + 0.75 * scale * totZoom;
                y = y - 0.43 * scale * totZoom;
                break;

            case 2:
                x = x + 0.75 * scale * totZoom;
                y = y + 0.43 * scale * totZoom;
                break;

            case 3:
                x = 0;
                y = y + 0.87 * scale * totZoom;
                break;
            case 4:
                x = x - 0.75 * scale * totZoom;
                y = y + 0.43 * scale * totZoom;
                break;
            case 5:
                x = x - 0.75 * scale * totZoom;
                y = y - 0.43 * scale * totZoom;
                break;

        }
        return new double[]{x, y};
    }

    public void setCenterOfImageHitbox(double totZoom) {
        double scale = this.p.getImgPion().getFitWidth() * totZoom + 25;
        double result[] = getCenterOfHitbox(totZoom, scale);
        this.posX = result[0];
        this.posY = result[1];
    }
}
