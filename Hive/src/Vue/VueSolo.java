package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class VueSolo extends Vue {

    private static int NB_COL = 7;

    VueSolo(Stage primaryStage) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("rsc/config.properties");
            prop.load(input);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }

        boolean fs = primaryStage.isFullScreen();
        Label t = new Label(getLangStr("solo"));
        t.setPrefWidth(width);
        t.setAlignment(Pos.CENTER);
        t.setFont(Font.font("Georgia", FontWeight.BOLD, 80));
        t.setStyle("-fx-border-width: 0 0 3 0; -fx-border-color: rgb(37, 19, 7);");
        t.setTextFill(Color.rgb(37, 19, 7));

        GridPane root = new GridPane();

        for (int column = 0; column < NB_COL; column++) {
            root.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth() / NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 1; row++) {
            root.getRowConstraints().add(new RowConstraints(primaryStage.getHeight(), Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        HBox hb = new HBox();
        Label tp = new Label(getLangStr("entername"));
        tp.setStyle("-fx-font-weight: bold;-fx-font-size: 24px;");
        tp.setTextFill(Color.WHITE);
        System.out.println(prop);
        TextField ta = new TextField(prop.getProperty("joueurBlanc"));

        ta.setMaxSize(300.0, 5.0);
        hb.getChildren().addAll(tp, ta);
        hb.setAlignment(Pos.CENTER);

        Button bplay = new Button(getLangStr("jouer"));
        bplay.setMinSize(200.0, 50.0);

        VBox vb1 = new VBox();
        Label td = new Label(getLangStr("difficulte") + ":");
        td.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        td.setTextFill(Color.WHITE);
        final ToggleGroup group = new ToggleGroup();
        RadioButton rEasy = new RadioButton(getLangStr("easy"));
        rEasy.setUserData("easy");
        rEasy.setTextFill(Color.WHITE);
        RadioButton rMedium = new RadioButton(getLangStr("medi"));
        rMedium.setUserData("medium");
        rMedium.setTextFill(Color.WHITE);
        RadioButton rHard = new RadioButton(getLangStr("hard"));
        rHard.setUserData("hard");
        rHard.setTextFill(Color.WHITE);
        rEasy.setToggleGroup(group);
        rMedium.setToggleGroup(group);
        rHard.setToggleGroup(group);
        if(!group.getToggles().isEmpty()) {
            for (Toggle toggle : group.getToggles()) {
                if (toggle.getUserData().equals(prop.getProperty("difficulteIA")))
                    group.selectToggle(toggle);
            }
        }

        group.selectToggle(rMedium);
        vb1.getChildren().addAll(td, rEasy, rMedium, rHard);
        for (Toggle to : group.getToggles()) {
            if (to.getUserData().equals(prop.getProperty("difficulteIA"))) {
                group.selectToggle(to);
            }
        }

        VBox vb2 = new VBox();
        Label tc = new Label(getLangStr("chColor"));
        tc.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        tc.setTextFill(Color.WHITE);
        final ToggleGroup group2 = new ToggleGroup();
        RadioButton rWhite = new RadioButton(getLangStr("white"));
        rWhite.setUserData("white");
        rWhite.setTextFill(Color.WHITE);
        RadioButton rBlack = new RadioButton(getLangStr("black"));
        rBlack.setUserData("black");
        rBlack.setTextFill(Color.WHITE);
        rBlack.setToggleGroup(group2);
        rWhite.setToggleGroup(group2);
        rWhite.setSelected(true);
        vb2.getChildren().addAll(tc, rWhite, rBlack);

        Button bBack = new Button(getLangStr("back"));
        bBack.setMinSize(200.0, 50.0);

        bBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        bplay.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (group.getSelectedToggle() != null) {
                int difficulte = 0;
                switch (group.getSelectedToggle().getUserData().toString()) {
                    case "easy":
                        difficulte = 2;
                        break;
                    case "medium":
                        difficulte = 3;
                        break;
                    case "hard":
                        difficulte = 4;
                        break;
                }
                if (group2.getSelectedToggle() != null) {
                    switch (group2.getSelectedToggle().getUserData().toString()) {
                        case "black":
                            difficulte = difficulte + 3;
                            prop.setProperty("joueurNoir",ta.getText());
                            break;
                        case "white":
                            prop.setProperty("joueurBlanc",ta.getText());
                            break;
                    }
                    try {
                        prop.store(new FileWriter("rsc/config.properties"),"");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                SceneTerrain(primaryStage, difficulte, true, false);
            }
        });

        GridPane g = new GridPane();
        g.add(bplay, 0, 1);
        g.add(vb1, 0, 0);
        g.add(bBack, 1, 1);
        g.add(vb2, 1, 0);
        g.setHgap(30.0);
        g.setVgap(25.0);
        g.setAlignment(Pos.CENTER);

        VBox vb = new VBox();
        vb.getChildren().addAll(t, hb, g);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(100.0);
        vb.setPrefHeight(primaryStage.getHeight());
        vb.setStyle("-fx-background-color : rgba(123,67,36, 0.2);  -fx-border-width: 0 0 0 0;"); //-fx-border-color: black;

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setBlurType(BlurType.ONE_PASS_BOX);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.80);
        dropShadow.setColor(Color.rgb(84, 46, 25, 0.6));
        vb.setEffect(dropShadow);

        vb.setPadding(new Insets(0, 20, 0, 20));
        root.add(vb, 2, 0, 3, 1);

        Scene s = new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        s.getStylesheets().add("Vue/button.css");
        root.prefHeightProperty().bind(s.heightProperty());
        root.prefWidthProperty().bind(s.widthProperty());
        root.setStyle("-fx-background-image: url(backPions2.jpg); -fx-background-size: cover;");
        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();

    }
}
