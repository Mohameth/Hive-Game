package Vue;

import Controleur.Controleur;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Vue extends Application {
    private static int NB_LIGNE = 5;
    private static int NB_COL = 5;
    private int width = 1280;
    private int heigth = 720;
    private static Controleur controleur;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hive");
        SceneMain(primaryStage);
    }

    private void SceneMain(Stage primaryStage){
        Group root = new Group();
        Scene scene = new Scene(root, width, heigth);

        Text t = new Text("Hive");
        t.setFont(Font.font(100));
        BorderPane bp = new BorderPane();
        bp.setCenter(t);
        scene.getStylesheets().add("Vue/button.css");

        Button solo = new Button();
        solo.setText("Solo");
        solo.setMaxWidth(200.0);

        solo.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneSoloParam(primaryStage);
        });

        Button jvj = new Button();
        jvj.setText("2 Joueurs");
        jvj.setMaxWidth(200.0);

        Button tuto = new Button();
        tuto.setText("Tutoriel");
        tuto.setMaxWidth(200.0);

        Button setting = new Button();
        setting.setText("Paramètres");
        setting.setMaxWidth(200.0);

        Button quit = new Button();
        quit.setText("Quitter");
        quit.setMaxWidth(200.0);

        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            System.exit(0);
        });

        VBox vb = new VBox();
        vb.setSpacing(20.0);
        vb.getChildren().add(solo);
        vb.getChildren().add(jvj);
        vb.getChildren().add(tuto);
        vb.getChildren().addAll(setting,quit);

        GridPane g = new GridPane();
        g.setMinWidth(width);
        g.setMinHeight(heigth);
        //ImageView fond = new ImageView(new Image("sample/hive.jpeg"));
        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }

        //g.add(fond,0,0,5,5);
        g.add(bp,0,0,5,1);
        g.add(vb,4,2,1,5);

        root.getChildren().addAll(g);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SceneSoloParam(Stage primaryStage) {
        Text t = new Text("Singleplayer");
        t.setFont(Font.font(60));

        HBox hb = new HBox();
        Text tp = new Text("Enter your name : ");
        TextArea ta = new TextArea("Player 1");
        ta.setMaxSize(300.0,5.0);
        hb.getChildren().addAll(tp,ta);
        hb.setAlignment(Pos.CENTER);

        Button bplay = new Button("Play");
        bplay.setMinSize(300.0,50.0);

        VBox vb1 = new VBox();
        Text td = new Text("Difficulty :");
        final ToggleGroup group = new ToggleGroup();
        RadioButton rEasy = new RadioButton("Easy");
        RadioButton rMedium = new RadioButton("Medium");
        RadioButton rHard = new RadioButton("Hard");
        rEasy.setToggleGroup(group);
        rMedium.setToggleGroup(group);
        rHard.setToggleGroup(group);
        rMedium.setSelected(true);
        vb1.getChildren().addAll(td,rEasy,rMedium,rHard);

        VBox vb2 = new VBox();
        Text tc = new Text("Choose your color :");
        final ToggleGroup group2 = new ToggleGroup();
        RadioButton rWhite= new RadioButton("White");
        RadioButton rBlack = new RadioButton("Black");
        rBlack.setToggleGroup(group2);
        rWhite.setToggleGroup(group2);
        rWhite.setSelected(true);
        vb2.getChildren().addAll(tc,rWhite,rBlack);

        Button bBack = new Button("back");

        bBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        bplay.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneTerrain(primaryStage);
        });

        GridPane g = new GridPane();
        g.add(bplay,0,0);
        g.add(vb1,1,0);
        g.add(bBack,0,1);
        g.add(vb2,1,1);
        g.setHgap(100.0);
        g.setVgap(50.0);
        g.setAlignment(Pos.CENTER);

        VBox vb = new VBox();
        vb.getChildren().addAll(t,hb,g);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(100.0);

        BorderPane b = new BorderPane();
        b.setTop(vb);

        Scene s = new Scene(b,width,heigth);
        s.getStylesheets().add("Vue/button.css");
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public void SceneTerrain(Stage primaryStage){
        Text txt = new Text("Point du joueur 2");
        HBox pointJ2 = new HBox();
        pointJ2.getChildren().addAll(txt);
        pointJ2.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 0 0 3 0;\n");

        Text txt1 = new Text("Point du joueur 1");
        HBox pointJ1 = new HBox();
        pointJ1.getChildren().addAll(txt1);
        pointJ1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");

        BorderPane b = new BorderPane();
        b.setTop(pointJ2);
        b.setBottom(pointJ1);
        b.setRight(getHudDroite());
        b.setLeft(getHudGauche());

        Scene s = new Scene(b,width,heigth);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public BorderPane getHudDroite(){
        Button bPause = new Button();
        bPause.setGraphic(new ImageView(new Image("Vue/pause.png")));
        bPause.setMinSize(100,100);
        bPause.setStyle("-fx-background-color: Transparent;\n");

        Button bSave = new Button();
        Button bLoad = new Button();
        bSave.setGraphic(new ImageView(new Image("Vue/diskette.png")));
        bSave.setMinSize(32,32);
        bSave.setStyle("-fx-background-color: Transparent;\n");
        bLoad.setGraphic(new ImageView(new Image("Vue/upload.png")));
        bLoad.setMinSize(32,32);
        bLoad.setStyle("-fx-background-color: Transparent;\n");

        HBox hb = new HBox();
        hb.getChildren().addAll(bSave,bLoad);

        Button bUndo = new Button();
        Button bRedo = new Button();
        Button bSug = new Button();
        bUndo.setGraphic(new ImageView(new Image("Vue/icon.png")));
        bUndo.setMinSize(32,32);
        bUndo.setStyle("-fx-background-color: Transparent;\n");
        bRedo.setGraphic(new ImageView(new Image("Vue/redo-arrow.png")));
        bRedo.setMinSize(32,32);
        bRedo.setStyle("-fx-background-color: Transparent;\n");
        bSug.setGraphic(new ImageView(new Image("Vue/small-light-bulb.png")));
        bSug.setMinSize(32,32);
        bSug.setStyle("-fx-background-color: Transparent;\n");
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(bUndo,bRedo);
        VBox vb1 = new VBox();
        vb1.getChildren().addAll(hb1,bSug);
        vb1.setSpacing(10);
        vb1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");
        vb1.setAlignment(Pos.BOTTOM_CENTER);

        VBox vb = new VBox();
        vb.getChildren().addAll(bPause,hb);
        vb.setSpacing(50);

        BorderPane pDroite = new BorderPane();
        pDroite.setTop(vb);
        pDroite.setBottom(vb1);
        pDroite.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 0 0 0 3;\n");
        return pDroite;
    }

    public BorderPane getHudGauche(){
        Button bZoom = new Button();
        Button bDeZoom = new Button();
        bZoom.setGraphic(new ImageView(new Image("Vue/zoom-in.png")));
        bZoom.setStyle("-fx-background-color: Transparent;\n");
        bZoom.setMinSize(32,32);
        bDeZoom.setGraphic(new ImageView(new Image("Vue/zoom-out.png")));
        bDeZoom.setMinSize(32,32);
        bDeZoom.setStyle("-fx-background-color: Transparent;\n");

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(bZoom,bDeZoom);

        Button bUp = new Button();
        Button bLeft = new Button();
        Button bDown = new Button();
        Button bRight = new Button();
        bUp.setGraphic(new ImageView(new Image("Vue/up-arrowhead-in-a-circle.png")));
        bUp.setStyle("-fx-background-color: Transparent;\n");
        bUp.setMinSize(24,24);
        bRight.setGraphic(new ImageView(new Image("Vue/right-arrow-in-circular-button.png")));
        bRight.setMaxSize(24,24);
        bRight.setStyle("-fx-background-color: Transparent;\n");
        bDown.setGraphic(new ImageView(new Image("Vue/down-arrowhead-in-a-circle.png")));
        bDown.setMaxSize(24,24);
        bDown.setStyle("-fx-background-color: Transparent;\n");
        bLeft.setGraphic(new ImageView(new Image("Vue/left-arrow-in-circular-button.png")));
        bLeft.setMaxSize(24,24);
        bLeft.setStyle("-fx-background-color: Transparent;\n");
        ImageView img = new ImageView(new Image("Vue/target.png"));

        HBox hb = new HBox();
        hb.getChildren().addAll(bLeft,img,bRight);

        VBox vb = new VBox();
        vb.getChildren().addAll(bUp,hb,bDown);
        vb.setAlignment(Pos.CENTER);

        BorderPane bgauche = new BorderPane();
        bgauche.setTop(hb3);
        bgauche.setBottom(vb);
        return bgauche;
    }

    public static void initFenetre(String[] args, Controleur c) {
        controleur = c;
        launch(args);
    }
}
