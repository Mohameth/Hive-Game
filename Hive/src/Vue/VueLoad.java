package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;

import static com.sun.javafx.PlatformUtil.isWindows;

public class VueLoad extends Vue {
    private static int NB_COL = 7;

    VueLoad(Stage primaryStage) {

        boolean fs = primaryStage.isFullScreen();
        Label t = new Label(getLangStr("ldgame"));
        t.setFont(Font.font("Georgia", FontWeight.BOLD, 50));
        t.setStyle("-fx-border-width: 0 0 3 0; -fx-border-color: rgb(37, 19, 7);");
        t.setTextFill(Color.rgb(37, 19, 7));
        t.setPadding(new Insets(50,0,0,0));

        GridPane root = new GridPane();

        for (int column = 0; column < NB_COL; column++) {
            root.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth() / NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 1; row++) {
            root.getRowConstraints().add(new RowConstraints(primaryStage.getHeight(), Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        ListView<String> lv = getSaveFile();
        Button load = new Button(getLangStr("load"));
        load.setMinWidth(150);
        Button cancel = new Button(getLangStr("cancel"));
        cancel.setMinWidth(150);

        HBox hbutton = new HBox();
        hbutton.getStylesheets().add("Vue/button.css");
        hbutton.getChildren().addAll(load, cancel);
        hbutton.setSpacing(30);
        hbutton.setAlignment(Pos.CENTER);

        VBox vLoad = new VBox();
        vLoad.getChildren().addAll(lv, hbutton);
        vLoad.prefWidthProperty().bind(primaryStage.widthProperty());
        vLoad.prefHeightProperty().bind(primaryStage.heightProperty());
        vLoad.setAlignment(Pos.CENTER);
        vLoad.setSpacing(10);
        lv.setMaxWidth((primaryStage.getWidth() * 33) / 100);
        lv.getStylesheets().add("Vue/button.css");

        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
            SceneMain(primaryStage);
        });

        load.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e1) -> {
            //this.controleur.load(lv.getSelectionModel().getSelectedItem());
        });

        VBox vb = new VBox();
        vb.getChildren().addAll(t, vLoad);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10.0);
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
        primaryStage.setFullScreen(fs);
        primaryStage.show();

    }

    public ListView<String> getSaveFile() {
        String path;
        if (isWindows()) {
            path = System.getProperty("user.dir").concat("\\Hive\\rsc\\SAVE");
        } else {
            path = System.getProperty("user.dir").concat("/rsc/SAVE/");
        }
        System.out.println(path);
        File rep = new File(path);
        if (!rep.exists()) {
            rep.mkdir();
        }

        ListView<String> listSaveFile = new ListView<>();
        for (String s : rep.list()) {
            listSaveFile.getItems().add(s);
            System.out.println(s);
        }
        return listSaveFile;
    }
}
