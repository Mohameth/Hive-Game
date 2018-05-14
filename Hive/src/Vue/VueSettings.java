package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VueSettings extends Vue {

    private static int NB_LIGNE = 3;
    private static int NB_COL = 7;

    VueSettings(Stage primaryStage) {
        GridPane g = new GridPane();
        Scene s = new Scene(g, primaryStage.getWidth(), primaryStage.getHeight());
        s.getStylesheets().add("Vue/button.css");

        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth() / NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(primaryStage.getHeight() / NB_LIGNE, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }
        Rectangle r = new Rectangle((g.getWidth() / NB_COL) * 3, g.getHeight(), Color.WHITE);
        r.setOpacity(0.7);
        r.heightProperty().bind(g.prefHeightProperty());
        VBox solo = getSolo();
        VBox multi = getMulti();
        g.add(r, 2, 0, 3, NB_LIGNE);
        g.add(solo, 2, 0, 3, 1);
        g.add(multi, 2, 1, 3, 1);
        g.add(getAll(primaryStage), 2, 2, 3, 1);
        r.widthProperty().bind(solo.widthProperty());

        g.prefHeightProperty().bind(s.heightProperty());
        g.prefWidthProperty().bind(s.widthProperty());
        g.setStyle("-fx-background-image: url(background.jpg);");

        primaryStage.setScene(s);
        primaryStage.show();
    }

    private GridPane getAll(Stage primaryStage) {
        GridPane gAll = new GridPane();
        for (int column = 0; column < 2; column++) {
            gAll.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 3; row++) {
            gAll.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        Label t5 = new Label("All");
        t5.setFont(Font.font(36));
        t5.setAlignment(Pos.TOP_LEFT);

        Text t6 = new Text("Window size");
        ComboBox<String> cb = new ComboBox();
        cb.getItems().addAll("1280*720", "1920*1080");
        cb.getSelectionModel().select("1280*720");

        Text t7 = new Text("Language");
        ComboBox<String> cb1 = new ComboBox();
        cb1.getItems().addAll("French", "English");
        cb1.getSelectionModel().select("French");

        HBox hb2 = new HBox();
        hb2.getChildren().addAll(t6, cb);
        hb2.setSpacing(10);

        CheckBox chb = new CheckBox("FullScreen");
        chb.setAlignment(Pos.TOP_CENTER);

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(t7, cb1);
        hb3.setSpacing(30);

        Button bSaveDef = new Button("Save");
        Button bCancel = new Button("Cancel");
        Button bSave = new Button("Save for\nthe game");

        bCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        bSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (chb.isSelected()) {

            } else {
                System.out.println(cb.getValue());
                if (cb.getValue().equals("1920*1080")) {
                    primaryStage.setHeight(1080);
                    primaryStage.setWidth(1920);
                } else if (cb.getValue().equals("1280*720")) {
                    primaryStage.setHeight(720);
                    primaryStage.setWidth(1280);
                }
                //primaryStage.setHeight(0);
            }
        });

        HBox hb5 = new HBox();
        if (true) {
            hb5.getChildren().add(bSave);
        }
        hb5.getChildren().addAll(bCancel, bSaveDef);
        hb5.setSpacing(50);

        gAll.add(t5, 0, 0, 1, 1);
        gAll.add(hb2, 0, 1, 1, 1);
        gAll.add(hb3, 0, 2, 1, 1);
        gAll.add(chb, 1, 1, 1, 1);
        gAll.add(hb5, 1, 2, 1, 1);

        gAll.setPadding(new Insets(0, 20.0, 0, 20.0));
        gAll.setStyle("-fx-border-color: black; -fx-border-width: 0 3 0 3;");

        return gAll;
    }

    private VBox getMulti() {
        Label t1 = new Label("Multi");
        t1.setFont(Font.font(36));

        Label t2 = new Label("Players names");

        Text t3 = new Text("White");
        TextField tf = new TextField();
        Text t4 = new Text("Black");
        TextField tf1 = new TextField();

        HBox hb = new HBox();
        hb.getChildren().addAll(t3, tf);
        hb.setSpacing(10);

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(t4, tf1);
        hb1.setSpacing(15);
        hb1.setPadding(new Insets(0, 0, 30, 0));
        hb1.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0;");

        VBox v = new VBox();
        v.getChildren().addAll(t1, t2, hb, hb1);
        v.setPadding(new Insets(0, 20.0, 0, 20.0));
        v.setStyle("-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        v.setSpacing(20.0);

        return v;
    }

    private VBox getSolo() {
        Label t = new Label("Solo");
        t.setFont(Font.font(36));

        Text td = new Text("Difficulty :");
        final ToggleGroup group = new ToggleGroup();
        RadioButton rEasy = new RadioButton("Easy");
        RadioButton rMedium = new RadioButton("Medium");
        RadioButton rHard = new RadioButton("Hard");
        rEasy.setToggleGroup(group);
        rMedium.setToggleGroup(group);
        rHard.setToggleGroup(group);
        rMedium.setSelected(true);

        VBox vb = new VBox();
        vb.getChildren().addAll(td, rEasy, rMedium, rHard);
        vb.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0;");
        vb.setPadding(new Insets(0, 0, 30.0, 0));
        VBox.setMargin(vb, new Insets(0, 20, 0, 0));
        vb.setSpacing(5);

        VBox v = new VBox();
        v.getChildren().addAll(t, vb);
        v.setAlignment(Pos.CENTER_LEFT);
        v.setStyle("-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        v.setSpacing(20);
        v.setPadding(new Insets(0, 0, 10, 20));

        return v;
    }
}
