package Vue;


import Controleur.Hive;
import Modele.Observateur;
import Modele.Plateau;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class VuePlateau extends Application implements ObservateurPiece, Observateur {

    private ArrayList<Piece> pieceList;
    private static Hive controleur;
    private Map joueurBlanc;
    private Map joueurNoir;
    private ArrayList<ImageView> hintZones;
    private Piece currentSelected;
    private int sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau
    private Group g;

    public static void init(String[] args, Hive c) {
        controleur = c;
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.g = new Group();
        this.pieceList = new ArrayList<>();
        this.hintZones = new ArrayList<>();
        this.joueurBlanc = new HashMap<>(); //todo coordonnée point
        this.joueurNoir = new HashMap<>();
        this.currentSelected = null; //aucune piece selectionnée
        this.sceneWidth = 1280; //taille de base
        this.sceneHeight = 720;
        this.totZoom = 1;

//        Map<String, String> m = new HashMap();
//        m.put("salut", "valeur");
//        m.put("cle2", "valeur2");
//
//        for (Map.Entry<String, String> entry : m.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("key: " + key + " Val:" + value);
//            System.out.println("sssss: " + m.get("cle2"));
//        }
        Scene scene = new Scene(g, sceneWidth, sceneHeight);

        //creation du plateau
        Rectangle rect = new Rectangle(0, 0);
        Image img = new Image("background.jpg");
        rect.setFill(new ImagePattern(img));
        g.getChildren().add(rect);  //ajout dans le group
        makeBoardDraggable(rect);   //si on clique sur le rectangle deplacer les images
        makeBoardScrollZoom(rect);  //si on zoom scroll sur le plateau (rectangle) appliquer le zoom
        rect.setCursor(Cursor.MOVE); //Change cursor to crosshair
        rect.widthProperty().bind(scene.widthProperty());
        rect.heightProperty().bind(scene.heightProperty());

        makeSceneResizeEvent(scene);//Window resize event

        primaryStage.setScene(scene);
        primaryStage.show();
        dessineTemplate();

        /*
        AnimationTimer anim = new AnimationTimer() {
            public void handle(long temps) {
                animHints(temps);
            }
        };
        anim.start();
         */
    }

    //Ajoute le pion sur le plateau G au coordonnée x y.
    private void addPiece(String imgName, Group g, double x, double y) {
        Piece p = new Piece(imgName, sceneWidth, sceneHeight, totZoom);
//        if (this.pieceList.size() == 0) { //Si c'est la première piece alors on lui donne les coordonnées x0 y0
//            p.setXYZ(0, 0, 0);
//        }
        p.addObserver(this);
        pieceList.add(p); //ajoute le pion dans la liste des pieces du plateau
        p.moveToXY(x, y);   //deplace la piece au bonne coordonnée
        g.getChildren().add(p.getImgv()); //ajoute l'image sur le plateau
    }

    //Initialisation de la vue
    private void dessineTemplate() {
        //création des images

        String[] namePiece = new String[]{"araignee", "fourmis", "ladybug", "moustique", "pillbug", "renne", "sauterelles", "scarabée"};
        String[] colorPiece = new String[]{"black", "white"};

        addPiece("piontr_black_pillbug.png", g, 0, 0);

        int x = -1800, y = -1800;
        for (String color : colorPiece) {
            for (String name : namePiece) {
                addPiece("piontr_" + color + "_" + name + ".png", g, x, y);
                x += 470;  //image width + 9
            }
            x = -1800;
            y += 4000;
        }

//        addImage("piontr_black_ladybug.png", g, -800, 0);
//        addImage("piontr_black_pillbug.png", g, 0, 0);
//        addImage("piontr_black_scarabée.png", g, 800, 0);
        double moveRangeXY = 40;
        double ZoomRangePM = 3; //equivaut a 3 zoom
        Button bReset = new Button("Reset view");
        Button bViewLeft = new Button("<- left");
        Button bViewRight = new Button("Right ->");
        Button bViewUP = new Button("UP ^");
        Button bViewDown = new Button("Down v");
        Button bViewZoomIn = new Button("Zoom +");
        Button bViewZoomOut = new Button("Zoom -");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(bReset, bViewLeft, bViewRight, bViewUP, bViewDown, bViewZoomIn, bViewZoomOut);

        bReset.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> {
                    resetView();
                }
        );

        bViewLeft.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(moveRangeXY, 0);
                }
        );
        bViewRight.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(-moveRangeXY, 0);
                }
        );
        bViewUP.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(0, moveRangeXY);
                }
        );
        bViewDown.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(0, -moveRangeXY);
                }
        );
        bViewZoomIn.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        ZoomFactor(20);
                    }
                }
        );
        bViewZoomOut.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        ZoomFactor(-20);
                    }
                }
        );

        g.getChildren().add(hbox);
    }

    private void makeSceneResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                updateWindowWidth(newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                updateWindowHeight(newSceneHeight);
            }
        });
    }

    private void updateWindowWidth(Number w) {
        this.sceneWidth = w.intValue();
        updateTranslationPiece();
    }

    private void updateWindowHeight(Number h) {
        this.sceneHeight = h.intValue();
        System.out.println(h.intValue());
        updateTranslationPiece();
    }

    private void updateTranslationPiece() {
        for (Piece p : pieceList) {
            p.setTranslation(sceneWidth, sceneHeight);
        }
        removeHint();

    }

    private void unselectPiece() {
        if (this.currentSelected != null) {
            this.currentSelected.unSelect();
        }
        removeHint();
    }

    private void makeBoardDraggable(Rectangle rect) {
        MouseLocation lastMouseLocation = new MouseLocation();
        rect.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX(); //sauvegarde des coordonnées initial de la souris
            lastMouseLocation.y = mouseEvent.getSceneY();
            removeHint();
            unselectPiece();
        });

        // --- Shift node calculated from mouse cursor movement
        rect.addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
            double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;
            applyBoardMove(deltaX, deltaY);
            lastMouseLocation.x = mouseEvent.getSceneX();
            lastMouseLocation.y = mouseEvent.getSceneY();
        });
    }

    private void applyBoardMove(double dx, double dy) {
        for (Piece p : pieceList) {
            p.moveXYBoard(dx, dy);
        }
        this.totMoveBoardX += dx;
        this.totMoveBoardY += dy;
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
        }
        if (!isBoardMove) {
            checkCollision(p);
        } else {
            removeHint();
        }
    }

    public void checkCollision(Piece p) {
        for (Piece autrePiece : pieceList) {
            if (!autrePiece.equals(p)) { //si ce n'est pas la meme piece
                //alors on explore les hitbox LIBRE de cette piece et on test la collision
                for (PieceHitbox hitbox : autrePiece.getPieceHitboxList()) {
                    if (hitbox.isLibre()) { //si elle est libre
                        //il y a t'il collision avec un des coins de la pièce qu'on bouge?
                        if (collisionHitbox(p, hitbox)) {
                            p.snap(hitbox);
                            removeHint();
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
        //C1 with center (x1,y1) and radius r1;
        //C2 with center (x2,y2) and radius r2.
        //(x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2

        double imgWidthRadius = (p.getImgv().getFitWidth() / 3) * totZoom;
        double imgx = p.getImgv().getX();
        double imgy = p.getImgv().getY();

        double r1 = (ph.getPosX() - imgx);
        double r2 = (imgy - ph.getPosY());
        double r3 = (imgWidthRadius + (75 * totZoom));  //75 radius du point d'encrage
        r1 *= r1;
        r2 *= r2;
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

    @Override
    public void coupJoue(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void coupsPossibles(ArrayList<Point3D> coups) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static final class MouseLocation {

        public double x, y;
    }
}
