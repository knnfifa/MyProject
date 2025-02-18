package com.project;
 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
 
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
    
        TextField firstNumberField = new TextField("0");
        TextField secondNumberField = new TextField("0");

    
        firstNumberField.setPrefColumnCount(4);
        secondNumberField.setPrefColumnCount(4);

    
        Label plusLabel = new Label("+");
        Label equalsLabel = new Label("=");

    
        Button addButton = new Button("Add");

    
        Label resultLabel = new Label("0");

    
        addButton.setOnAction(e -> {
            try {
            
                int num1 = Integer.parseInt(firstNumberField.getText());
                int num2 = Integer.parseInt(secondNumberField.getText());
                int result = num1 + num2;
                resultLabel.setText(String.valueOf(result));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input");
            }
        });

    
        HBox topRow = new HBox(10, firstNumberField, plusLabel, secondNumberField, equalsLabel, resultLabel);
        topRow.setAlignment(Pos.CENTER);

    
        VBox content = new VBox(20, topRow, addButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

    
        Scene scene = new Scene(content, 500, 200);

    
        stage.setScene(scene);
        stage.setTitle("Adder");
        stage.setResizable(false);
        stage.show();
    }
}
