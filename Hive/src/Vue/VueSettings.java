package Vue;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VueSettings extends Vue {
    private static int NB_LIGNE = 10;
    private static int NB_COL = 7;

    VueSettings(Stage primaryStage){
        Text t = new Text("Solo");
        t.setFont(Font.font(60));

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

        Text t1 = new Text("Multi");
        t1.setFont(Font.font(60));

        Text t2 = new Text("Players names");

        Text t3 = new Text("White");
        TextField tf = new TextField();
        Text t4 = new Text("Black");
        TextField tf1 = new TextField();

        HBox hb = new HBox();
        HBox hb1 = new HBox();
        hb.getChildren().addAll(t3,tf);
        hb.setSpacing(10);
        hb1.getChildren().addAll(t4,tf1);
        hb1.setSpacing(15);

        Text t5 = new Text("All");
        t5.setFont(Font.font(60));

        Text t6 = new Text("Window size");
        ComboBox<String> cb = new ComboBox();
        cb.getItems().addAll("1280*720","1920*1080");
        cb.getSelectionModel().select("1280*720");
        Text t7 = new Text("Language");
        ComboBox<String> cb1 = new ComboBox();
        cb1.getItems().addAll("French","English");
        cb1.getSelectionModel().select("French");

        HBox hb2 = new HBox();
        hb2.getChildren().addAll(t6,cb);
        hb2.setSpacing(10);

        CheckBox chb = new CheckBox("FullScreen");

        HBox hb4 = new HBox();
        hb4.getChildren().addAll(hb2,chb);
        hb4.setSpacing(100);

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(t7,cb1);
        hb3.setSpacing(30);

        Button bSaveDef = new Button("Save");
        Button bCancel = new Button("Cancel");
        Button bSave = new Button("Save for the game");

        bCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        HBox hb5 = new HBox();
        if(true)
            hb5.getChildren().add(bSave);
        hb5.getChildren().addAll(bCancel,bSaveDef);
        hb5.setSpacing(50);

        GridPane g = new GridPane();
        //ImageView fond = new ImageView(new Image("sample/hive.jpeg"));
        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        //g.setGridLinesVisible(true);
        g.add(t,1,0,3,1);
        g.add(vb1,2,1,3,1);
        g.add(t1,1,2,3,1);
        g.add(t2,1,3,3,1);
        g.add(hb,2,4,3,1);
        g.add(hb1,2,5,3,1);
        g.add(t5,1,6,3,1);
        g.add(hb4,2,7,3,1);
        g.add(hb3,2,8,3,1);
        g.add(hb5,2,9,3,1);

        Scene s = new Scene(g,primaryStage.getWidth(),primaryStage.getHeight());
        g.prefHeightProperty().bind(s.heightProperty());
        g.prefWidthProperty().bind(s.widthProperty());
        s.getStylesheets().add("Vue/button.css");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
