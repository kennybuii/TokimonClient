package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private final List<Tokimon> tokimons = new ArrayList<>();
    private final List<Rectangle> rectangles = new ArrayList<>();
    private final List<Label> labels = new ArrayList<>();

    private static String defaultName;
    private static double defaultWeight;
    private static double defaultHeight;
    private static String defaultAbility;
    private static int defaultStrength;
    private static String defaultColor;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Tokimon");

        //TODO: complete
        //ViewAll Button
        Button viewAllButton = new Button();
        viewAllButton.setText("View All Tokimon");
        viewAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tokimons.clear();
                rectangles.clear();
                labels.clear();
                try {
                    //opening up connection with url
                    URL url = new URL("http://localhost:8080/api/tokimon/all");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    //preparing to read
                    connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //reading
                    String output;
                    output = bufferedReader.readLine();
                    //done
                    connection.disconnect();
                    //turning string into JSONArray and adding each tokimon from the JSONArray to tokimons
                    JSONArray jsonArr = (JSONArray) new JSONParser().parse( output );
                    jsonArr.forEach( toki -> parseTokimonObject( (JSONObject) toki ) );
                    System.out.println(output);
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
               //creating rectangles and labels
                for (int i = 0; i< tokimons.size(); i++) {
                    Rectangle rectangle = new Rectangle();
                    Label label = new Label();

                    int width = (int) tokimons.get(i).getWeight();
                    int height = (int) tokimons.get(i).getHeight();
                    String name = tokimons.get(i).getName();
                    double tokiWeight = tokimons.get(i).getWeight();
                    double tokiHeight = tokimons.get(i).getHeight();
                    String ability = tokimons.get(i).getAbility();
                    int strength = tokimons.get(i).getStrength();
                    String tokiColor = tokimons.get(i).getColour();
                    int id = tokimons.get(i).getPrivateId();
                    Color color = Color.web(tokimons.get(i).getColour());

                    rectangle.setWidth(width);
                    rectangle.setHeight(height);
                    rectangle.setFill(color);
                    label.setText(
                            "ID: " + id + "\n" +
                            "Name: " + name + "\n" +
                            "weight: " + tokiWeight + "\n" +
                            "height: " + tokiHeight + "\n" +
                            "Ability: " + ability + "\n" +
                            "Strength: " + strength + "\n" +
                            "Color: " + tokiColor);
                    labels.add(label);
                    rectangles.add(rectangle);
                }

                Stage stage = new Stage();
                stage.setTitle("View All Tokimon");

                int counter = 0;
                GridPane gridPane = new GridPane();

                //adding rectangles and labels to gridpane
                for (int i = 0; i < rectangles.size(); i++) {
                    Rectangle rectangle = rectangles.get(i);
                    Label label = labels.get(i);

                    gridPane.add(rectangle,i,counter);
                    gridPane.add(label,i, counter+1);
                    gridPane.setAlignment(Pos.CENTER);
                }

                gridPane.setHgap(20);
                VBox vBox = new VBox(gridPane);
                stage.setScene(new Scene(vBox, 800, 800));
                stage.show();}
        });

        //TODO: complete
        //ViewSpecific Button
        Button viewOneButton = new Button();
        viewOneButton.setText("View Specific Tokimon by Id");
        viewOneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                stage.setTitle("View Specific Tokimon");

                Label label = new Label("Tokimon ID: ");
                Label tokiInfoLabel = new Label();
                Label errorLabel = new Label();
                TextField textField = new TextField();
                Button button = new Button("Search");

                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            int input = Integer.parseInt(textField.getText());
                            //opening up connection with url
                            URL url = new URL("http://localhost:8080/api/tokimon/" + input);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            //preparing to read
                            connection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            //reading
                            String output;
                            output = bufferedReader.readLine();
                            //parsing
                            JSONObject tokiObject = (JSONObject) new JSONParser().parse( output );
                            String name = (String) tokiObject.get("name");
                            double weight = (double) tokiObject.get("weight");
                            double height = (double) tokiObject.get("height");
                            String ability = (String) tokiObject.get("ability");
                            long str = (long) tokiObject.get("strength");
                            int strength = (int) str;
                            String color = (String) tokiObject.get("colour");
                            tokiInfoLabel.setText("Name: " + name + "\n" +
                                    "weight: " + weight + "\n" +
                                    "height: " + height + "\n" +
                                    "Ability: " + ability + "\n" +
                                    "Strength: " + strength + "\n" +
                                    "Color: " + color + "\n" +
                                    "Link: http://localhost:8080/api/tokimon/" + input);
                            //done
                            connection.disconnect();
                            errorLabel.setText("");
                        }
                        catch (IOException | ParseException e) {
                            e.printStackTrace();
                            tokiInfoLabel.setText("");
                            errorLabel.setText("ERROR! No Tokimon with that ID exists.");
                        }
                        catch (NumberFormatException e) {
                            e.printStackTrace();
                            tokiInfoLabel.setText("");
                            errorLabel.setText("ERROR! Invalid or no input entered.");
                        }
                    }
                });

                GridPane gridPane = new GridPane();
                gridPane.add(label,0,0);
                gridPane.add(textField,1,0);
                VBox vBox = new VBox(gridPane,button,tokiInfoLabel,errorLabel);

                stage.setScene(new Scene(vBox, 300, 275));
                stage.show();
            }
        });

        //TODO:Complete
        //Add Button
        Button addButton = new Button();
        addButton.setText("Add New Tokimon");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //creating save button, labels, and text fields
                //and adding them to gridpane
                Button button = new Button("Save");
                Label nameLabel = new Label("Name: ");
                Label weightLabel = new Label("Weight: ");
                Label heightLabel = new Label("Height: ");
                Label abilityLabel = new Label("Ability: ");
                Label strengthLabel = new Label("Strength: ");
                Label colorLabel = new Label("Color: ");
                Label successLabel = new Label();
                Label errorLabel = new Label();
                TextField nameField = new TextField();
                TextField weightField = new TextField();
                TextField heightField = new TextField();
                TextField abilityField = new TextField();
                TextField strengthField = new TextField();
                TextField colorField = new TextField();
                GridPane gridPane = new GridPane();
                gridPane.setHgap(20);
                gridPane.setVgap(20);
                gridPane.add(nameLabel,0,0);
                gridPane.add(nameField,1,0);
                gridPane.add(weightLabel,0,1);
                gridPane.add(weightField,1,1);
                gridPane.add(heightLabel,0,2);
                gridPane.add(heightField,1,2);
                gridPane.add(abilityLabel,0,3);
                gridPane.add(abilityField,1,3);
                gridPane.add(strengthLabel,0,4);
                gridPane.add(strengthField,1,4);
                gridPane.add(colorLabel,0,5);
                gridPane.add(colorField,1,5);

                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            String name1 = nameField.getText();
                            double weight1 = Double.parseDouble(weightField.getText());
                            double height1 = Double.parseDouble(heightField.getText());
                            String ability1 = abilityField.getText();
                            int strength1 = Integer.parseInt(strengthField.getText());
                            String color1 = colorField.getText();

                            Tokimon tokimon = new Tokimon(name1, weight1, height1, ability1, strength1, color1);
                            tokimons.add(tokimon);


                            URL url = new URL("http://localhost:8080/api/tokimon/add");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");

                            //preparing to write
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                            outputStreamWriter.write("{\"name\":\"" + name1 + "\",\"weight\":" + weight1 +
                                    ",\"height\":" + height1 + ",\"ability\":\"" + ability1 +
                                    "\",\"strength\":" + strength1 + ",\"colour\":\"" + color1 + "\"}");
                            outputStreamWriter.flush();
                            outputStreamWriter.close();

                            //done
                            connection.connect();
                            System.out.println(connection.getResponseCode());
                            connection.disconnect();
                            errorLabel.setText("");
                            successLabel.setText("Success! Tokimon with the following attributes was created:\n" +
                                    "Name: " + name1 + "\n" +
                                    "weight: " + weight1 + "\n" +
                                    "height: " + height1 + "\n" +
                                    "Ability: " + ability1 + "\n" +
                                    "Strength: " + strength1 + "\n" +
                                    "Color: " + color1);
                        }
                        catch (IOException | NumberFormatException e) {
                            errorLabel.setText("ERROR! Invalid/missing data fields. Please fill them in and try again.");
                            successLabel.setText("");
                            e.printStackTrace();
                        }

                    }
                });

                Stage stage = new Stage();
                stage.setTitle("Add New Tokimon");

                VBox vBox = new VBox(gridPane, button,successLabel,errorLabel);
                vBox.getChildren().get(1).setTranslateY(190);
                vBox.getChildren().get(1).setTranslateX(220);
                stage.setScene(new Scene(vBox, 500, 500));
                stage.show();
            }
        });

        //TODO: complete
        //Alter Button
        Button alterButton = new Button();
        alterButton.setText("Alter Tokimon");
        alterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                stage.setTitle("Alter Tokimon");
                //Creating labels, text fields, and button
                Label idLabel = new Label("ID of Tokimon to change: ");
                Label nameLabel = new Label("Change Name: ");
                Label weightLabel = new Label("Change Weight: ");
                Label heightLabel = new Label("Change Height: ");
                Label abilityLabel = new Label("Change Ability: ");
                Label strengthLabel = new Label("Change Strength: ");
                Label colorLabel = new Label("Change Color: ");
                Label successLabel = new Label();
                TextField idField = new TextField();
                TextField nameField = new TextField();
                TextField weightField = new TextField();
                TextField heightField = new TextField();
                TextField abilityField = new TextField();
                TextField strengthField = new TextField();
                TextField colorField = new TextField();
                Button button = new Button("Save");
                //Creating and setting up gridpane
                GridPane gridPane = new GridPane();
                gridPane.setHgap(20);
                gridPane.setVgap(20);
                gridPane.add(idLabel,0,0);
                gridPane.add(idField,1,0);
                gridPane.add(nameLabel,0,1);
                gridPane.add(nameField,1,1);
                gridPane.add(weightLabel,0,2);
                gridPane.add(weightField,1,2);
                gridPane.add(heightLabel,0,3);
                gridPane.add(heightField,1,3);
                gridPane.add(abilityLabel,0,4);
                gridPane.add(abilityField,1,4);
                gridPane.add(strengthLabel,0,5);
                gridPane.add(strengthField,1,5);
                gridPane.add(colorLabel,0,6);
                gridPane.add(colorField,1,6);

                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        //Getting Tokimon attributes as indicated by ID
                        int input = Integer.parseInt(idField.getText());
                        for (int i = 0; i < tokimons.size(); i++) {
                            if (tokimons.get(i).getPrivateId() == input) {
                                defaultName = tokimons.get(i).getName();
                                defaultWeight = tokimons.get(i).getWeight();
                                defaultHeight = tokimons.get(i).getHeight();
                                defaultAbility = tokimons.get(i).getAbility();
                                defaultStrength = tokimons.get(i).getStrength();
                                defaultColor = tokimons.get(i).getColour();
                            }
                        }
                        //If user left fields blank because they did not want
                        //to change certain attributes, grab Tokimon's original values
                        if (nameField.getText().equals("null")) {
                            nameField.setText(defaultName);
                        }
                        if (weightField.getText().equals("")) {
                            weightField.setText("" + defaultWeight);
                        }
                        if (heightField.getText().equals("")) {
                            heightField.setText("" + defaultHeight);
                        }
                        if (abilityField.getText().equals("null")) {
                            abilityField.setText(defaultAbility);
                        }
                        if (strengthField.getText().equals("")) {
                            strengthField.setText("" + defaultStrength);
                        }
                        if (colorField.getText().equals("null")) {
                            colorField.setText(defaultColor);
                        }
                        //Parse text fields
                        String name1 = nameField.getText();
                        double weight1 = Double.parseDouble(weightField.getText());
                        double height1 = Double.parseDouble(heightField.getText());
                        String ability1 = abilityField.getText();
                        int strength1 = Integer.parseInt(strengthField.getText());
                        String color1 = colorField.getText();
                        //Clear text fields for neatness
                        nameField.clear();
                        weightField.clear();
                        heightField.clear();
                        abilityField.clear();
                        strengthField.clear();
                        colorField.clear();

                        //Send request
                        try {
                            String tokiUrl ="http://localhost:8080/api/tokimon/change/"
                                    + input
                                    + "?name="+ name1
                                    + "&" + "weight="
                                    + weight1 + "&"
                                    + "height="
                                    + height1 + "&"
                                    + "ability="
                                    + ability1 + "" +
                                    "&" + "strength="
                                    + "&" + strength1
                                    + "&" + "color=" + color1;
                            System.out.println(tokiUrl);
                            //Establishing connection
                            URL url = new URL(tokiUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type","application/json");
                            successLabel.setText("Success! Tokimon now has changed the following attributes:\n" +
                                    "Name: " + name1 + "\n" +
                                    "weight: " + weight1 + "\n" +
                                    "height: " + height1 + "\n" +
                                    "Ability: " + ability1 + "\n" +
                                    "Strength: " + strength1 + "\n" +
                                    "Color: " + color1);
                            //Finished
                            connection.connect();
                            System.out.println(connection.getResponseCode());
                            connection.disconnect();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                VBox vBox = new VBox(gridPane,button,successLabel);
                vBox.getChildren().get(1).setTranslateY(150);
                vBox.getChildren().get(1).setTranslateX(220);
                stage.setScene(new Scene(vBox, 500, 500));
                stage.show();
            }
        });

        //TODO: complete
        //Delete Button
        Button deleteButton = new Button();
        deleteButton.setText("Delete Tokimon");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                stage.setTitle("Delete Tokimon");
                Label label = new Label("Tokimon to delete: ");
                Label label1 = new Label();
                Label errorLabel = new Label();
                TextField textField = new TextField();
                Button button = new Button();
                GridPane gridPane = new GridPane();
                gridPane.add(label,0,0);
                gridPane.add(textField,1,0);

                VBox vBox = new VBox(gridPane,button,label1,errorLabel);
                vBox.getChildren().get(1).setTranslateY(200);
                vBox.getChildren().get(1).setTranslateX(95);

                button.setText("Delete Tokimon");
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        //opening up connection with url
                        try {
                            int input = Integer.parseInt(textField.getText());
                            String urlLink = "http://localhost:8080/api/tokimon/" + input;
                            System.out.println(urlLink);
                            URL url = new URL(urlLink);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("DELETE");

                            //preparing to read
                            connection.getInputStream();
                            System.out.println("Response code: " + connection.getResponseCode());

                            if (connection.getResponseCode() == 204) {
                                errorLabel.setText("");
                                label1.setText("Successfully deleted Tokimon " + input);
                            }
                        }
                        catch (IOException | NumberFormatException e) {
                            label1.setText("");
                            errorLabel.setText("ERROR! Invalid/missing data fields");
                            e.printStackTrace();
                        }
                    }
                });
                stage.setScene(new Scene(vBox, 300, 275));
                stage.show();
            }
        });

        //Setting up menu screen
        Label menuLabel = new Label("Main Menu");
        VBox vBox =new VBox(menuLabel, viewAllButton,viewOneButton,addButton, alterButton, deleteButton);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(vBox, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void parseTokimonObject(JSONObject tokimon)
    {
        //Get tokimon object within list
        JSONObject tokimonObject = (JSONObject) tokimon.get("tokimon");

        //Parse tokimon's attributes from JSONObject
        String name = (String) tokimonObject.get("name");
        double weight = (double) tokimonObject.get("weight");
        double height = (double) tokimonObject.get("height");
        String ability = (String) tokimonObject.get("ability");
        long str = (long) tokimonObject.get("strength");
        int strength = (int) str;
        String color = (String) tokimonObject.get("color");
        long pid = (long) tokimonObject.get("privateId");
        int privateId = (int) pid;

        //Creating tokimon and adding it to list
        Tokimon newTokimon = new Tokimon(name,weight,height,ability,strength,color);
        newTokimon.setPrivateId(privateId);
        tokimons.add(newTokimon);
    }
}
