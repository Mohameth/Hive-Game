package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VueMenuPrincipal extends Vue {

    private static int NB_LIGNE = 5;
    private static int NB_COL = 5;
    private Group root;
    private Stage primaryStage;

    VueMenuPrincipal(Stage primaryStage) {
        boolean fs = primaryStage.isFullScreen();

        this.primaryStage = primaryStage;
        root = new Group();
        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add("Vue/button.css");

//        Font f = Font.loadFont(getClass().getClassLoader().getResource("FunSized.ttf").toExternalForm(), 150);
//
//        Label t = new Label("Hive");
//        if (f != null) {
//            t.setFont(f);
//        }
//        t.setTextFill(Color.WHITE);
        Image img = new Image("hivelogo.png");
        ImageView imgv = new ImageView(img);

        BorderPane bp = new BorderPane();
        bp.setCenter(imgv);
        bp.setAlignment(imgv, Pos.CENTER);

        Image imgDeco = new Image("HiveTiles.png");
        ImageView imgv2 = new ImageView(imgDeco);

        BorderPane bpImg = new BorderPane();
        bpImg.setCenter(imgv2);

        Button solo = new Button();
        solo.setText(getLangStr("solo"));
        solo.setMaxWidth(200.0);

        solo.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneSoloParam(primaryStage);
        });

        Button jvj = new Button();
        jvj.setText(getLangStr("deuxjoueur"));
        jvj.setMaxWidth(200.0);

        jvj.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMulti(primaryStage);
        });

        Button loadGame = new Button(getLangStr("ldgame"));
        loadGame.setMaxWidth(200.0);

        loadGame.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneLoad(primaryStage);
        });

        Button tuto = new Button();
        tuto.setText(getLangStr("rule"));
        tuto.setMaxWidth(200.0);

        Button setting = new Button();
        setting.setText(getLangStr("setting"));
        setting.setMaxWidth(200.0);

        setting.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneSettings(primaryStage);
        });

        Button quit = new Button();
        quit.setText(getLangStr("quitter"));
        quit.setMaxWidth(200.0);

        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            getPupExit();
        });

        VBox vb = new VBox(solo, jvj, loadGame, tuto, setting, quit);
        vb.setSpacing(20.0);
        vb.setPadding(new Insets(0, 0, 0, 70));

        GridPane g = new GridPane();
        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }

        g.prefHeightProperty().bind(scene.heightProperty());
        g.prefWidthProperty().bind(scene.widthProperty());
        g.add(bp, 0, 0, 5, 1);
        g.add(vb, 0, 2, 1, 5);
        g.add(bpImg, 3, 3, 2, 3);
        g.setStyle("-fx-background-image: url(background.jpg); -fx-background-size: cover;");

        root.getChildren().addAll(g);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(fs);
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();
    }

    private void getPupExit() {
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
        h.setSpacing(30);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color : rgba(0, 0, 0, .5);");
        h.setPadding(new Insets(20, 0, 10, 0));
        VBox v = new VBox(l, h);
        //v.setSpacing(20);
        v.prefWidthProperty().bind(primaryStage.widthProperty());
        v.prefHeightProperty().bind(primaryStage.heightProperty());
        v.setAlignment(Pos.CENTER);

        y.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            System.exit(0);
        });

        n.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().remove(v);
        });
        root.getChildren().add(v);
    }
}
