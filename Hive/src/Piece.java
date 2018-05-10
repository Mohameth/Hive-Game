
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
public class Piece implements Observable {

    Observateur obs;
    private ImageView imgv;
    double sceneWidth, sceneHeight;
    int X, Y, Z; //la pièce possède les coordonnées suivantes sur le plateau (coordonnée plateau pas du canvas javafx)
    boolean isSnapped;
    private ArrayList<PieceHitbox> pieceHitboxList;

    double totzoom;

    public void setXYZ(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        initCornerHitbox();
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

    public Piece(String imgName, Group g, double sceneWidth, double sceneHeight, double totzoom) {
        Image img = new Image("pieces/" + imgName);
        this.imgv = new ImageView();
        this.pieceHitboxList = new ArrayList<>();
        imgv.setImage(img);
        imgv.setFitWidth(img.getWidth());
        imgv.setFitHeight(img.getHeight());

        imgv.setLayoutX(-(img.getWidth() / 2));
        imgv.setLayoutY(-(img.getHeight() / 2));
        imgv.setX(0);
        imgv.setY(0);
        isSnapped = false;
        this.totzoom = totzoom;

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        imgv.setTranslateX(sceneWidth / 2);
        imgv.setTranslateY(sceneHeight / 2);
        setXYZ(47, 47, 47);
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
            this.getImgv().toFront(); //afficher par dessus les autres

            System.out.println("Coordonnée: " + getX() + ", " + getY() + ", " + getZ());
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
            double v = (distance / elsapstime);

            double maxVitesse = 0.6;

            isSnapped = false;

            moveToXY(mouseEvent.getSceneX() - (sceneWidth / 2), mouseEvent.getSceneY() - (sceneHeight / 2));
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
        dropShadow.setSpread(0.90);
        dropShadow.setColor(Color.RED);

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

    public void snap(PieceHitbox ph) {
        moveToXY(ph.getPosX(), ph.getPosY());
        isSnapped = true;
        this.X = ph.getX();
        this.Y = ph.getY();
        this.Z = ph.getZ();
    }

    public void moveToXY(double x, double y) {
        double dx = x - getImgv().getX();
        double dy = y - getImgv().getY();
        moveXYBoard(dx, dy);
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
        this.totzoom *= Math.abs(zoomFactor); //a verfiier
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

    public ArrayList<PieceHitbox> getPieceHitboxList() {
        return pieceHitboxList;
    }

    private void initCornerHitbox() {

        for (int i = 0; i < 6; i++) {
            PieceHitbox pieceHitbox;

            pieceHitbox = new PieceHitbox(this, i);  //10 10 la postion de l'image voisine
            pieceHitbox.setCenterOfHitbox(totzoom);
            pieceHitbox.setCenterOfImageHitbox(totzoom);

            if (X != 47 && Y != 47 && Z != 47) { //on est le premier
                int result[] = getHitboxCoord(i, getX(), getY(), getZ());
                pieceHitbox.setXYZ(result[0], result[1], result[2]);
            }

            //makeHitoxScrollZoom(pieceHitbox.getHitbox());
            pieceHitboxList.add(pieceHitbox);
        }
    }

    private int[] getHitboxCoord(int pos, int coordX, int coordY, int coordZ) { //en fonction de la position du coin et de la position de la piece sur la grille
        // System.out.println("AA= " + pos);
        // System.out.println("X= " + coordX + " Y = " + coordY + " Z = " + coordZ);

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

        //System.out.println(" New X= " + x + " New Y = " + y + " New Z = " + z);
        return new int[]{x, y, z};
    }

    public void updateHitBoxPos() {
        int i = 0;
        for (PieceHitbox pieceh : this.pieceHitboxList) {
            int result[] = getHitboxCoord(i, getX(), getY(), getZ());
            pieceh.setXYZ(result[0], result[1], result[2]);
            i++;
        }
    }

}
