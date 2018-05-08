
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Vue extends Application implements Observateur {

    ArrayList<Piece> pieceList;
    Map<Piece, ArrayList<PieceHitbox>> hitboxList;  //pour une piece j'ai les points hitbox coté

    private double sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void setImgListMouseEvent() {
        for (Piece p : this.pieceList) {
            p.getImgv().setCursor(Cursor.HAND);
            p.makeDraggable();
        }
    }

    private void initCornerHitbox(Piece p, Group g) {
        ArrayList<PieceHitbox> a = new ArrayList<>();

        double x, y;
        for (int i = 0; i < 6; i++) {
            PieceHitbox pieceHitbox;

            pieceHitbox = new PieceHitbox(p, i);  //10 10 la postion de l'image voisine
            pieceHitbox.setCenterOfHitbox(totZoom);
            pieceHitbox.setCenterOfImageHitbox(totZoom);
//            Circle c = new Circle(pieceHitbox.getPosX(), pieceHitbox.getPosY(), 5);
//            c.setTranslateX(sceneWidth / 2);
//            c.setTranslateY(sceneHeight / 2);
//            g.getChildren().add(c);

            if (this.pieceList.size() == 1) { //mettre les coordonnées a 0 0 pour la première piece
                int result[] = getHitboxCoord(i, p.getX(), p.getY(), p.getZ());
                pieceHitbox.setXYZ(result[0], result[1], result[2]);
            } else {
                pieceHitbox.setXYZ(47, 47, 47); //undefine pour les autres depend du snapping
            }
            a.add(pieceHitbox);
        }

        this.hitboxList.put(p, a);

        for (PieceHitbox piece : this.hitboxList.get(p)) {
            Circle c = piece.getHitbox();
            c.setTranslateX(sceneWidth / 2);
            c.setTranslateY(sceneHeight / 2);
            makeHitoxScrollZoom(c);
            g.getChildren().add(c);
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

    private void addImage(String imgName, Group g, double x, double y) {
        Piece p = new Piece(imgName, g, sceneWidth, sceneHeight);
        if (this.pieceList.size() == 0) { //la premiere piece jouer possède les coordonnées 0 0
            p.setXYZ(0, 0, 0);
        } else {
            p.setXYZ(47, 47, 47);
        }
        p.addObserver(this);
        pieceList.add(p);
        initCornerHitbox(p, g);
        p.moveToXY(x, y);
        g.getChildren().add(p.getImgv());
    }

    private void dessineTemplate(Group g) {
        //création des images

        Line l1 = new Line(0, 0, sceneWidth, sceneHeight);
        Line l2 = new Line(0, sceneHeight, sceneWidth, 0);

        g.getChildren().addAll(l1, l2);

        addImage("hive1.png", g, -800, 0);
        addImage("hive1.png", g, -600, 0);
        addImage("hive1.png", g, -400, 0);
        addImage("hive1.png", g, -200, 0);
        addImage("hive1.png", g, -0, 0);
        addImage("hive2.png", g, 200, 0);
        addImage("hive2.png", g, 400, 0);
        addImage("hive3.png", g, 600, 0);
        addImage("hive3.png", g, 800, 0);

        addImage("hive1.png", g, -800, 200);
        addImage("hive1.png", g, -600, 200);
        addImage("hive1.png", g, -400, 200);
        addImage("hive1.png", g, -200, 200);
        addImage("hive1.png", g, -0, 200);
        addImage("hive2.png", g, 200, 200);
        addImage("hive2.png", g, 400, 200);
        addImage("hive3.png", g, 600, 200);
        addImage("hive3.png", g, 800, 200);

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
        this.pieceList = new ArrayList<>();
        this.hitboxList = new HashMap<>();

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
            for (Piece p : pieceList) {
                p.moveXYBoard(deltaX, deltaY);
            }

            this.totMoveBoardX += deltaX;
            this.totMoveBoardY += deltaY;

            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    private void resetView() {
        for (Piece p : pieceList) {
            p.moveXYBoard(-this.totMoveBoardX, -this.totMoveBoardY);
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
        double zoomFactor = 1.05;

        if (delta < 0) {
            zoomFactor = 2.0 - zoomFactor;
        }

        for (Piece p : pieceList) {
            p.zoomFactor(zoomFactor);
            updateZoom(p, zoomFactor);
        }

        this.totZoom *= zoomFactor;

        this.totMoveBoardX *= zoomFactor;
        this.totMoveBoardY *= zoomFactor;

    }

    @Override
    public void updateMove(Piece p, double deltaX, double deltaY, boolean isBoardMove) { //on deplace un seul pion
        //DEplacer les cercles hitbox
        for (PieceHitbox pieceh : this.hitboxList.get(p)) {
            pieceh.updateCoordMove(deltaX, deltaY);
        }
        if (!isBoardMove) {
            checkCollision(p);
        }
    }

    public void checkCollision(Piece p) {
        //si la piece n'est pas celle la et que le cercle et libre alors l'analyser (si colision placer l'image de la piece correctement + update X et Y)
        //ajouter une boucle pour les autres points...

        for (PieceHitbox pieceh : this.hitboxList.get(p)) {
            Circle c = pieceh.getHitbox(); //pour chaqu'un des cercles de la piece qu'on vient de bouger verfier si collision

            for (Piece autrePiece : this.hitboxList.keySet()) {
                if (!autrePiece.equals(p)) { //si c'est pas la meme piece
                    //alors on explore les cercles (hitbox) LIBRE de cette piece et on test la collision
                    for (PieceHitbox hitbox : this.hitboxList.get(autrePiece)) {
                        if (hitbox.isLibre()) { //si elle est libre
                            //il y a t'il collision ?
                            if (collisionHitbox(c, hitbox.getHitbox())) {
                                //System.out.println("Collision !! avec x:" + hitbox.X + " y:" + hitbox.Y);
                                p.snap(hitbox);
                                updateHitBoxPos(p);
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateHitBoxPos(Piece p) {
        for (int i = 0; i < 6; i++) {
            int result[] = getHitboxCoord(i, p.getX(), p.getY(), p.getZ());
            p.setXYZ(result[0], result[1], result[2]);
        }
    }

    public void updateZoom(Piece p, double zoomFactor) {
        for (PieceHitbox pieceh : this.hitboxList.get(p)) {
            pieceh.updateCoordZoom(zoomFactor);
            //pieceh.setCenterOfHitbox(totZoom);
        }
    }

    private boolean collisionHitbox(Circle c1, Circle c2) {

        //C1 with center (x1,y1) and radius r1;
        //C2 with center (x2,y2) and radius r2.
        //(x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
        double r1 = (c2.getCenterX() - c1.getCenterX());
        r1 *= r1;
        double r2 = (c1.getCenterY() - c2.getCenterY());
        r2 *= r2;
        double r3 = (c1.getRadius() + c2.getRadius());
        r3 *= r3;
        // System.out.println("R1 " + r1);
        // System.out.println("R2 " + r2);

        return (r1 + r2 <= r3);

    }

    private static final class MouseLocation {

        public double x, y;
    }
}