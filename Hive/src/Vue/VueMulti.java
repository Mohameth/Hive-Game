package Vue;

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

public class VueMulti extends Vue {
    private static int NB_COL = 7;

    VueMulti(Stage primaryStage){
        boolean fs = primaryStage.isFullScreen();
        Label t = new Label(getLangStr("deuxjoueur"));
        t.setFont(Font.font(60));

        GridPane root = new GridPane();

        for (int column = 0; column < NB_COL; column++) {
            root.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth()/NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 1; row++) {
            root.getRowConstraints().add(new RowConstraints(primaryStage.getHeight(), Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        Label tp = new Label(getLangStr("white"));
        tp.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        TextField ta = new TextField(getLangStr("joueur1"));
        ta.setMaxSize(200.0,5.0);
        HBox hb = new HBox(tp,ta);
        hb.setSpacing(5);

        HBox hb1 = new HBox();
        Label tp1 = new Label(getLangStr("black"));
        tp1.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");
        TextField ta1 = new TextField(getLangStr("joueur2"));
        ta1.setMaxSize(300.0,5.0);
        hb1.getChildren().addAll(tp1,ta1);
        hb1.setSpacing(12.0);

        Button bplay = new Button(getLangStr("jouer"));
        bplay.setMinSize(200.0,50.0);

        Label tc = new Label(getLangStr("pname"));
        tc.setStyle("-fx-font-weight: bold;-fx-font-size: 18px;");

        VBox vb2 = new VBox();
        vb2.getChildren().addAll(tc,hb,hb1);
        vb2.setSpacing(5);

        Button bBack = new Button(getLangStr("back"));
        bBack.setAlignment(Pos.CENTER_LEFT);
        bBack.setMinSize(200.0,50.0);
        bBack.setAlignment(Pos.CENTER);

        bBack.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        bplay.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneTerrain(primaryStage, 1);
        });

        GridPane g = new GridPane();
        g.add(bplay,0,2);
        g.add(bBack,1,2);
        g.add(vb2,0,0,2,2);
        g.setHgap(30.0);
        g.setVgap(25.0);

        VBox vb = new VBox();
        vb.getChildren().addAll(t,g);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(100.0);
        vb.setPrefHeight(primaryStage.getHeight());
        vb.setStyle("-fx-background-color : rgba(255, 255, 255, .7);-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        vb.setPadding(new Insets(0,20,0,20));

        root.add(vb,2,0,3,1);

        Scene s = new Scene(root,primaryStage.getScene().getWidth(),primaryStage.getScene().getHeight());
        s.getStylesheets().add("Vue/button.css");
        root.prefHeightProperty().bind(s.heightProperty());
        root.prefWidthProperty().bind(s.widthProperty());
        root.setStyle("-fx-background-image: url(background.jpg);");
        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();
    }


}
