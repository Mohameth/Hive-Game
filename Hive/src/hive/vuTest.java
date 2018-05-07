package hive;


import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class vuTest extends Application {

    ArrayList<Piece> pionList;
    private double sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void setImgListMouseEvent() {
        for (Piece p : this.pionList) {
            p.getImgv().setCursor(Cursor.HAND);
            p.makeDraggable();
        }
    }

    private void addImage(String imgName, Group g) {
        Piece p = new Piece(imgName, g, sceneWidth, sceneHeight);
        for (Circle c : p.getCornerHitbox()) {
            makeHitoxScrollZoom(c);
        }
        pionList.add(p);
    }

    private void dessineTemplate(Group g) {
        //création des images

        Line l1 = new Line(0, 0, sceneWidth, sceneHeight);
        Line l2 = new Line(0, sceneHeight, sceneWidth, 0);
        g.getChildren().addAll(l1, l2);

        for (int i = 0; i < 2; i++) {
            addImage("hive2.png", g);
        }

        //Ajout des events de la souris
        setImgListMouseEvent();

        Button b = new Button("Reset view");
        b.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            resetView();
        });

        g.getChildren().add(b);
    }

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        this.pionList = new ArrayList<>();

        //creation du plateau
        Rectangle rect = new Rectangle(0, 0);
        rect.setFill(Color.ORANGE);
        rect.setStroke(Color.BLACK);
        group.getChildren().add(rect);  //ajout dans le group

        //si on clique sur le rectangle deplacer les images
        makeBoardDraggable(rect);
        makeBoardScrollZoom(rect);
        rect.setCursor(Cursor.MOVE); //Change cursor to crosshair

        this.sceneWidth = 1280;
        this.sceneHeight = 720;
        this.totZoom = 1;

        Scene scene = new Scene(group, sceneWidth, sceneHeight);

        rect.widthProperty().bind(scene.widthProperty());
        rect.heightProperty().bind(scene.heightProperty());

        primaryStage.setScene(scene);
        primaryStage.show();
        dessineTemplate(group);
    }

    private void makeBoardDraggable(Rectangle rect) {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        rect.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });

        // --- Shift node calculated from mouse cursor movement
        rect.addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
            double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;
            for (Piece p : pionList) {
                p.moveXY(deltaX, deltaY);
            }

            this.totMoveBoardX += deltaX;
            this.totMoveBoardY += deltaY;

            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    private void resetView() {
        for (Piece p : pionList) {
            p.moveXY(-this.totMoveBoardX, -this.totMoveBoardY);
        }

        while (this.totZoom < 0.95 || this.totZoom > 1.05) {
            if (totZoom < 1) {
                ZoomFactor(this.totZoom);
            } else {
                ZoomFactor(-this.totZoom);
            }
        }

        this.totMoveBoardX = 0;
        this.totMoveBoardY = 0;
        this.totZoom = 1;
    }

    private void makeBoardScrollZoom(Rectangle rect) {

        rect.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            ZoomFactor(deltaY);
        }
        );
    }

    private void makeHitoxScrollZoom(Circle c) {
        c.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            ZoomFactor(deltaY);
        }
        );
    }

    private void ZoomFactor(double delta) {
        double imgX, imgY;
        double zoomFactor = 1.05;

        if (delta < 0) {
            zoomFactor = 2.0 - zoomFactor;
        }

        for (Piece p : pionList) {
            p.zoomFactor(zoomFactor);
        }

        this.totZoom *= zoomFactor;

        this.totMoveBoardX *= zoomFactor;
        this.totMoveBoardY *= zoomFactor;

    }

    private static final class MouseLocation {

        public double x, y;
    }
}
