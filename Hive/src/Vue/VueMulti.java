package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class VueMulti extends Vue {

    private static int NB_COL = 7;

    VueMulti(Stage primaryStage) {
        Properties prop = new Properties();
        String propFileName = "rsc/config.properties";
        InputStream input = null;
        try {
            input = new FileInputStream(propFileName);
            prop.load(input);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }

        boolean fs = primaryStage.isFullScreen();
        Label t = new Label(getLangStr("deuxjoueur"));
        t.setFont(Font.font("Georgia", FontWeight.BOLD, 70));
        t.setStyle("-fx-border-width: 0 0 3 0; -fx-border-color: rgb(37, 19, 7);");
        t.setTextFill(Color.rgb(37, 19, 7));

        GridPane root = new GridPane();

        for (int column = 0; column < NB_COL; column++) {
            root.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth() / NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 1; row++) {
            root.getRowConstraints().add(new RowConstraints(primaryStage.getHeight(), Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        Label tp = new Label(getLangStr("white"));
        tp.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        tp.setTextFill(Color.WHITE);
        tp.setMinWidth(50);
        TextField ta = new TextField(prop.getProperty("joueurBlanc"));
        ta.setPromptText(getLangStr("whitePlayer"));
        ta.setMaxSize(200.0, 5.0);
        HBox hb = new HBox(tp, ta);
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(10,0,0,0));

        HBox hb1 = new HBox();
        Label tp1 = new Label(getLangStr("black"));

        double widthtp = tp.getWidth();
        double widthtp1 = tp1.getWidth();
        if (widthtp > widthtp1) {
            tp1.setMinWidth(widthtp);
        } else {
            tp.setMinWidth(widthtp1);
        }

        tp1.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        tp1.setTextFill(Color.WHITE);
        tp1.setMinWidth(50);
        TextField ta1 = new TextField(prop.getProperty("joueurNoir"));
        ta1.setPromptText(getLangStr("blackPlayer"));
        ta1.setMaxSize(300.0, 5.0);
        hb1.getChildren().addAll(tp1, ta1);
        hb1.setSpacing(12);
        hb1.setAlignment(Pos.CENTER);

        Button bplay = new Button(getLangStr("jouer"));
        bplay.setMinSize(200.0, 50.0);
        bplay.setAlignment(Pos.CENTER);

        Label tc = new Label(getLangStr("pname"));
        tc.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        tc.setTextFill(Color.WHITE);

        VBox vb2 = new VBox();
        vb2.getChildren().addAll(tc, hb, hb1);
        vb2.setSpacing(5);

        Button bBack = new Button(getLangStr("back"));
        bBack.setAlignment(Pos.CENTER_LEFT);
        bBack.setMinSize(200.0, 50.0);
        bBack.setAlignment(Pos.CENTER);

        bBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        bplay.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            prop.setProperty("joueurNoir", ta1.getText());
            prop.setProperty("joueurBlanc", ta.getText());
            try {
                prop.store(new FileWriter("rsc/config.properties"), "");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            SceneTerrain(primaryStage, 1, false, false);
        });

        GridPane g = new GridPane();
        g.add(bplay, 0, 2);
        g.add(bBack, 1, 2);
        g.add(vb2, 0, 0, 2, 2);
        g.setHgap(30.0);
        g.setVgap(25.0);

        VBox vb = new VBox();
        vb.getChildren().addAll(t, g);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(100.0);
        vb.setPrefHeight(primaryStage.getHeight());
        vb.setStyle("-fx-background-color : rgba(123,67,36, 0.2);  -fx-border-width: 0 0 0 0;");
        vb.setPadding(new Insets(0, 20, 0, 20));

        root.add(vb, 2, 0, 3, 1);

        Scene s = new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        s.getStylesheets().add("Vue/button.css");
        root.prefHeightProperty().bind(s.heightProperty());
        root.prefWidthProperty().bind(s.widthProperty());
        root.setStyle("-fx-background-image: url(backPions2.jpg); -fx-background-size: cover;");
        primaryStage.setScene(s);
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN));
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(fs);
        primaryStage.show();
    }

}
