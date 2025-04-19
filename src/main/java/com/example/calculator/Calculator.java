package com.example.calculator;

import org.mindrot.jbcrypt.BCrypt;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class Calculator extends Application{
    private String firstNum = "";
    private String secondNum = "";
    private String operator = "";
    private int calculatedNum;
    private TextField calculation;

    private String hashedPassword = ""; //stores the hashed password in memory
    private static final String ACTUAL_PASSWORD = "finalProject123";
    @Override
    public void start(Stage stage) {
        //LOGIN & PASSWORD HASHING

        //generates a hashed password from ACTUAL_PASSWORD
        hashedPassword = HashGenerator.generateHash(ACTUAL_PASSWORD);

        //login screen setup
        GridPane loginGrid = new GridPane();
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);
        loginGrid.setPadding(new Insets(10));
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setStyle("-fx-background-color: #222222;"); //for dark background

        //label for message
        Label loginMessage = new Label();
        loginMessage.setId("loginMessage");
        loginMessage.setTextFill(Color.RED);

        //create password input field
        PasswordField passwordField = new PasswordField(); //this is different from text field; it masks the characters entered by user, displaying dots (.)
        passwordField.setPromptText("Enter password");

        //login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 18; -fx-background-color: #444444; -fx-text-fill: #ffffff; -fx-background-radius: 5;");
        //add "hover" effect to the button
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #555555; -fx-text-fill: #FFFFFF; -fx-font-size: 18;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #444444; -fx-text-fill: #FFFFFF; -fx-font-size: 18;"));

        //action event for login button
        loginButton.setOnAction(e -> {
            String userEnteredPassword = passwordField.getText();

            //check if entered password & hashed password are identical
            if (BCrypt.checkpw(userEnteredPassword, hashedPassword)) {
                loginMessage.setText("Access granted!");
                //switch to the calculator scene if password is correct
                Scene calcScene = calculatorScene(stage);
                stage.setScene(calcScene);
            } else {
                loginMessage.setText("Invalid password. Access denied. Try again.");
                passwordField.clear();
            }
        });

        //add components to login grid
        loginGrid.add(passwordField, 0, 0);
        loginGrid.add(loginButton, 0, 1);
        loginGrid.add(loginMessage, 0, 2);

        //create login scene
        Scene loginScene = new Scene(loginGrid, 400, 300);
        loginScene.setFill(Color.valueOf("#222222"));
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    //method to create the hashed password using BCrypt
    public static class HashGenerator {
        public static String generateHash(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt(12)); //generates hash with work factor of 12 as it is sufficiently slow to make brute force attacks impractical
        }
    }

    //CALCULATOR

    private Scene calculatorScene(Stage stage){
        //initializations
        calculation = new TextField();
        calculation.setEditable(false);
        calculation.setStyle("-fx-background-color: #333333; -fx-text-fill: #FFFFFF; -fx-font-size: 20;");

        // Number Buttons
        Button zero = new Button("0");
        Button one = new Button("1");
        Button two = new Button("2");
        Button three = new Button("3");
        Button four = new Button("4");
        Button five = new Button("5");
        Button six = new Button("6");
        Button seven = new Button("7");
        Button eight = new Button("8");
        Button nine = new Button("9");

        // Action events for number buttons
        zero.setOnAction(event -> addDigit('0'));
        one.setOnAction(event -> addDigit('1'));
        two.setOnAction(event -> addDigit('2'));
        three.setOnAction(event -> addDigit('3'));
        four.setOnAction(event -> addDigit('4'));
        five.setOnAction(event -> addDigit('5'));
        six.setOnAction(event -> addDigit('6'));
        seven.setOnAction(event -> addDigit('7'));
        eight.setOnAction(event -> addDigit('8'));
        nine.setOnAction(event -> addDigit('9'));

        // Operator Buttons
        Button add = new Button("+");
        Button sub = new Button("-");
        Button mult = new Button("*");
        Button div = new Button("/");
        Button equal = new Button("=");
        Button clr = new Button("Clear");

        // Action events for operation buttons
        add.setOnAction(event -> addOperator("+"));
        sub.setOnAction(event -> addOperator("-"));
        mult.setOnAction(event -> addOperator("*"));
        div.setOnAction(event -> addOperator("/"));
        equal.setOnAction(event -> calculate());
        clr.setOnAction(event -> clearAll());

        //make buttons dark-themed
        setButtonStyle(zero, one, two, three, four, five, six, seven, eight, nine, add, sub, mult, div, equal, clr);

        //create a layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #222222;"); //for dark background

        //add components to the grid in order of how they appear on the calculator
        //7,8,9,division
        grid.add(calculation, 0, 0, 4, 1);
        grid.add(seven, 0, 1);
        grid.add(eight, 1, 1);
        grid.add(nine, 2, 1);
        grid.add(div, 3, 1);
        //4,5,6,multiplication
        grid.add(four, 0, 2);
        grid.add(five, 1, 2);
        grid.add(six, 2, 2);
        grid.add(mult, 3, 2);
        //1,2,3,subtraction
        grid.add(one, 0, 3);
        grid.add(two, 1, 3);
        grid.add(three, 2, 3);
        grid.add(sub, 3, 3);
        //0,clear,equal,addition
        grid.add(zero, 0, 4);
        grid.add(clr, 1, 4);
        grid.add(equal, 2, 4);
        grid.add(add, 3, 4);

        //create the scene with dark theme
        Scene scene = new Scene(grid, 320, 400);
        scene.setFill(Color.valueOf("#222222"));
        stage.setScene(scene);
        stage.setTitle("Calculator");
        stage.show();
        return scene;
    }

    //method to calculate the result based on the operator
    public void calculate() {
        int firstNumInt = Integer.parseInt(firstNum);
        int secondNumInt = Integer.parseInt(secondNum);
        switch (operator) {
            case "+" -> calculatedNum = firstNumInt + secondNumInt;
            case "-" -> calculatedNum = firstNumInt - secondNumInt;
            case "*" -> calculatedNum = firstNumInt * secondNumInt;
            case "/" -> {
                if (secondNumInt != 0) {
                    double result = (double) firstNumInt / secondNumInt;
                    calculation.setText(String.valueOf(result));
                    return;
                } else {
                    calculation.setText("Error");
                    return;
                }
            }
        }
        calculation.setText(String.valueOf(calculatedNum));
        operator = "";
    }

    private String addOperator(String op) {
        operator = op;
        return operator;
    }

    private String addDigit(char digit) {
        if (operator.equals("")) {
            firstNum += digit;
            calculation.setText(firstNum);
            return firstNum;
        } else {
            secondNum += digit;
            calculation.setText(firstNum + operator + secondNum);
            return secondNum;
        }
    }

    public void clearAll() {
        firstNum = "";
        secondNum = "";
        operator = "";
        calculation.setText("");
    }

    private void setButtonStyle(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #444444; -fx-text-fill: #FFFFFF; -fx-font-size: 18;");
            button.setPrefWidth(60);
            button.setPrefHeight(40);
            //add "hover" effect to the buttons
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555555; -fx-text-fill: #FFFFFF; -fx-font-size: 18;"));
            button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #444444; -fx-text-fill: #FFFFFF; -fx-font-size: 18;"));
        }
    }

    public static void main(String[] args) {
        launch();
    }

}

