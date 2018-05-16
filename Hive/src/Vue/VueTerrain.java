package Vue;

import Modele.Insectes.Araignee;
import Modele.Insectes.Cloporte;
import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Insectes.Moustique;
import Modele.Insectes.Reine;
import Modele.Insectes.Sauterelle;
import Modele.Insectes.Scarabee;
import Controleur.Hive;
import Modele.Insectes.Coccinelle;
import Modele.TypeInsecte;
import com.sun.javafx.scene.text.HitInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VueTerrain extends Vue implements ObservateurVue {

    private ArrayList<PionPlateau> pieceList;
    private ArrayList<PionMain> joueurBlanc;
    private ArrayList<PionMain> joueurNoir;
    private ArrayList<ImageView> hintZones;
    private Hive controleur;
    private Piece currentSelected;
    private int sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau
    private ArrayList<BorderPane> hudElems;

    private Group root;
    private Stage primaryStage;

    VueTerrain(Stage primaryStage, Hive controleur, int casJoueurs) {
        boolean fs = primaryStage.isFullScreen();
        this.primaryStage = primaryStage;
        root = new Group();

        this.controleur = controleur;
        this.controleur.reset();
        this.controleur.setJoueurs(casJoueurs, true);

        this.hintZones = new ArrayList<>();
        this.joueurBlanc = new ArrayList<>(); //todo coordonnée point
        this.joueurNoir = new ArrayList<>();
        hudElems = new ArrayList<>();
        this.currentSelected = null; //aucune piece selectionnée
        this.sceneWidth = 1280; //taille de base
        this.sceneHeight = 720;
        this.totZoom = 1;
        Scene s = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        demarrer(s);

        makeSceneResizeEvent(s);//Window resize event

        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();

        //dessineTemplate();
        BorderPane p = getHudDroite();
        p.minWidth(150);
        p.setLayoutX(-155);
        p.prefHeightProperty().bind(s.heightProperty());
        p.translateXProperty().bind(s.widthProperty());

        BorderPane ctrlView = getHudGauche();
        ctrlView.minHeight(220);
        ctrlView.setLayoutY(-320);
        ctrlView.translateYProperty().bind(s.heightProperty());

        /*liste d'insect*/
        ArrayList<Insecte> initInsectes = new ArrayList<>();

        initInsectes = this.controleur.mainsInit();

        BorderPane playerOne = getHudPlayer(getnbInsect(initInsectes), 1);

        playerOne.minWidthProperty().bind(s.widthProperty());
        playerOne.maxWidthProperty().bind(s.widthProperty());

        BorderPane playerTwo = getHudPlayer(getnbInsect(initInsectes), 2);
        playerTwo.minWidthProperty().bind(s.widthProperty());
        playerTwo.maxWidthProperty().bind(s.widthProperty());

        playerTwo.setLayoutY(-110);
        playerTwo.translateYProperty().bind(s.heightProperty());

        root.getChildren().addAll(p, ctrlView, playerOne, playerTwo);
        this.hudElems.add(ctrlView);
        this.hudElems.add(playerOne);
        this.hudElems.add(playerTwo);
        this.hudElems.add(p);
        //faire au clic passer devant HUD
        hudToFront();

    }

    public HashMap<TypeInsecte, Integer> getnbInsect(ArrayList<Insecte> a) {
        HashMap<TypeInsecte, Integer> m = new HashMap<>();
        for (Insecte insecte : a) {
            TypeInsecte ti = getTypeFromClass(insecte);
            if (m.containsKey(ti)) {
                int v = m.get(ti).intValue() + 1;
                m.put(getTypeFromClass(insecte), new Integer(v));
            } else {
                m.put(ti, 1);
            }
        }
        return m;
    }

    public TypeInsecte getTypeFromClass(Insecte i) {
        if (i instanceof Araignee) {
            return TypeInsecte.ARAIGNEE;
        } else if (i instanceof Coccinelle) {
            return TypeInsecte.COCCINELLE;
        } else if (i instanceof Fourmi) {
            return TypeInsecte.FOURMI;
        } else if (i instanceof Reine) {
            return TypeInsecte.REINE;
        } else if (i instanceof Sauterelle) {
            return TypeInsecte.SAUTERELLE;
        } else if (i instanceof Scarabee) {
            return TypeInsecte.SCARABEE;
        } else if (i instanceof Cloporte) {
            return TypeInsecte.CLOPORTE;
        } else if (i instanceof Moustique) {
            return TypeInsecte.MOUSTIQUE;
        }
        return TypeInsecte.ARAIGNEE;
    }

    public void hudToFront() {
        for (BorderPane p : hudElems) {
            p.toFront();
        }
    }

    public void demarrer(Scene s) {
        this.pieceList = new ArrayList<>();
        this.hintZones = new ArrayList<>();
        this.joueurBlanc = new ArrayList<>(); //todo coordonnée point
        this.joueurNoir = new ArrayList<>();
        this.currentSelected = null; //aucune piece selectionnée
        this.sceneWidth = 1280; //taille de base
        this.sceneHeight = 720;
        this.totZoom = 1;

        //creation du plateau
        Rectangle rect = new Rectangle(0, 0);
        Image img = new Image("background.jpg");
        rect.setFill(new ImagePattern(img));
        root.getChildren().add(rect);  //ajout dans le group
        makeBoardDraggable(rect);   //si on clique sur le rectangle deplacer les images
        makeBoardScrollZoom(rect);  //si on zoom scroll sur le plateau (rectangle) appliquer le zoom
        rect.setCursor(Cursor.MOVE); //Change cursor to crosshair
        rect.widthProperty().bind(s.widthProperty());
        rect.heightProperty().bind(s.heightProperty());
        makeSceneResizeEvent(s);//Window resize event

        //addPiece("piontr_black_cloporte.png", root, 0, 0);
    }

    public void updateMainJoueur() { //liste d'insect en param

        //if (ins2.getJoueur().isWhite()) {
        if (true) {
            this.joueurBlanc.clear();
        } else { //mise a jour joueur noir
            this.joueurNoir.clear();
        }
    }

    private BorderPane getHudPlayer(HashMap<TypeInsecte, Integer> m, int numplayer) {

        Button bEdit = new Button();
        bEdit.setGraphic(new ImageView(new Image("icons/pencil.png")));
        bEdit.setStyle("-fx-background-color: Transparent;\n");
        Text txt1 = new Text("Nom joueur " + numplayer);
        //txt1.setFont(FontWeight.BOLD, 70);
        txt1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        txt1.setFill(Color.WHITE);

        HBox hName = new HBox();
        hName.setAlignment(Pos.CENTER_LEFT);
        hName.getChildren().addAll(bEdit, txt1);
        //hName.setStyle("-fx-background-color:#FFFFFF;");

        HBox pointJ1 = new HBox();

        for (Map.Entry<TypeInsecte, Integer> entry : m.entrySet()) {
            BorderPane bp = new BorderPane();
            PionMain pm;
            Text t;

//            if (entry.getValue().intValue() > 1) {
//                  t = new Text("" + entry.getValue().intValue());
//            } else {
//                t = new Text(" ");
//            }
            t = new Text("" + entry.getValue().intValue());

            if (numplayer == 2) {
                pm = new PionMain(entry.getKey(), entry.getValue().intValue(), false, t);
            } else {
                pm = new PionMain(entry.getKey(), entry.getValue().intValue(), true, t);
            }
            pm.addObserver(this);
            ImageView imgv = pm.getImgPion();
            imgv.setFitWidth(imgv.getImage().getWidth() / 4.5);
            imgv.setFitHeight(imgv.getImage().getHeight() / 4.5);

            t.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
            t.setFill(Color.WHITE);
            BorderPane.setAlignment(t, Pos.CENTER);
            if (numplayer == 2) {
                bp.setTop(t);
            } else {
                bp.setBottom(t);
            }

            bp.setCenter(imgv);
            pointJ1.getChildren().add(bp);
        }

        pointJ1.setAlignment(Pos.CENTER);

        pointJ1.setPadding(
                new Insets(5, 0, 5, 0));

        //pointJ1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");
        bEdit.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> {
                    TextInputDialog ti = new TextInputDialog(txt1.getText());
                    ti.setHeaderText("Enter your name");
                    Optional<String> result = ti.showAndWait();
                    if (result.isPresent()) {
                        txt1.setText(result.get());
                    }
                }
        );

        String style = "-fx-background-color: rgba(255, 255, 255, 0.2);";

        hName.setStyle(style);

        pointJ1.setStyle(style);

        HBox Space = new HBox();

        Space.setMinWidth(
                155);
        Space.setMaxWidth(
                155);
        hName.setMinWidth(
                155);
        hName.setMaxWidth(
                155);

        BorderPane b = new BorderPane();

        b.setLeft(hName);

        b.setCenter(pointJ1);

        b.setRight(Space);

        return b;
    }

    private BorderPane getHudDroite() {

        Button bPause = new Button();

        bPause.setGraphic(new ImageView(new Image("icons/pause.png")));
        bPause.setMinSize(100, 100);
        // bPause.setStyle("-fx-background-color: Transparent;\n");

        Button bSave = new Button();
        Button bLoad = new Button();

        bSave.setGraphic(new ImageView(new Image("icons/diskette.png")));
        bSave.setMinSize(32, 32);
        //bSave.setStyle("-fx-background-color: Transparent;\n");
        bLoad.setGraphic(new ImageView(new Image("icons/upload.png")));
        bLoad.setMinSize(32, 32);
        // bLoad.setStyle("-fx-background-color: Transparent;\n");

        HBox hb = new HBox();
        hb.setAlignment(Pos.BOTTOM_CENTER);
        hb.setSpacing(10);
        hb.getChildren().addAll(bSave, bLoad);

        Button bUndo = new Button();
        Button bRedo = new Button();
        Button bSug = new Button();

        bUndo.setGraphic(new ImageView(new Image("icons/icon.png")));
        bUndo.setMinSize(32, 32);
        // bUndo.setStyle("-fx-background-color: Transparent;\n");
        bRedo.setGraphic(new ImageView(new Image("icons/redo-arrow.png")));
        bRedo.setMinSize(32, 32);
        // bRedo.setStyle("-fx-background-color: Transparent;\n");
        bSug.setGraphic(new ImageView(new Image("icons/small-light-bulb.png")));
        bSug.setMinSize(32, 32);
        // bSug.setStyle("-fx-background-color: Transparent;\n");

        HBox hb1 = new HBox();
        hb1.setAlignment(Pos.BOTTOM_CENTER);
        hb1.setSpacing(10);
        hb1.getChildren().addAll(bUndo, bRedo);

        VBox vb1 = new VBox();
        vb1.getChildren().addAll(hb1, bSug);
        vb1.setSpacing(10);
        //vb1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");
        vb1.setAlignment(Pos.BOTTOM_CENTER);

        VBox vb = new VBox();
        vb.setAlignment(Pos.BOTTOM_CENTER);
        vb.getChildren().addAll(bPause, hb);
        vb.setSpacing(50);

        BorderPane pDroite = new BorderPane();
        pDroite.setPadding(new Insets(15, 10, 15, 10));
        pDroite.setTop(vb);
        pDroite.setBottom(vb1);
        String style = "-fx-background-color: rgba(255, 255, 255, 0.2);";
        pDroite.setStyle(style);

        pDroite.getStylesheets().add("Vue/button.css");

        bPause.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            getPause();
        });

        return pDroite;
    }

    public void getPause() {
        /*Rectangle r = new Rectangle(primaryStage.getWidth(),primaryStage.getHeight(),Color.BLACK);
        r.setOpacity(0.5);
        r.heightProperty().bind(primaryStage.getScene().heightProperty());
        r.widthProperty().bind(primaryStage.getScene().widthProperty());*/

        Text t = new Text("PAUSE");
        t.setFont(Font.font(60));
        t.setStyle("-fx-fill: white;\n");
        Button bResume = new Button("Resume");
        bResume.setMaxWidth(200);
        Button bRules = new Button("Rules");
        bRules.setMaxWidth(200);
        Button bRestart = new Button("Restart");
        bRestart.setMaxWidth(200);
        Button bSettings = new Button("Settings");
        bSettings.setMaxWidth(200);
        Button bMain = new Button("Back main menu");
        bMain.setMaxWidth(200);
        Button bQuit = new Button("Quit");
        bQuit.setMaxWidth(200);

        VBox menu = new VBox();
        menu.getChildren().addAll(t, bResume, bRules, bRestart, bSettings, bMain, bQuit);
        menu.setMinSize(width, heigth);
        menu.prefHeightProperty().bind(primaryStage.getScene().heightProperty());
        menu.prefWidthProperty().bind(primaryStage.getScene().widthProperty());
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        menu.getStylesheets().add("Vue/button.css");
        menu.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");

        bResume.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().removeAll(menu);
        });

        bMain.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want back to main menu ?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                new VueMenuPrincipal(this.primaryStage);
            }
        });

        bQuit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want quit the game ?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                System.exit(0);
            }
        });

        root.getChildren().addAll(menu);
    }

    private BorderPane getHudGauche() {

        Button bZoom = new Button();
        Button bDeZoom = new Button();

        bZoom.setGraphic(new ImageView(new Image("icons/zoom-in.png")));
        bZoom.setCursor(Cursor.HAND);
        //bZoom.setStyle("-fx-background-color: Transparent;\n");
        bZoom.setMinSize(32, 32);
        bDeZoom.setGraphic(new ImageView(new Image("icons/zoom-out.png")));
        bDeZoom.setCursor(Cursor.HAND);
        bDeZoom.setMinSize(32, 32);
        // bDeZoom.setStyle("-fx-background-color: Transparent;\n");

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(bZoom, bDeZoom);
        hb3.setPadding(new Insets(0, 0, 15, 0));
        hb3.setSpacing(10);

        hb3.setAlignment(Pos.CENTER);
        Button bUp = new Button();
        Button bLeft = new Button();
        Button bDown = new Button();
        Button bRight = new Button();
        Button breset = new Button();

        bUp.setGraphic(new ImageView(new Image("icons/up-arrowhead-in-a-circle.png")));
        //bUp.setStyle("-fx-background-color: Transparent;\n");
        bUp.setMinSize(24, 24);
        bUp.setCursor(Cursor.HAND);
        bRight.setGraphic(new ImageView(new Image("icons/right-arrow-in-circular-button.png")));
        bRight.setMaxSize(24, 24);
        bRight.setCursor(Cursor.HAND);
        //bRight.setStyle("-fx-background-color: Transparent;\n");
        bDown.setGraphic(new ImageView(new Image("icons/down-arrowhead-in-a-circle.png")));
        bDown.setMaxSize(24, 24);
        bDown.setCursor(Cursor.HAND);
        // bDown.setStyle("-fx-background-color: Transparent;\n");
        bLeft.setGraphic(new ImageView(new Image("icons/left-arrow-in-circular-button.png")));
        bLeft.setMaxSize(24, 24);
        bLeft.setCursor(Cursor.HAND);
        // bLeft.setStyle("-fx-background-color: Transparent;\n");
        breset.setGraphic(new ImageView(new Image("icons/target.png")));
        breset.setMaxSize(24, 24);
        breset.setCursor(Cursor.HAND);
        //  breset.setStyle("-fx-background-color: Transparent;\n");

        HBox hb = new HBox();
        hb.getChildren().addAll(bLeft, breset, bRight);

        VBox vb = new VBox();
        vb.getChildren().addAll(bUp, hb, bDown);
        vb.setAlignment(Pos.CENTER);

        BorderPane bgauche = new BorderPane();
        bgauche.setTop(hb3);
        bgauche.setBottom(vb);
        bgauche.getStylesheets().add("Vue/button.css");

        bgauche.setPadding(new Insets(0, 0, 15, 15));

        double moveRangeXY = 40;
        double ZoomRangePM = 3; //equivaut a 3 zoom

        breset.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> {
                    resetView();
                }
        );

        bLeft.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(moveRangeXY, 0);
                }
        );
        bRight.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(-moveRangeXY, 0);
                }
        );
        bUp.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(0, moveRangeXY);
                }
        );
        bDown.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    applyBoardMove(0, -moveRangeXY);
                }
        );
        bZoom.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        ZoomFactor(20);
                    }
                }
        );
        bDeZoom.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        ZoomFactor(-20);
                    }
                }
        );

        return bgauche;
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
        //System.out.println(h.intValue());
        updateTranslationPiece();
    }

    private void updateTranslationPiece() {
        for (PionPlateau p : pieceList) {
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
        for (PionPlateau p : pieceList) {
            p.moveXYBoard(dx, dy);
        }
        this.totMoveBoardX += dx;
        this.totMoveBoardY += dy;
    }

    private void resetView() {
        for (PionPlateau p : pieceList) {
            p.moveXYBoard(-this.totMoveBoardX, -this.totMoveBoardY);
//            p.zoomFactor(this.totZoom);
//            updateZoom(p, this.totZoom);
//            p.updateHitBoxPos();
        }
        while (this.totZoom < 0.99 || this.totZoom > 1.01) {
            if (totZoom < 1) {
                ZoomFactor(this.totZoom);
            } else {
                ZoomFactor(-this.totZoom);
            }
        }

        this.totMoveBoardX = 0;
        this.totMoveBoardY = 0;
        this.totZoom = 1;
        hudToFront();
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
        removeHint();
        //bug de mise a jour des positions des hitbox si vide
        if (!pieceList.isEmpty()) {
            double zoomFactor = 1.05;
            if (delta < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            for (PionPlateau p : pieceList) {
                p.zoomFactor(zoomFactor);
                updateZoom(p, zoomFactor);
            }
            this.totZoom *= zoomFactor;
            this.totMoveBoardX *= zoomFactor;
            this.totMoveBoardY *= zoomFactor;
        }

    }

    @Override
    public void updateMove(PionPlateau p, double deltaX, double deltaY, boolean isBoardMove) { //on deplace un seul pion
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

    public void checkCollision(PionPlateau p) {
        for (PionPlateau autrePiece : pieceList) {
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

    public void updateZoom(PionPlateau p, double zoomFactor) {
        for (PieceHitbox pieceh : p.getPieceHitboxList()) {
            pieceh.updateCoordZoom(zoomFactor);
        }
    }

    private boolean collisionHitbox(Piece p, PieceHitbox ph) {
        //C1 with center (x1,y1) and radius r1;
        //C2 with center (x2,y2) and radius r2.
        //(x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2

        double imgWidthRadius = (p.getImgPion().getFitWidth() / 3) * totZoom;
        double imgx = p.getImgPion().getX();
        double imgy = p.getImgPion().getY();

        double r1 = (ph.getPosX() - imgx);
        double r2 = (imgy - ph.getPosY());
        double r3 = (imgWidthRadius + (75 * totZoom));  //75 radius du point d'encrage
        r1 *= r1;
        r2 *= r2;
        r3 *= r3;
        return (r1 + r2 <= r3);

    }

    public void removeHint() {
        root.getChildren().removeAll(this.hintZones);
        this.hintZones.clear();
    }

    public void updateMousePressPiece(Piece p) {
        unselectPiece();
        hudToFront();
        this.currentSelected = p;
        displayLibre(p);

    }

    public ImageView showValidPos(double x, double y) {
        Image img = new Image("hint.png");
        ImageView iv = new ImageView();
        iv.setImage(img);
        iv.setCursor(Cursor.HAND);
        iv.setLayoutX(-(img.getWidth() / 2));
        iv.setLayoutY(-(img.getHeight() / 2));
        iv.setTranslateX(sceneWidth / 2);
        iv.setTranslateY(sceneHeight / 2);

        iv.setX(x);
        iv.setY(y);

        iv.setScaleX(iv.getScaleX() * totZoom);
        iv.setScaleY(iv.getScaleY() * totZoom);

        hintZones.add(iv);

        iv.addEventFilter(MouseEvent.MOUSE_ENTERED, (
                final MouseEvent mouseEvent) -> {
            setSelected(iv);
        });

        iv.addEventFilter(MouseEvent.MOUSE_EXITED, (
                final MouseEvent mouseEvent) -> {
            iv.setEffect(null);
        });

        return iv;
    }

    public void addPion(Piece p, PieceHitbox hitbox) { //ajoute un pion de la main au plateau
        if (p.decrNbPion() >= 0) {
            PionPlateau newp = new PionPlateau(p.getPionsType(), sceneWidth, sceneHeight, 1, p.isWhite());
            newp.addObserver(this);
            pieceList.add(newp);
            newp.zoomFactor(this.totZoom);
            updateZoom(newp, this.totZoom);
            root.getChildren().add(newp.getImgPion());
            newp.snap(hitbox);
            unselectPiece();
        }
    }

    public void displayLibre(Piece p) {
        // removeHint();

        if (this.hintZones.isEmpty()) {
            //première piece sur le plateau
            if (pieceList.isEmpty()) {
                ImageView iv = showValidPos(0.0, 0.0);

                iv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                        final MouseEvent mouseEvent) -> {

                    PieceHitbox hitbox = new PieceHitbox(p, 0);
                    addPion(p, hitbox); //premier pion du plateau position 0 0
                    //newp.snap(hb);
                    //newp.moveToXY(0, 0);

                });

            } else {

                if (this.hintZones.isEmpty()) {
                    for (PionPlateau autrePiece : this.pieceList) {
                        if (!autrePiece.equals(p)) { //si c'est pas la meme piece
                            //alors on explore les cercles (hitbox) LIBRE de cette piece et on test la collision
                            for (PieceHitbox hitbox : autrePiece.getPieceHitboxList()) {
                                if (hitbox.isLibre()) { //si elle est libre
                                    double x = hitbox.getPosX();
                                    double y = hitbox.getPosY();
                                    ImageView iv = showValidPos(x, y);

                                    iv.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                                            final MouseEvent mouseEvent) -> {
                                        if (p instanceof PionMain) {  //ajout d'un pion depuis la main avec un plateau non vide
                                            addPion(p, hitbox);
                                        } else {  //mise a jour d'un pion deja sur le plateau a une nouvelle position
                                            p.snap(hitbox);
                                            unselectPiece();
                                        }

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

                }
            }
            root.getChildren().addAll(hintZones);
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
