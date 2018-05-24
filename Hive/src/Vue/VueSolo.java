package Vue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class VueSolo extends Vue {

    private static int NB_COL = 7;

    VueSolo(Stage primaryStage) {
        Properties prop = new Properties();
        String propFileName = System.getProperty("user.dir").concat("/rsc/config.properties");
        InputStream input = null;
        try {
            input = new FileInputStream(propFileName);
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }
        
        boolean fs = primaryStage.isFullScreen();
        Label t = new Label(getLangStr("solo"));
        t.setFont(Font.font(60));

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
        TextField ta = new TextField(prop.getProperty("joueurBlanc"));
        
        ta.setMaxSize(300.0, 5.0);
        hb.getChildren().addAll(tp, ta);
        hb.setAlignment(Pos.CENTER);

        Button bplay = new Button(getLangStr("jouer"));
        bplay.setMinSize(200.0, 50.0);

        VBox vb1 = new VBox();
        Label td = new Label(getLangStr("difficulte"));
        td.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        final ToggleGroup group = new ToggleGroup();
        RadioButton rEasy = new RadioButton(getLangStr("easy"));
        rEasy.setUserData("easy");
        RadioButton rMedium = new RadioButton(getLangStr("medi"));
        rMedium.setUserData("medium");
        RadioButton rHard = new RadioButton(getLangStr("hard"));
        rHard.setUserData("hard");
        rEasy.setToggleGroup(group);
        rMedium.setToggleGroup(group);
        rHard.setToggleGroup(group);
        group.selectToggle(rMedium);
        vb1.getChildren().addAll(td, rEasy, rMedium, rHard);
        for(Toggle to : group.getToggles()) {
            if (to.getUserData().equals(prop.getProperty("difficulteIA")))
                group.selectToggle(to);
        }

        VBox vb2 = new VBox();
        Label tc = new Label(getLangStr("chColor"));
        tc.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        final ToggleGroup group2 = new ToggleGroup();
        RadioButton rWhite= new RadioButton(getLangStr("white"));
        rWhite.setUserData("white");
        RadioButton rBlack = new RadioButton(getLangStr("black"));
        rBlack.setUserData("black");
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
                    switch(group.getSelectedToggle().getUserData().toString()) {
                        case "black":
                            difficulte = difficulte+3;
                        break;
                    }
                }
                SceneTerrain(primaryStage,difficulte,true);
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
        vb.setStyle("-fx-background-color : rgba(255, 255, 255, .7);-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        vb.setPadding(new Insets(0, 20, 0, 20));

        root.add(vb, 2, 0, 3, 1);

        Scene s = new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        s.getStylesheets().add("Vue/button.css");
        root.prefHeightProperty().bind(s.heightProperty());
        root.prefWidthProperty().bind(s.widthProperty());
        root.setStyle("-fx-background-image: url(background.jpg);");
        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();
        
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
