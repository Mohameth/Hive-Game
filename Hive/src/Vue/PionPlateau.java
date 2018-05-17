package Vue;

import Modele.TypeInsecte;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sylvestre
 */
public class PionPlateau extends Piece {

    private double sceneWidth, sceneHeight;
    private int X, Y, Z; //la pièce possède les coordonnées suivantes sur le plateau (coordonnée plateau pas du canvas javafx)
    private ArrayList<PieceHitbox> pieceHitboxList;
    private boolean snap, snapConfirm;
    private double prevImgX, prevImgY;
    private double totzoom;

    public PionPlateau(TypeInsecte pionType, int sceneWidth, int sceneHeight, double totzoom, boolean iswhite) {
        super(pionType, iswhite);
        this.pieceHitboxList = new ArrayList<>();
        this.totzoom = totzoom;

        setOnClicEvent();
        snap = true;

        setTranslation(sceneWidth, sceneHeight);
        initCornerHitbox();
        setXYZ(0, 0, 0);

    }

    public void setXYZ(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        updateHitBoxPos();
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

    public ArrayList<PieceHitbox> getPieceHitboxList() {
        return pieceHitboxList;
    }

    public void setTranslation(int sw, int sh) {
        this.sceneWidth = sw;
        this.sceneHeight = sh;
        getImgPion().setTranslateX(sceneWidth / 2);
        getImgPion().setTranslateY(sceneHeight / 2);
    }

    public void setOnClicEvent() {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        getImgPion().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();

            notifyListenersMousePressed(this);
            setSelected();
            snapConfirm = false;
            affiche();
            // printVoisin();
        }
        );

        getImgPion().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            if (!snap) { //retour position origine
                moveToXY(this.prevImgX, this.prevImgY);
            } else {
                updatePrevPos();
                //Jouer un coup user
                System.out.println("Jouer:" + this.getPionsType() + "Coordonnée : X: " + getX() + " Y: " + getY() + " Z: " + getZ());
            }
            snapConfirm = true;
            snap = true;
        }
        );
        // --- Shift node calculated from mouse cursor movement
        getImgPion().addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
            double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;

            snap = false;
            moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
            moveXY(deltaX, deltaY); //si enleve plus de snap

            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    @Override
    public void affiche() {
        System.out.println("PIECE : X: " + X + " Y:" + Y + " Z:" + Z + " Snap: " + snap);
    }

    public void printVoisin() {
        int i = 0;
        for (PieceHitbox ph : pieceHitboxList) {
            //System.out.println("POS: " + i++ + "\t X: " + ph.getX() + " Y: " + ph.getY() + " Z: " + ph.getZ() + " Libre: " + ph.isLibre());
            ph.affiche();
        }
    }

    public void moveXY(double deltaX, double deltaY) {
        Applymove(deltaX, deltaY);
        this.notifyListenersMove(deltaX, deltaY, false);
    }

    public void moveXYBoard(double deltaX, double deltaY) { //move sans maj des collisions
        Applymove(deltaX, deltaY);
        this.notifyListenersMove(deltaX, deltaY, true);
    }

    public void moveToXY(double x, double y) {
        double dx = x - getImgPion().getX();
        double dy = y - getImgPion().getY();
        moveXYBoard(dx, dy);
    }

    private void Applymove(double dx, double dy) {
        getImgPion().setX(getImgPion().getX() + dx);
        getImgPion().setY(getImgPion().getY() + dy);
        if (snap && snapConfirm) {
            updatePrevPos();
        }
    }

    private void updatePrevPos() {
        prevImgX = getImgPion().getX();
        prevImgY = getImgPion().getY();
    }

    @Override
    public void snap(PieceHitbox ph) {
        this.snap = true;
        moveToXY(ph.getPosX(), ph.getPosY());
        setXYZ(ph.getX(), ph.getY(), ph.getZ());
    }

    public void zoomFactor(double zoomFactor) {
        double imgX, imgY;

        getImgPion().setScaleX(getImgPion().getScaleX() * zoomFactor);  //faire un visiteur
        getImgPion().setScaleY(getImgPion().getScaleY() * zoomFactor);

        //mise a jour des coordonnées car l'image est plus grande
        imgX = getImgPion().getX() * zoomFactor;
        imgY = getImgPion().getY() * zoomFactor;
        getImgPion().setX(imgX);
        getImgPion().setY(imgY);

        //update pos previous
        this.prevImgX = imgX;
        this.prevImgY = imgY;

        this.totzoom *= Math.abs(zoomFactor);
    }

    @Override
    public void addObserver(ObservateurVue newobserver) {
        this.obs = newobserver;
    }

    @Override
    public void notifyListenersMove(double deltaX, double deltaY, boolean isBoardMove) {
        this.obs.updateMove(this, deltaX, deltaY, isBoardMove);
    }

    @Override
    public void notifyListenersMousePressed(Piece p) {
        this.obs.updateMousePressPiece(p);
    }

    public void initCornerHitbox() {
        pieceHitboxList.clear();
        for (int i = 0; i < 6; i++) {
            PieceHitbox pieceHitbox;
            pieceHitbox = new PieceHitbox(this, i);
            pieceHitbox.setCenterOfImageHitbox(totzoom);
            pieceHitboxList.add(pieceHitbox);
        }
    }

    private int[] getHitboxCoord(int pos, int coordX, int coordY, int coordZ) { //en fonction de la position du coin et de la position de la piece sur la grille
        int x = 0;
        int y = 0;
        int z = 0;
        switch (pos) {
            case 0:
                x = coordX;
                y = coordY + 1;
                z = coordZ - 1;
                break;
            case 1:
                x = coordX + 1;
                y = coordY;
                z = coordZ - 1;
                break;
            case 2:
                x = coordX + 1;
                y = coordY - 1;
                z = coordZ;
                break;
            case 3:
                x = coordX;
                y = coordY - 1;
                z = coordZ + 1;
                break;
            case 4:
                x = coordX - 1;
                y = coordY;
                z = coordZ + 1;
                break;
            case 5:
                x = coordX - 1;
                y = coordY + 1;
                z = coordZ;
                break;
        }
        return new int[]{x, y, z};
    }

    public void updateHitBoxPos() {
        int i = 0;
        for (PieceHitbox pieceh : this.pieceHitboxList) {
            int result[] = getHitboxCoord(i++, getX(), getY(), getZ());
            pieceh.setXYZ(result[0], result[1], result[2]);
        }
    }

    @Override
    public int decrNbPion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static final class MouseLocation {

        public double x, y;
    }

}
