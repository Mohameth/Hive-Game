package Vue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VueSolo extends Vue {

    VueSolo(Stage primaryStage){
        Text t = new Text("Singleplayer");
        t.setFont(Font.font(60));

        HBox hb = new HBox();
        Text tp = new Text("Enter your name : ");
        TextField ta = new TextField("Player 1");
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
}
