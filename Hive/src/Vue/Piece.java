package Vue;

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
public class Piece implements ObservableVue {

    private ObservateurVue obs;
    private ImageView imgv;
    private double sceneWidth, sceneHeight;
    private int X, Y, Z; //la pièce possède les coordonnées suivantes sur le plateau (coordonnée plateau pas du canvas javafx)
    private ArrayList<PieceHitbox> pieceHitboxList;
    private boolean snap, snapConfirm;
    private double prevImgX, prevImgY;
    private double totzoom;

    public Piece(String imgName, int sceneWidth, int sceneHeight, double totzoom) {
        Image img = new Image("pieces/" + imgName);
        this.imgv = new ImageView();
        this.pieceHitboxList = new ArrayList<>();
        this.totzoom = totzoom;
        imgv.setImage(img);
        imgv.setFitWidth(img.getWidth());
        imgv.setFitHeight(img.getHeight());

        imgv.setLayoutX(-(img.getWidth() / 2));
        imgv.setLayoutY(-(img.getHeight() / 2));
        imgv.setX(0);
        imgv.setY(0);

        //Ajout des events de la souris
        imgv.setCursor(Cursor.HAND);
        makeDraggable();
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

    public ImageView getImgv() {
        return imgv;
    }

    public ArrayList<PieceHitbox> getPieceHitboxList() {
        return pieceHitboxList;
    }

    public void setTranslation(int sw, int sh) {
        this.sceneWidth = sw;
        this.sceneHeight = sh;
        this.imgv.setTranslateX(sceneWidth / 2);
        this.imgv.setTranslateY(sceneHeight / 2);
    }

    public void makeDraggable() {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        this.imgv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();

            notifyListenersMousePressed(this);
            setSelected();
            this.getImgv().toFront(); //afficher par dessus les autres
            snapConfirm = false;
            affiche();
            //printVoisin();
        }
        );

        this.imgv.addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            if (!snap) { //retour position origine
                moveToXY(this.prevImgX, this.prevImgY);
            } else {

                updatePrevPos();
            }
            snapConfirm = true;
            snap = true;
        }
        );
        // --- Shift node calculated from mouse cursor movement
        this.imgv.addEventFilter(MouseEvent.MOUSE_DRAGGED, (
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

    public void affiche() {
        System.out.println("PIECE : X: " + X + " Y:" + Y + " Z:" + Z + " Snap: " + snap);
    }

    public void printVoisin() {
        int i = 0;
        for (PieceHitbox ph : pieceHitboxList) {
            System.out.println("POS: " + i++ + "\t X: " + ph.getX() + " Y: " + ph.getY() + " Z: " + ph.getZ() + " Libre: " + ph.isLibre());
        }
    }

    public void setSelected() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.90);
        dropShadow.setColor(Color.RED);
        this.getImgv().setEffect(dropShadow);
    }

    public void unSelect() {
        this.getImgv().setEffect(null);
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
        double dx = x - getImgv().getX();
        double dy = y - getImgv().getY();
        moveXYBoard(dx, dy);
    }

    private void Applymove(double dx, double dy) {
        this.imgv.setX(this.imgv.getX() + dx);
        this.imgv.setY(this.imgv.getY() + dy);
        if (snap && snapConfirm) {
            updatePrevPos();
        }
    }

    private void updatePrevPos() {
        prevImgX = imgv.getX();
        prevImgY = imgv.getY();
    }

    public void snap(PieceHitbox ph) {
        this.snap = true;
        moveToXY(ph.getPosX(), ph.getPosY());
        setXYZ(ph.getX(), ph.getY(), ph.getZ());
    }

    public void zoomFactor(double zoomFactor) {
        double imgX, imgY;

        this.imgv.setScaleX(this.imgv.getScaleX() * zoomFactor);  //faire un visiteur
        this.imgv.setScaleY(this.imgv.getScaleY() * zoomFactor);

        //mise a jour des coordonnées car l'image est plus grande
        imgX = this.imgv.getX() * zoomFactor;
        imgY = this.imgv.getY() * zoomFactor;
        this.imgv.setX(imgX);
        this.imgv.setY(imgY);

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

    private static final class MouseLocation {

        public double x, y;
    }

}
