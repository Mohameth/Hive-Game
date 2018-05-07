package hive;


import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sylve
 */
public class Piece {

    private ImageView imgv;
    private ArrayList<Circle> cornerHitbox;
    private Group g;
    double sceneWidth, sceneHeight;

    public Piece(String imgName, Group g, double sceneWidth, double sceneHeight) {
        Image img = new Image("pieces/" + imgName);
        this.imgv = new ImageView();
        imgv.setImage(img);
        imgv.setFitWidth(img.getWidth());
        imgv.setFitHeight(img.getHeight());

        imgv.setLayoutX(-(img.getWidth() / 2));
        imgv.setLayoutY(-(img.getHeight() / 2));
        imgv.setX(0);
        imgv.setX(0);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        this.g = g;

        imgv.setTranslateX(sceneWidth / 2);
        imgv.setTranslateY(sceneHeight / 2);

        this.cornerHitbox = new ArrayList<>();
        initCornerHitbox();
        g.getChildren().add(imgv);

    }

    public ImageView getImgv() {
        return imgv;
    }

    private void initCornerHitbox() {

        int r = 25;
        Circle c0 = new Circle(0, 0.87, r, Color.rgb(0, 0, 0, 0.5));
        Circle c1 = new Circle(0.75, 0.43, r, Color.rgb(0, 0, 0, 0.5));
        Circle c2 = new Circle(0.75, -0.43, r, Color.rgb(0, 0, 0, 0.5));
        Circle c3 = new Circle(0, -0.87, r, Color.rgb(0, 0, 0, 0.5));
        Circle c4 = new Circle(-0.75, -0.43, r, Color.rgb(0, 0, 0, 0.5));
        Circle c5 = new Circle(-0.75, 0.43, r, Color.rgb(0, 0, 0, 0.5));

        this.cornerHitbox.add(c0);
        this.cornerHitbox.add(c1);
        this.cornerHitbox.add(c2);
        this.cornerHitbox.add(c3);
        this.cornerHitbox.add(c4);
        this.cornerHitbox.add(c5);

        for (Circle c : this.cornerHitbox) {
            c.setCenterX(c.getCenterX() * 55); //distance du cercle par rapport au centre
            c.setCenterY(c.getCenterY() * 55);

            c.setTranslateX(sceneWidth / 2);
            c.setTranslateY(sceneHeight / 2);

            this.g.getChildren().add(c);
        }
    }

    public ArrayList<Circle> getCornerHitbox() {
        return cornerHitbox;
    }

    public void makeDraggable() {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        this.imgv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });

        // --- Shift node calculated from mouse cursor movement
        this.imgv.addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
            double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;

            moveXY(deltaX, deltaY);
//            this.imgv.setX(this.imgv.getX() + deltaX);
//            this.imgv.setY(this.imgv.getY() + deltaY);

            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    public void moveXY(double deltaX, double deltaY) {
        this.imgv.setX(this.imgv.getX() + deltaX);
        this.imgv.setY(this.imgv.getY() + deltaY);

        for (Circle c : this.cornerHitbox) {
            c.setCenterX(c.getCenterX() + deltaX);
            c.setCenterY(c.getCenterY() + deltaY);
        }

        //DEplacer les cercles hitbox
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

        for (Circle c : this.cornerHitbox) {
            c.setCenterX(c.getCenterX() * zoomFactor);
            c.setCenterY(c.getCenterY() * zoomFactor);
            c.setRadius(c.getRadius() * zoomFactor);
        }

    }

    private static final class MouseLocation {

        public double x, y;
    }

}
