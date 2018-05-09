
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sylve
 */
public class PieceHitbox {

    private Piece p;
    private Circle hitbox;
    private double posX, posY;  //posX = position du voisin pour l'image
    private int X, Y, Z; //X et Y coordonnée que prendra la piece
    private int numCoin;
    private PieceHitbox PieceHitboxSnapped;
    private boolean libre;

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public PieceHitbox(Piece newp, int numCoin) {
        this.p = newp;
        this.posX = 0;
        this.posY = 0;
        PieceHitboxSnapped = null;

        this.libre = true;
        this.X = 47;
        this.Y = 47;
        this.Z = 47;
        this.numCoin = numCoin;
    }

    public void setXYZ(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public void setVoisin(PieceHitbox phv) {
        this.PieceHitboxSnapped = phv;
    }

    public void setSnap(PieceHitbox phVoisin) {
        setLibre(false);
        this.setVoisin(phVoisin);
        phVoisin.setLibre(false);
        phVoisin.setVoisin(this);
    }

    public void removeSnap() {
        if (this.PieceHitboxSnapped != null) {
            this.PieceHitboxSnapped.setVoisin(null);
            this.PieceHitboxSnapped.setLibre(true);
        }
        this.setVoisin(null);
        setLibre(true);
    }

    public boolean isLibre() {
        return libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void updateCoordZoom(double zoomFactor) {
        hitbox.setCenterX(hitbox.getCenterX() * zoomFactor);
        hitbox.setCenterY(hitbox.getCenterY() * zoomFactor);
        hitbox.setRadius(hitbox.getRadius() * zoomFactor);
        posX *= zoomFactor;
        posY *= zoomFactor;
    }

    public void updateCoordMove(double deltaX, double deltaY) {
        hitbox.setCenterX(hitbox.getCenterX() + deltaX);
        hitbox.setCenterY(hitbox.getCenterY() + deltaY);
        posX += deltaX;
        posY += deltaY;
    }

    public void setCenterOfHitbox(double totZoom) {
        //double imgWidth = this.p.getImgv().getFitWidth() / 4;
        double scale = this.p.getImgv().getFitWidth() / 4;
        double result[] = getCenterOfHitbox(totZoom, scale);
        this.hitbox = new Circle(result[0], result[1], scale, Color.rgb(0, 0, 0, 0.5));
    }

    public double[] getCenterOfHitbox(double totZoom, double scale) {
        double x = p.getImgv().getX();
        double y = p.getImgv().getY();
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

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getZ() {
        return Z;
    }

    public void setCenterOfImageHitbox(double totZoom) {
        double scale = this.p.getImgv().getFitWidth() * totZoom + 25;
        double result[] = getCenterOfHitbox(totZoom, scale);
        this.posX = result[0];
        this.posY = result[1];
    }

}
