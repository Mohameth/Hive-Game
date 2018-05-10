
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Vue extends Application implements Observateur {

    ArrayList<Piece> pieceList;
    ArrayList<ImageView> hintZones;
    Piece currentSelected;

    private double sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau
    private Group g;

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void setImgListMouseEvent() {
        for (Piece p : this.pieceList) {
            p.getImgv().setCursor(Cursor.HAND);
            p.makeDraggable();
        }
    }

    private void addImage(String imgName, Group g, double x, double y) {
        Piece p = new Piece(imgName, g, sceneWidth, sceneHeight, totZoom);
        if (this.pieceList.size() == 0) { //la premiere piece jouer possède les coordonnées 0 0
            p.setXYZ(0, 0, 0);
        }
        p.addObserver(this);
        pieceList.add(p);
        p.moveToXY(x, y);
        g.getChildren().add(p.getImgv());
    }

    private void dessineTemplate(Group g) {
        //création des images
        Line l1 = new Line(0, 0, sceneWidth, sceneHeight);
        Line l2 = new Line(0, sceneHeight, sceneWidth, 0);
        g.getChildren().addAll(l1, l2);
        String[] namePiece = new String[]{"araignee", "fourmis", "ladybug", "moustique", "pillbug", "renne", "sauterelles", "scarabée"};
        String[] colorPiece = new String[]{"black", "white"};

        addImage("piontr_black_pillbug.png", g, 0, 0);

        int x = -1800, y = -1800;
        for (String color : colorPiece) {
            for (String name : namePiece) {
                addImage("piontr_" + color + "_" + name + ".png", g, x, y);
                x += 470;  //image width + 9
            }
            x = -1800;
            y += 4000;
        }

//        addImage("piontr_black_ladybug.png", g, -800, 0);
//        addImage("piontr_black_pillbug.png", g, 0, 0);
//        addImage("piontr_black_scarabée.png", g, 800, 0);
        //Ajout des events de la souris
        setImgListMouseEvent();

        Button b = new Button("Reset view");

        b.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> {
                    resetView();
                }
        );
        g.getChildren()
                .add(b);
    }

    @Override
    public void start(Stage primaryStage) {

        this.g = new Group();
        this.pieceList = new ArrayList<>();
        this.hintZones = new ArrayList<>();
        this.currentSelected = null;

        //creation du plateau
        Rectangle rect = new Rectangle(0, 0);

        Image img = new Image("background.jpg");
        rect.setFill(new ImagePattern(img));
        rect.setStroke(Color.BLACK);
        g.getChildren().add(rect);  //ajout dans le group

        //si on clique sur le rectangle deplacer les images
        makeBoardDraggable(rect);
        makeBoardScrollZoom(rect);
        rect.setCursor(Cursor.MOVE); //Change cursor to crosshair

        this.sceneWidth = 1280;
        this.sceneHeight = 720;
        this.totZoom = 1;

        Scene scene = new Scene(g, sceneWidth, sceneHeight);
        rect.widthProperty().bind(scene.widthProperty());
        rect.heightProperty().bind(scene.heightProperty());

        primaryStage.setScene(scene);
        primaryStage.show();
        dessineTemplate(g);

        /*
        AnimationTimer anim = new AnimationTimer() {
            public void handle(long temps) {
                animHints(temps);
            }
        };
        anim.start();
         */
    }

    private void unselectPiece() {
        if (this.currentSelected != null) {
            this.currentSelected.unSelect();
        }
        removeHint();
    }

    private void makeBoardDraggable(Rectangle rect) {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        rect.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
            removeHint();
            unselectPiece();

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
            removeHint();
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
        for (PieceHitbox pieceh : p.getPieceHitboxList()) {
            pieceh.updateCoordMove(deltaX, deltaY);
            pieceh.removeSnap();
        }
        if (!isBoardMove) {
            checkCollision(p);
        } else {
            removeHint();
        }
    }

    public void checkCollision(Piece p) {
        //si la piece n'est pas celle la et que le cercle et libre alors l'analyser (si colision placer l'image de la piece correctement + update X et Y)
        //ajouter une boucle pour les autres points...

        for (Piece autrePiece : pieceList) {
            if (!autrePiece.equals(p)) { //si c'est pas la meme piece
                //alors on explore les cercles (hitbox) LIBRE de cette piece et on test la collision
                for (PieceHitbox hitbox : autrePiece.getPieceHitboxList()) {
                    if (hitbox.isLibre()) { //si elle est libre
                        //il y a t'il collision avec un des coins de la pièce qu'on bouge?
                        if (collisionHitbox(p, hitbox)) {
                            removeHint();
                            p.snap(hitbox);
                            p.updateHitBoxPos();
                        }

                    }
                }
            }
        }

    }

    public void updateZoom(Piece p, double zoomFactor) {
        for (PieceHitbox pieceh : p.getPieceHitboxList()) {
            pieceh.updateCoordZoom(zoomFactor);
        }
    }

    private boolean collisionHitbox(Piece p, PieceHitbox ph) {
        double imgWidthRadius = (p.getImgv().getFitWidth() / 3) * totZoom;
        double imgx = p.getImgv().getX();
        double imgy = p.getImgv().getY();

        //C1 with center (x1,y1) and radius r1;
        //C2 with center (x2,y2) and radius r2.
        //(x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
        double r1 = (ph.getPosX() - imgx);
        r1 *= r1;
        double r2 = (imgy - ph.getPosY());
        r2 *= r2;
        double r3 = (imgWidthRadius + (75 * totZoom));  //1 radius du point d'encrage
        r3 *= r3;
        return (r1 + r2 <= r3);

    }

    public void removeHint() {
        g.getChildren().removeAll(this.hintZones);
        this.hintZones.clear();
    }

    public void updateMousePressPiece(Piece p) {

        unselectPiece();
        this.currentSelected = p;

        displayLibre(p);
    }

    public void displayLibre(Piece p) {
        // removeHint();
        Image img = new Image("hint.png");
        if (this.hintZones.isEmpty()) {
            for (Piece autrePiece : this.pieceList) {
                if (!autrePiece.equals(p)) { //si c'est pas la meme piece
                    //alors on explore les cercles (hitbox) LIBRE de cette piece et on test la collision

                    for (PieceHitbox hitbox : autrePiece.getPieceHitboxList()) {

                        if (hitbox.isLibre()) { //si elle est libre

                            ImageView iv = new ImageView();
                            iv.setImage(img);
                            iv.setCursor(Cursor.HAND);
                            iv.setLayoutX(-(img.getWidth() / 2));
                            iv.setLayoutY(-(img.getHeight() / 2));
                            iv.setTranslateX(sceneWidth / 2);
                            iv.setTranslateY(sceneHeight / 2);

                            double x = hitbox.getPosX();
                            double y = hitbox.getPosY();

                            iv.setX(x);
                            iv.setY(y);

                            iv.setScaleX(iv.getScaleX() * totZoom);
                            iv.setScaleY(iv.getScaleY() * totZoom);

                            hintZones.add(iv);

                            iv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                                    final MouseEvent mouseEvent) -> {
                                //this.currentSelected.moveToXY(x, y);
                                p.snap(hitbox);
                                unselectPiece();

                            });

                            iv.addEventFilter(MouseEvent.MOUSE_ENTERED, (
                                    final MouseEvent mouseEvent) -> {
                                setSelected(iv);
                            });

                            iv.addEventFilter(MouseEvent.MOUSE_EXITED, (
                                    final MouseEvent mouseEvent) -> {
                                iv.setEffect(null);
                            });
                        }
                    }
                }
            }
            this.g.getChildren().addAll(hintZones);
        }
    }

    public void setSelected(ImageView iv) {
        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(0.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.90);
        dropShadow.setColor(Color.rgb(0, 255, 0, 0.5));

        iv.setEffect(dropShadow);
    }

    private static final class MouseLocation {

        public double x, y;
    }
}
