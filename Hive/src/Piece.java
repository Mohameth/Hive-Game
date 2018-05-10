
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
 * @author sylve
 */
public class Piece implements Observable {

    Observateur obs;
    private ImageView imgv;
    double sceneWidth, sceneHeight;
    int X, Y, Z; //la pièce possède les coordonnées suivantes sur le plateau (coordonnée plateau pas du canvas javafx)
    boolean isSnapped;

    public void setXYZ(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
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

    public Piece(String imgName, Group g, double sceneWidth, double sceneHeight) {
        Image img = new Image("pieces/" + imgName);
        this.imgv = new ImageView();
        imgv.setImage(img);
        imgv.setFitWidth(img.getWidth());
        imgv.setFitHeight(img.getHeight());

        imgv.setLayoutX(-(img.getWidth() / 2));
        imgv.setLayoutY(-(img.getHeight() / 2));
        imgv.setX(0);
        imgv.setY(0);
        isSnapped = false;

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        imgv.setTranslateX(sceneWidth / 2);
        imgv.setTranslateY(sceneHeight / 2);
    }

    public ImageView getImgv() {
        return imgv;
    }

    public void makeDraggable() {
        MouseLocation lastMouseLocation = new MouseLocation();
        MouseLocation lastMouseLocationDist = new MouseLocation();
        TimeSave ts = new TimeSave();

        // --- remember initial coordinates of mouse cursor and node
        this.imgv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
            lastMouseLocationDist.x = mouseEvent.getX();
            lastMouseLocationDist.y = mouseEvent.getY();
            ts.time = System.nanoTime();
            notifyListenersMousePressed(this);
            setSelected();

            System.out.println("Coordonnée: " + getX() + ", " + getY() + ", " + getZ());
            System.out.println("isnapped: " + this.isSnapped);
        }
        );

        this.imgv.addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            //isSnapped = true;
        }
        );
        // --- Shift node calculated from mouse cursor movement
        this.imgv.addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
            double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;
            double distance = (long) Math.hypot(mouseEvent.getX() - lastMouseLocationDist.x, mouseEvent.getY() - lastMouseLocationDist.y);
            long lEndTime = System.nanoTime();
            double elsapstime = (lEndTime - ts.time) / 1000000;
            //getImgv().setCursor(Cursor.NONE);
            double v = (distance / elsapstime);

            double maxVitesse = 0.6;
            //System.out.println("Speeed: " + v);
            //todo gerer le hover
//            if (!isSnapped || v > maxVitesse) {
//
//                if (isSnapped && v > maxVitesse) {
//                    isSnapped = false;
//                    moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
//                } else {
//                    //moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
//                    moveXY(deltaX, deltaY);
//                }
//            }

            //System.out.println("Vitesse: " + v);
            if (isSnapped) {
                //moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
                moveXY(deltaX, deltaY);
                isSnapped = false;
            }

            if (v > maxVitesse) {
                moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
            } else {
                moveXY(deltaX, deltaY);
            }

            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    public void setSelected() {
        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.95);
        dropShadow.setColor(Color.PURPLE);

        this.getImgv().setEffect(dropShadow);
    }

    public void unSelect() {
        this.getImgv().setEffect(null);
    }

    public void moveXY(double deltaX, double deltaY) {
        this.imgv.setX(this.imgv.getX() + deltaX);
        this.imgv.setY(this.imgv.getY() + deltaY);
        this.notifyListenersMove(deltaX, deltaY, false);
    }

    public void moveXYBoard(double deltaX, double deltaY) {
        this.imgv.setX(this.imgv.getX() + deltaX);
        this.imgv.setY(this.imgv.getY() + deltaY);
        this.notifyListenersMove(deltaX, deltaY, true);

    }

    public void snap(PieceHitbox myph, PieceHitbox ph) {
        // this.getImgv().setEffect(null);
        moveToXY(ph.getPosX(), ph.getPosY());
        isSnapped = true;
        this.X = ph.getX();
        this.Y = ph.getY();
        this.Z = ph.getZ();
        myph.setSnap(ph);
    }

    public void moveToXY(double x, double y) {
        double dx = x - getImgv().getX();
        double dy = y - getImgv().getY();
        moveXYBoard(dx, dy);
        //moveXY(dx, dy);
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

    }

    @Override
    public void addObserver(Observateur newobserver) {
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

    private static final class MouseLocation {

        public double x, y;
    }

    private static final class TimeSave {

        public long time;
    }
}
