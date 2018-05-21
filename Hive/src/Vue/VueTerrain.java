package Vue;

import Controleur.Hive;
import Modele.Insectes.Insecte;
import Modele.HexaPoint;
import Modele.TypeInsecte;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.effect.ColorAdjust;
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

public class VueTerrain extends Vue implements ObservateurVue {

    private ArrayList<ZoneLibre> listZoneLibres;
    private HashMap<HexaPoint, PionPlateau2> listPionsPlateau;
    private Hive controleur;
    private PionPlateau2 currentSelected;
    private boolean selectionValidee; //l'utilisateur a fait un mouse release sur le pion
    private boolean currentSelectionIsSnapped; //l'utilisateur a fait un mouse release sur le pion
    private PionMain currentMainSelected;
    private int sceneWidth, sceneHeight; //taille de la scene
    private double totZoom;  //zoom actuel du plateau
    private double totMoveBoardX, totMoveBoardY;  //position du plateau
    private ArrayList<BorderPane> hudElems;
    private HashMap<TypeInsecte, PionMain> pionMainPlayer1, pionMainPlayer2;
    private int partieManche = 0;
    private Group root;
    private Stage primaryStage;

    VueTerrain(Stage primaryStage, Hive controleur, int casJoueurs) {
        boolean fs = primaryStage.isFullScreen();
        this.primaryStage = primaryStage;
        root = new Group();

        this.controleur = controleur;
        this.controleur.reset();
        this.controleur.setJoueurs(casJoueurs, true);

        pionMainPlayer1 = new HashMap<>();
        pionMainPlayer2 = new HashMap<>();
        this.listPionsPlateau = new HashMap<>();
        this.listZoneLibres = new ArrayList<>();
        hudElems = new ArrayList<>();
        this.currentSelected = null; //aucune piece selectionnée
        this.currentMainSelected = null; //aucune piece selectionnée
        this.sceneWidth = 1280; //taille de base
        this.sceneHeight = 720;
        this.totZoom = 0.5;
        this.selectionValidee = false;
        this.currentSelectionIsSnapped = false;

        Scene s = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

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

        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();

        //
        //place un pion de la main vers le plateau
//place un pion du plateau vers le plateau
        //sauvegarde coordonnée de départ du pion
        //et les coordonnées cibles
        //this.controleur.deplacementInsecte(pionDeplOrigin, new Point3DH(this.pionDepl.getX(), this.pionDepl.getY(), this.pionDepl.getY()));
        BorderPane p = getHudDroite();
        p.minWidth(150);
        p.setLayoutX(-155);
        p.prefHeightProperty().bind(s.heightProperty());
        p.translateXProperty().bind(s.widthProperty());

        BorderPane ctrlView = getHudGauche();
        ctrlView.minHeight(220);
        ctrlView.setLayoutY(-320);
        ctrlView.translateYProperty().bind(s.heightProperty());

        ArrayList<Insecte> initInsectes = new ArrayList<>();

        initInsectes = this.controleur.mainsInit();
        BorderPane playerOne = getHudPlayer(getnbInsect(initInsectes), 1); //initialisation tout les pions possable
        BorderPane playerTwo = getHudPlayer(getnbInsect(initInsectes), 2);

        playerOne.minWidthProperty().bind(s.widthProperty());
        playerOne.maxWidthProperty().bind(s.widthProperty());
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

        //ctrlGame();
        coupJouer();
    }

