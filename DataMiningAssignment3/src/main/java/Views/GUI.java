package Views;

import Controllers.BaysianAlgorithm;
import Controllers.KnearestAlgorithm;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

    private TextArea textAreaFirstAlgorithm = new TextArea("");
    private TextArea textAreaSecondAlgorithm = new TextArea("");


    public void appendToFirstTextArea(String text) {
        textAreaFirstAlgorithm.appendText(text + "\n");
    }

    public void appendToSecondTextArea(String text) {
        textAreaSecondAlgorithm.appendText(text + "\n");
    }

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Hello To Mining Window");
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);

        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinWidth(600);

        final Label label = new Label("for k-nearest Algorithm, Enter K: ");
        label.setMinWidth(150);
        label.setStyle("-fx-text-fill: #faafbe; -fx-font-size: 18");
        pane.add(label, 0, 0, 1, 1);

        final TextField field = new TextField();
        field.setStyle("-fx-background-color: #faafbe");
        field.setMinWidth(200);
        pane.add(field, 1, 0, 1, 1);

        final Button doMining = new Button("Do Mining");
        doMining.setStyle("-fx-background-color: #faafbe; -fx-font-size: 18");
        doMining.setMinWidth(200);
        pane.add(doMining,0,1,1,1);

        setOnAction(doMining, field);

        Button exit = new Button("exit");
        exit.setStyle("-fx-background-color: #faafbe; -fx-font-size: 18;");
        exit.setMinWidth(200);

        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        pane.add(exit,1,1,1,1);
        pane.setStyle("-fx-background-color: #013");
        Scene scene = new Scene(pane, 1400, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setOnAction(final Button button, final TextField field) {

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                final Stage stage = new Stage();
                final GridPane pane = new GridPane();
                pane.setAlignment(Pos.CENTER);

                pane.setHgap(10);
                pane.setVgap(10);
                pane.setPadding(new Insets(10, 10, 10, 10));
                pane.setMinWidth(600);

                if (field.getText().equals("")) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("K not determined!!");
                    alert.setContentText("You should enter k to do the algorithm.");
                    alert.show();
                } else {

                    textAreaFirstAlgorithm.clear();
                    textAreaSecondAlgorithm.clear();

                    textAreaFirstAlgorithm.setEditable(false);
                    textAreaSecondAlgorithm.setEditable(false);

                    pane.add(textAreaFirstAlgorithm, 0, 2,1,4);
                    pane.add(textAreaSecondAlgorithm, 1, 2,1,4);

                    textAreaFirstAlgorithm.setMinSize(620, 555);
                    textAreaSecondAlgorithm.setMinSize(620, 555);

                    final Label firstAlgorithm = new Label("Baysian Algorithm");
                    firstAlgorithm.setMinWidth(150);
                    firstAlgorithm.setStyle("-fx-text-fill: #faafbe; -fx-font-size: 18");
                    pane.add(firstAlgorithm, 0, 1, 1, 1);

                    final Label secondAlgorithm = new Label("K-nearest Algorithm");
                    secondAlgorithm.setMinWidth(150);
                    secondAlgorithm.setStyle("-fx-text-fill: #faafbe; -fx-font-size: 18");
                    pane.add(secondAlgorithm, 1, 1, 1, 1);

                    Button run = new Button("RUN");
                    run.setStyle("-fx-background-color: #faafbe; -fx-font-size: 18");
                    pane.add(run,0,0,1,1);

                    run.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            BaysianAlgorithm baysianAlgorithm = new BaysianAlgorithm(Views.GUI.this);
                            float firstAlgorithmAccuracy = baysianAlgorithm.run();

                            if (Integer.parseInt(field.getText()) > baysianAlgorithm.getData().getTrainingData().size()) {

                                textAreaSecondAlgorithm.appendText("ERROR!! \nThe number of tuples = " +
                                        baysianAlgorithm.getData().getTrainingData().size()
                                        + ", so K is greater than size of data, you can back and try again.");

                            } else {
                                KnearestAlgorithm knearestAlgorithm =
                                        new KnearestAlgorithm(baysianAlgorithm.getClasses(), Views.GUI.this);
                                float secondAlgorithmAccuracy = knearestAlgorithm.run(Integer.parseInt(field.getText()));

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Accuracy");
                                if (firstAlgorithmAccuracy > secondAlgorithmAccuracy) {

                                    alert.setContentText("Baysian Algorithm = " + firstAlgorithmAccuracy +
                                            " ,K-nearest Algorithm = " + secondAlgorithmAccuracy + "\n\n" +
                                            "So the baysian algorithm more accurate than K-nearest Algorithm");
                                } else if (firstAlgorithmAccuracy < secondAlgorithmAccuracy) {

                                    alert.setContentText("Baysian Algorithm = " + firstAlgorithmAccuracy +
                                            " ,K-nearest Algorithm = " + secondAlgorithmAccuracy + "\n\n" +
                                            "so the K-nearest algorithm more accurate than baysian Algorithm");
                                } else {

                                    alert.setContentText("Baysian Algorithm = " + firstAlgorithmAccuracy +
                                            " ,K-nearest Algorithm = " + secondAlgorithmAccuracy + "\n\n" +
                                            "so the two algorithms are equally likely");
                                }

                                alert.show();
                            }
                        }
                    });

                    Button back = new Button("back");
                    back.setStyle("-fx-background-color: #faafbe; -fx-font-size: 18");

                    back.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            stage.hide();
                        }
                    });
                    pane.add(back, 0, 7,1,1);
                    pane.setStyle("-fx-background-color: #013");

                    Scene scene = new Scene(pane, 1400, 700);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });
    }
}