    public void displayZoneLibre() {
        System.out.println("Display zone libre");
        //updatePosZoneLibre();
        ArrayList<HexaPoint> zoneLibres = new ArrayList<>();

        if (currentMainSelected != null && currentSelected != null) {
            System.out.println("======================NE DOIT JMAIS ARRIVER==============================");
        }

        if (currentMainSelected != null) {
            System.out.println("Affiche zone libre pion de la main");
            zoneLibres = this.controleur.placementsPossibles(); //lorsqu'on clique sur un pionMain affiche les zones libres
        } else if (currentSelected != null) {
            System.out.println("Affiche zone libre pion plateau");
            zoneLibres = this.controleur.deplacementsPossibles(currentSelected.getCoordPion());
        }

        if (zoneLibres.size() == 1 && zoneLibres.get(0).equals(new HexaPoint(0, 0, 0))) {
            addPremierZoneLibre();
            this.listZoneLibres.get(0).setZoneLibreVisible();
        } else {
            //affiche ceux qu'on a pas trouvé par une boucle:
            for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
                PionPlateau2 pp2 = entry.getValue();
                for (ZoneLibre uneZoneLibre : pp2.getZonesLibresListe()) {
                    if (containsSamePoint(zoneLibres, uneZoneLibre)) {
                        uneZoneLibre.setZoneLibreVisible();
                    }
                }
            }
        }
        hudToFront();
    }

    private boolean containsSamePoint(ArrayList<HexaPoint> aZL, ZoneLibre zone) {
        if (aZL != null) {
            for (HexaPoint p3d : aZL) {
                if (zone.getCoordZoneLibre().equals(p3d)) {
                    aZL.remove(p3d);
                    return true;
                }
            }
        }
        return false;
    }

    public void updatePosZoneLibre() {
        hideZoneLibre();
        System.out.println("Update Zone Libre");
        this.listZoneLibres.clear();
        for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
            PionPlateau2 pp2 = entry.getValue();
            pp2.updateZoneLibreVoisin();
        }
    }

    public void hideZoneLibre() {
        for (ZoneLibre zoneLibre : listZoneLibres) {
            zoneLibre.setZoneLibreCachee();
        }
    }

    public void addPremierZoneLibre() {
        ZoneLibre zl = new ZoneLibre(getZoom(), getWidth(), getHeight()) {
            @Override
            public void setMouseEvent() {
                this.getImage().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                        final MouseEvent mouseEvent) -> {
                    //valider le coup au mouse release:
                    System.out.println("Mon Override premiere zone libre");
                    updateMousePressedZoneLibre(this);
                });
            }
        };
        UpdateAddNewZoneLibre(zl);
    }

    public Group getRoot() {
        return this.root;
    }

    private void zoomImage(double totZoomVar) {
        //limite le zoom a 0.10 min et 2.5 MAX, ne zoom pas si plateau est vide
        if (totZoomVar > 0.10 && totZoomVar < 2.5 && !listPionsPlateau.isEmpty()) {

            for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
                entry.getValue().updateZoomWidthHeight(totZoomVar, this.getWidth(), this.getHeight());
            }

            double zoomFactor = totZoomVar / this.totZoom;
            this.totMoveBoardX *= zoomFactor;
            this.totMoveBoardY *= zoomFactor;
            this.totZoom = totZoomVar;
        }
    }

    private void moveDeltaBoard(double x, double y) {
        for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
            entry.getValue().moveDeltaBoard(x, y);
        }
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
        updateTranslationPiece();
    }

    private void updateTranslationPiece() {
        for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
            entry.getValue().updateZoomWidthHeight(this.totZoom, this.sceneWidth, this.sceneHeight);
        }
    }

    private void makeBoardDraggable(Rectangle rect) {
        MouseLocation lastMouseLocation = new MouseLocation();
        rect.addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            lastMouseLocation.x = mouseEvent.getSceneX(); //sauvegarde des coordonnées initial de la souris
            lastMouseLocation.y = mouseEvent.getSceneY();
            //remove selection
            //removeSelectedPion();
            //hideZoneLibre();
        });

        rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        removeSelectedPion();
                    }
                }
            }
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
        moveDeltaBoard(dx, dy);
        this.totMoveBoardX += dx;
        this.totMoveBoardY += dy;
    }

    private void resetView() {
        moveDeltaBoard(-this.totMoveBoardX, -this.totMoveBoardY);
        zoomImage(0.5);
        this.totMoveBoardX = 0;
        this.totMoveBoardY = 0;
    }

    private void makeBoardScrollZoom(Rectangle rect) {
        rect.setOnScroll((ScrollEvent event) -> {
            applyZoomEvent(event);
        }
        );
    }

    private void removeSelectedPion() {
        if (this.currentSelected != null) {
            this.currentSelected.unSelect();
            this.currentSelected = null;
        }

        if (this.currentMainSelected != null) {
            this.currentMainSelected.unSelect();
            this.currentMainSelected = null;
        }
        hideZoneLibre();

    }

    private void makeImageScrollZoom(ImageView iv) {
        iv.setOnScroll((ScrollEvent event) -> {
            applyZoomEvent(event);
        }
        );
    }

    private void applyZoomEvent(ScrollEvent event) {
        double zoomForce = 0.05;
        double deltaY = event.getDeltaY();
        if (deltaY < 0) {
            zoomImage(this.totZoom - zoomForce);
        } else {
            zoomImage(this.totZoom + zoomForce);

        }
    }

    public double getZoom() {
        return this.totZoom;
    }

    public double getWidth() {
        return this.sceneWidth;
    }

    public double getHeight() {
        return this.sceneHeight;
    }

    public void hudToFront() {
        for (BorderPane p : hudElems) {
            p.toFront();
        }
    }

    @Override
    public void UpdateAddNewZoneLibre(ZoneLibre zLibre) {
        this.listZoneLibres.add(zLibre);
        this.getRoot().getChildren().add(zLibre.getImage());
        makeImageScrollZoom(zLibre.getImage());
        System.out.println("Ok new Zone Libre");
    }

    @Override
    public void UpdateAddNewPionPlateau(PionPlateau2 pionPlateau) {
        //gestion des doublons (cases a la meme place)
        if (this.listPionsPlateau.containsKey(pionPlateau.getCoordPion())) {
            System.out.println("ADD NEW PION doublons");
            pionPlateau.setPionEnDessous(this.listPionsPlateau.get(pionPlateau.getCoordPion()));
            this.getRoot().getChildren().remove(this.listPionsPlateau.get(pionPlateau.getCoordPion()).getImage());
        }
        this.listPionsPlateau.put(pionPlateau.getCoordPion(), pionPlateau);
        this.getRoot().getChildren().add(pionPlateau.getImage()); //ajout de l'image car nouveau pion
        makeImageScrollZoom(pionPlateau.getImage());
        System.out.println("Ok new Pion");
    }

    @Override
    public void UpdateZonLibPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, ZoneLibre zLibre) {
        //System.out.println("Maj des zones libres");
        //useless ? avec l'arraylist
    }

    public void affichePionPlateauList() {
        System.out.println("--------");
        for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
            HexaPoint key = entry.getKey();
            PionPlateau2 pp2 = entry.getValue();
            System.out.println("Key: " + key + " value: ");
            pp2.affiche();

        }
    }

    @Override
    public void UpdatePionPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, PionPlateau2 p) {
        //affichePionPlateauList();
        System.out.println("Maj des pionsPlateau");
        this.listPionsPlateau.remove(oldKeyPoint3D);
        //gestion des pions en dessous

        if (currentSelected.getPionEnDessous() != null) {
            System.out.println("Pion en dessous update position");
            PionPlateau2 oldpp2 = currentSelected.getPionEnDessous();
            //remettre le pion dans le tableau
            if (oldKeyPoint3D.equals(oldpp2.getCoordPion())) {
                System.out.println("Doit etre pareil");
            }
            this.listPionsPlateau.put(oldpp2.getCoordPion(), oldpp2);
            this.getRoot().getChildren().add(oldpp2.getImage());
            //supprime le pion en dessous lors du déplacement
            p.removePionEnDessous();
        }
        //gestion des doublons si un pions deja  a cette position le mettre en dessous
        if (this.listPionsPlateau.containsKey(newPos3D)) {
            System.out.println("UPDATE PION doublons");
            p.setPionEnDessous(this.listPionsPlateau.get(newPos3D));
            this.getRoot().getChildren().remove(this.listPionsPlateau.get(newPos3D).getImage());
        }
        this.listPionsPlateau.put(newPos3D, p);

        // affichePionPlateauList();
    }

    @Override
    public void updatePionPateauMove(PionPlateau2 p) {
        if (p.equals(this.currentSelected)) {
            //valide la selection commence un drag:
            this.selectionValidee = true;
            p.setDragging(true);
            //p.afficheZoneLibre(false);
            System.out.println("Selection");
        }
        System.out.println("Verif collision Move");
    }

    @Override
    public void updatePionPateauMousePress(PionPlateau2 p) {
        //ICI on set la selection mais elle n'est pas validé
        p.affiche();
        //il s'agit d'une selection d'un pion du plateau
        if (this.currentSelected == null) {
            removeSelectedPion();
            this.currentSelected = p;
            this.selectionValidee = false;
            displayZoneLibre();
            p.setToFront();
            hudToFront();
            p.setSelectedEffect();
        }

        //il s'agit d'une cible = ZoneLibre release
        if (this.currentSelected != null && !this.currentSelected.equals(p)) {
            removeSelectedPion();
            this.currentSelected = p;
            this.selectionValidee = false;
            displayZoneLibre();
            p.setToFront();
            hudToFront();
            p.setSelectedEffect();
        }

    }

    @Override
    public void updatePionPateauMouseReleased(PionPlateau2 p) {
        //ICI on verifie la selection

        //on selectionne le pion si dragging = false
        if (p.equals(this.currentSelected) && this.selectionValidee == false) {
            //onclique sur le pion release
            this.selectionValidee = true;
            //System.out.println("Clic Selection");
        } else if (p.equals(this.currentSelected) && this.selectionValidee == true && p.isDragging()) {
            //si snap = ok valide le coup
            //si snap = false on remet a la position d'origine et removeselection
            System.out.println("Fin drag & drop Verif si snap sinon back origine");
            if (this.currentSelectionIsSnapped) {
                System.out.println("Valide le drag and drop");
            } else {
                //retourne sur la position d'origine
                p.goToPrevPos();
            }
        }

    }

    @Override
    public void updateMousePressedZoneLibre(ZoneLibre zLibre) {
        System.out.println("Presse sur zone libre");
        //ICI ON JOUE UN COUP
        //pose un pionPlateau deja sur le plateau sur cette zone
        if (this.currentSelected != null) {
            //updateposition

            //mise a jour du tableau avec les points et zones libres
            System.out.println("Jouer coup plateau -> plateau");
            this.controleur.deplacementInsecte(currentSelected.getCoordPion(), zLibre.getCoordZoneLibre());
            this.currentSelected.setPionPosition(zLibre.getCoordZoneLibre(), zLibre.getImgPosX(), zLibre.getImgPosY());
            this.currentSelected.validCurrentPosXY();
        } else if (this.currentMainSelected != null) {    // un pionMain est selectionnée d'un joueur et on créer un pionPlateau sur le plateau au coordonnée zLibre
            //update add pion, pion ajouté depuis la main
            System.out.println("Jouer coup main -> plateau");
            currentMainSelected.affiche();
            zLibre.affiche();
            //mise a jour du tableau avec les points et zones libres

            this.controleur.joueurPlaceInsecte(currentMainSelected.getPionsType(), zLibre.getCoordZoneLibre());
            PionPlateau2 pp2 = new PionPlateau2(this, currentMainSelected.getPionsType(), currentMainSelected.isWhite(), zLibre.getCoordZoneLibre(), zLibre.getImgPosX(), zLibre.getImgPosY(), this.getZoom(), this.getWidth(), this.getHeight());
            //pp2.setPionPosition(zLibre.getCoordZoneLibre(), zLibre.getImgPosX(), zLibre.getImgPosY());
            pp2.validCurrentPosXY();
        }
        coupJouer();

    }

    @Override
    public void updatePionPlateauAddEnDessous(PionPlateau2 pionPlateau) {
        System.out.println("Add en Dessous");
        //this.getRoot().getChildren().add();
    }

    @Override
    public void updatePionPlateauRemoveEnDessous(PionPlateau2 pionPlateau) {
        //this.getRoot().getChildren().remove();
    }

    private void coupJouer() {
        hideZoneLibre();
        removeSelectedPion();
        updateMainJoueur();
        hudToFront();
        System.out.println("Coup Jouer");
    }

    //Main Pion vers plateau
    public void updateMouseReleasedMainJoueur(PionMain pm) {
        System.out.println("Clic sur main joueur");
        removeSelectedPion();
        this.currentMainSelected = pm;
        pm.setSelectedEffect();
        displayZoneLibre();
    }

    //ANCIEN CODE
    public HashMap<TypeInsecte, Integer> getnbInsect(ArrayList<Insecte> a) {
        HashMap<TypeInsecte, Integer> m = new HashMap<>();
        for (Insecte insecte : a) {
            TypeInsecte ti = insecte.getType();
            if (m.containsKey(ti)) {
                int v;
                v = m.get(ti).intValue() + 1;
                m.put(ti, new Integer(v));
            } else {
                m.put(ti, 1);
            }
        }
        return m;
    }

    public void updateMainJoueur() { //liste d'insect en param
        //joueur 1 = blanc
        //2 = noir
        System.out.println("Verif des données!!");
        HashMap<TypeInsecte, Integer> m;
        //Mise a jour si probleme du texte
        m = getnbInsect(this.controleur.mainJoueur(1));

        for (Map.Entry<TypeInsecte, PionMain> entry : pionMainPlayer1.entrySet()) {
            TypeInsecte keyType = entry.getKey();
            PionMain pMain = entry.getValue();
            if (m.containsKey(keyType)) {
                //verifier les données
                int nbInsects = m.get(keyType);
                if (pMain.getNbPions() != nbInsects) {
                    this.pionMainPlayer1.get(keyType).setNbPion(nbInsects);
                }
            } else { //si n'est pas present le supprimer
                pMain.setNbPion(0);
            }

        }

        m.clear();
        m = getnbInsect(this.controleur.mainJoueur(2));
        for (Map.Entry<TypeInsecte, PionMain> entry : pionMainPlayer2.entrySet()) {
            TypeInsecte keyType = entry.getKey();
            PionMain pMain = entry.getValue();
            if (m.containsKey(keyType)) {
                //verifier les données
                int nbInsects = m.get(keyType);
                if (pMain.getNbPions() != nbInsects) {
                    this.pionMainPlayer2.get(keyType).setNbPion(nbInsects);
                }
            } else { //si n'est pas present le supprimer
                pMain.setNbPion(0);
            }

        }

        System.out.println("----------------------------- NOUVEAU TOUR -----------------------------");
        if (this.controleur.tourJoueurBlanc()) {
            //setlock(true);  //pour griser les pions
            setLockPlayerPion(false); //lock les noirs  sur le plateau  et remove les blancs
            removeLock(true, this.controleur.tousPionsPosables(1));
            setlock(false);
        } else {
            //Mise a jour si probleme du texte
            //setlock(false); //pour griser les pions noir = false
            setLockPlayerPion(true); //lock les blancs sur le plateau et remove les noirs
            removeLock(false, this.controleur.tousPionsPosables(2));
            setlock(true);
        }
    }

    private void removeLock(boolean white, boolean toutPion) {
        HashMap<TypeInsecte, PionMain> m;
        if (white) {
            m = this.pionMainPlayer1;
        } else {
            m = this.pionMainPlayer2;
        }
        for (Map.Entry<TypeInsecte, PionMain> entry : m.entrySet()) {
            TypeInsecte type = entry.getKey();
            PionMain pionMainPlayer = entry.getValue();
            if (toutPion || type == TypeInsecte.REINE) {
                pionMainPlayer.removelock();
            }
        }
        //bloquer les pions du meme joueurs sur le plateau quand il y a que la reine a poser
        //bloque les pions du joueurs en cours tant que la reine n'a pas été joué
        if (!toutPion || !this.controleur.pionsDeplaceables()) {
            setLockPlayerPion(white, false);
        }
    }

    //lock les pions des mains des joueurs
    private void setlock(boolean white) {
        HashMap<TypeInsecte, PionMain> m;
        if (white) {
            m = this.pionMainPlayer1;
        } else {
            m = this.pionMainPlayer2;
        }
        for (Map.Entry<TypeInsecte, PionMain> entry : m.entrySet()) {
            TypeInsecte type = entry.getKey();
            PionMain pionMainPlayer = entry.getValue();
            pionMainPlayer.setlock();

        }
    }

    private void setLockPlayerPion(boolean iswhite) {
        setLockPlayerPion(iswhite, true);
    }

    private void setLockPlayerPion(boolean iswhite, boolean unlockOposite) {
        for (Map.Entry<HexaPoint, PionPlateau2> entry : listPionsPlateau.entrySet()) {
            PionPlateau2 pPlat = entry.getValue();
            if (pPlat.isWhite() == iswhite) {
                pPlat.setLock();
            } else {
                if (unlockOposite) {
                    pPlat.removeLock();
                }
            }

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
        //ajoute les borders panes de chaque pions dans la hbox
        pointJ1.getChildren().addAll(genListPionsMain(m, numplayer));

        pointJ1.setAlignment(Pos.CENTER);
        pointJ1.setPadding(new Insets(5, 0, 5, 0));

        bEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            getPupName(txt1);
        });

        String style = "-fx-background-color: rgba(255, 255, 255, 0.2);";

        hName.setStyle(style);

        pointJ1.setStyle(style);
        HBox Space = new HBox();
        Space.setMinWidth(155);
        Space.setMaxWidth(155);
        hName.setMinWidth(155);
        hName.setMaxWidth(155);
        BorderPane b = new BorderPane();
        b.setLeft(hName);
        b.setCenter(pointJ1);
        b.setRight(Space);
        return b;
    }

    public ArrayList<BorderPane> genListPionsMain(HashMap<TypeInsecte, Integer> m, int numplayer) {
        ArrayList<BorderPane> bps = new ArrayList<>();
        if (numplayer == 1) {
            this.pionMainPlayer1.clear();
        } else {
            this.pionMainPlayer2.clear();
        }
        System.out.println("player: " + numplayer);

        HBox pointJ1 = new HBox();
        for (Map.Entry<TypeInsecte, Integer> entry : m.entrySet()) {
            BorderPane bp = new BorderPane();
            PionMain pm;
            Text t;
            int nbPions = entry.getValue().intValue();
            t = new Text(nbPions + "⨯");
            boolean iswhite = false;
            if (numplayer == 1) {
                iswhite = true;
            }
            pm = new PionMain(this, entry.getKey(), nbPions, iswhite, t, bp);
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
            t.setFill(Color.WHITE);
            BorderPane.setAlignment(t, Pos.CENTER);
            if (numplayer == 2) { //position des numeros des pions en haut et en bas en fct du joueur
                bp.setTop(t);
                pionMainPlayer2.put(entry.getKey(), pm);
            } else {
                bp.setBottom(t);
                pionMainPlayer1.put(entry.getKey(), pm);
            }
            bp.setCenter(pm.getImage());
            bps.add(bp);
        }
        return bps;
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

        bLoad.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            ListView<String> lv = getSaveFile();
            Button load = new Button(getLangStr("load"));
            Button cancel = new Button(getLangStr("cancel"));

            HBox hbutton = new HBox();
            hbutton.getStylesheets().add("Vue/button.css");
            hbutton.getChildren().addAll(load, cancel);
            hbutton.setSpacing(10);
            hbutton.setAlignment(Pos.CENTER);

            VBox vLoad = new VBox();
            vLoad.getChildren().addAll(lv, hbutton);
            vLoad.prefWidthProperty().bind(primaryStage.widthProperty());
            vLoad.prefHeightProperty().bind(primaryStage.heightProperty());
            vLoad.setAlignment(Pos.CENTER);
            vLoad.setSpacing(10);
            vLoad.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
            lv.setMaxWidth((primaryStage.getWidth() * 33) / 100);
            lv.getStylesheets().add("Vue/button.css");

            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
                root.getChildren().removeAll(vLoad);
            });

            load.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
                this.controleur.load(style);
                root.getChildren().removeAll(vLoad);
            });

            root.getChildren().addAll(vLoad);
        });

        bSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            TextField tnom = new TextField("file");
            tnom.setStyle("-fx-font-weight: bold;\n"
                    + "     -fx-font-size: 24px;\n"
                    + "    -fx-background-color: transparent;\n"
                    + "    -fx-text-fill : rgb(255,255,255);");
            ListView<String> lv = getSaveFile();
            Button save = new Button(getLangStr("save"));
            Button cancel = new Button(getLangStr("cancel"));

            HBox htextin = new HBox(tnom);
            htextin.setAlignment(Pos.CENTER);

            HBox hbutton = new HBox();
            hbutton.getStylesheets().add("Vue/button.css");
            hbutton.getChildren().addAll(save, cancel);
            hbutton.setSpacing(10);
            hbutton.setAlignment(Pos.CENTER);

            VBox vLoad = new VBox();
            vLoad.getChildren().addAll(htextin, lv, hbutton);
            vLoad.prefWidthProperty().bind(primaryStage.widthProperty());
            vLoad.prefHeightProperty().bind(primaryStage.heightProperty());
            vLoad.setAlignment(Pos.CENTER);
            vLoad.setSpacing(10);
            vLoad.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
            lv.setMaxWidth((primaryStage.getWidth() * 33) / 100);
            tnom.setMaxWidth((primaryStage.getWidth() * 33) / 100);
            lv.getStylesheets().add("Vue/button.css");

            lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    tnom.setText(newValue);
                }
            });

            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
                root.getChildren().removeAll(vLoad);
            });

            save.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
                this.controleur.save(tnom.getText());
                root.getChildren().removeAll(vLoad);
            });

            root.getChildren().addAll(vLoad);
        });

        return pDroite;
    }

    public void getPause() {
        Text t = new Text("PAUSE");
        t.setFont(Font.font(60));
        t.setStyle("-fx-fill: white;\n");
        Button bResume = new Button(getLangStr("resume"));
        bResume.setMaxWidth(200);
        Button bRules = new Button(getLangStr("rule"));
        bRules.setMaxWidth(200);
        Button bRestart = new Button(getLangStr("restart"));
        bRestart.setMaxWidth(200);
        Button bSettings = new Button(getLangStr("setting"));
        bSettings.setMaxWidth(200);
        Button bMain = new Button(getLangStr("backmenu"));
        bMain.setMaxWidth(200);
        Button bQuit = new Button(getLangStr("quitter"));
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
            root.getChildren().removeAll(menu);
            getPupBackMain();
        });

        bQuit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().removeAll(menu);
            getPupExit();
        });

        bRules.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().removeAll(menu);
            getRule();
        });

        bSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            VueSettings v = new VueSettings(primaryStage, true, root);
            root.getChildren().add(v.getSetting());
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
        double ZoomRangePM = 1; //equivaut a 3 zoom

        breset.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent e) -> {
                    resetView();
                }
        );

        bLeft.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    moveDeltaBoard(moveRangeXY, 0);

                }
        );
        bRight.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    moveDeltaBoard(-moveRangeXY, 0);
                }
        );
        bUp.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    moveDeltaBoard(0, moveRangeXY);
                }
        );
        bDown.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    moveDeltaBoard(0, -moveRangeXY);
                }
        );
        bZoom.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        zoomImage(this.totZoom + 0.10);
                    }
                }
        );
        bDeZoom.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent e) -> {
                    for (int i = 0; i < ZoomRangePM; i++) {
                        zoomImage(this.totZoom - 0.10);
                    }
                }
        );

        return bgauche;
    }

    //toto lors du deplacement verifier collision A activer TODO
    /*
    public void checkCollision(PionPlateau p) {
        for (PionPlateau autrePiece : pieceList) {
            if (!autrePiece.equals(p)) { //si ce n'est pas la meme piece
                //alors on explore les hitbox LIBRE de cette piece et on test la collision
                for (PieceHitbox hitbox : autrePiece.getPieceHitboxList()) {
                    if (hitbox.isLibre()) { //si elle est libre
                        //il y a t'il collision avec un des coins de la pièce qu'on bouge?
                        if (collisionHitbox(p, hitbox)) {
                            p.snap(hitbox);
                        }
                    }
                }
            }
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
     */
    private static final class MouseLocation {

        public double x, y;
    }

    public ListView<String> getSaveFile() {
        String path = System.getProperty("user.dir").concat("/rsc/SAVE");
        System.out.println(path);
        File rep = new File(path);
        ListView<String> listSaveFile = new ListView<>();
        if (rep.length() != 0) {
            for (String s : rep.list()) {
                listSaveFile.getItems().add(s);
            }
        }
        return listSaveFile;
    }

    public void getPupExit() {
        Label l = new Label(getLangStr("quitGame"));
        l.setTextFill(Color.WHITE);
        l.prefWidthProperty().bind(primaryStage.widthProperty());
        l.setAlignment(Pos.CENTER);
        l.setPadding(new Insets(10, 0, 0, 0));
        l.setStyle("-fx-background-color : rgba(0, 0, 0, .5);-fx-font-weight: bold;\n-fx-font-size: 1.1em;\n-fx-text-fill: white;");
        Button y = new Button(getLangStr("oui"));
        y.setPrefWidth(150);
        Button n = new Button(getLangStr("non"));
        n.setPrefWidth(150);

        HBox h = new HBox(y, n);
        h.getStylesheets().add("Vue/button.css");
        h.setSpacing(30);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        h.setPadding(new Insets(20, 0, 10, 0));
        VBox v = new VBox(l, h);
        //v.setSpacing(20);
        v.prefWidthProperty().bind(this.primaryStage.widthProperty());
        v.prefHeightProperty().bind(this.primaryStage.heightProperty());
        v.setAlignment(Pos.CENTER);

        y.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            System.exit(0);
        });

        n.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().remove(v);
            getPause();
        });
        root.getChildren().add(v);
    }

    public void getPupBackMain() {
        Label l = new Label(getLangStr("backMain"));
        l.setTextFill(Color.WHITE);
        l.prefWidthProperty().bind(primaryStage.widthProperty());
        l.setAlignment(Pos.CENTER);
        l.setPadding(new Insets(10, 0, 0, 0));
        l.setStyle("-fx-background-color : rgba(0, 0, 0, .5);-fx-font-weight: bold;\n-fx-font-size: 1.1em;\n-fx-text-fill: white;");
        Button y = new Button(getLangStr("oui"));
        y.setPrefWidth(150);
        Button n = new Button(getLangStr("non"));
        n.setPrefWidth(150);

        HBox h = new HBox(y, n);
        h.getStylesheets().add("Vue/button.css");
        h.setSpacing(30);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        h.setPadding(new Insets(20, 0, 10, 0));
        VBox v = new VBox(l, h);
        //v.setSpacing(20);
        v.prefWidthProperty().bind(this.primaryStage.widthProperty());
        v.prefHeightProperty().bind(this.primaryStage.heightProperty());
        v.setAlignment(Pos.CENTER);

        y.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(this.primaryStage);
        });

        n.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().remove(v);
            getPause();
        });
        root.getChildren().add(v);
    }

    public void getPupName(Text tname) {
        Label l = new Label(getLangStr("name"));
        l.setTextFill(Color.WHITE);
        l.prefWidthProperty().bind(primaryStage.widthProperty());
        l.setAlignment(Pos.CENTER);
        l.setPadding(new Insets(10, 0, 0, 0));
        l.setStyle("-fx-background-color : rgba(0, 0, 0, .5);-fx-font-weight: bold;\n-fx-font-size: 1.1em;\n-fx-text-fill: white;");
        Button y = new Button("Ok");
        y.setPrefWidth(150);
        Button n = new Button(getLangStr("cancel"));
        n.setPrefWidth(150);

        TextField tf = new TextField(tname.getText());
        HBox hb1 = new HBox(tf);
        tf.setMaxWidth(300);
        hb1.setAlignment(Pos.CENTER);
        hb1.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        hb1.setPadding(new Insets(10, 0, 0, 0));

        HBox h = new HBox(y, n);
        h.getStylesheets().add("Vue/button.css");
        h.setSpacing(30);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        h.setPadding(new Insets(20, 0, 10, 0));
        VBox v = new VBox(l, hb1, h);
        //v.setSpacing(20);
        v.prefWidthProperty().bind(this.primaryStage.widthProperty());
        v.prefHeightProperty().bind(this.primaryStage.heightProperty());
        v.setAlignment(Pos.CENTER);

        y.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            tname.setText(tf.getText());
            root.getChildren().remove(v);
        });

        n.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().remove(v);
        });
        root.getChildren().add(v);
    }

    private void getRule() {
        System.out.println("TODO ne pas utiliser de variables GLOBALE !!!!");
        /*
        int i = 0;
        Label l = new Label(getLangStr("rule"));
        String[] urlImg = new String[20];
        l.setStyle("-fx-font-weight: bold;\n-fx-font-size: 100px;\n-fx-text-fill: white;");

        for (int x = 1; x < 12; x++) {
            urlImg[x - 1] = "rules/rule" + x + ".png";
        }

        ImageView img = new ImageView(new Image(urlImg[i]));
        Button back = new Button(getLangStr("previous"));
        back.setPrefWidth(150);
        Label nbPage = new Label((i + 1) + "/11");
        nbPage.setStyle("-fx-font-weight: bold;\n-fx-font-size: 1.1em;\n-fx-text-fill: white;");
        Button next = new Button(getLangStr("next"));
        next.setPrefWidth(150);
        Button retour = new Button(getLangStr("back"));

        HBox h = new HBox(back, nbPage, next);
        h.setAlignment(Pos.CENTER);
        h.setSpacing(20);
        VBox v = new VBox(l, img, h, retour);
        v.prefHeightProperty().bind(primaryStage.heightProperty());
        v.prefWidthProperty().bind(primaryStage.widthProperty());
        v.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        v.getStylesheets().add("Vue/button1.css");
        v.setAlignment(Pos.CENTER);
        v.setSpacing(15);

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            img.setImage(changeImg(urlImg, false, nbPage));
        });

        next.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            img.setImage(changeImg(urlImg, true, nbPage));
        });

        retour.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().remove(v);
            getPause();
        });

        root.getChildren().add(v);
         */
    }
    /*
    private Image changeImg(String[] url, boolean next, Label l) {

        int i = 0;
        if (next && i < 10) {
            i++;
        } else if (!next && i > 0) {
            i--;
        }
        l.setText((i + 1) + "/11");
        return new Image(url[i]);

    }
     */
}
