package application.FrontEnd;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

import application.BackEnd.Java;

public class DirectoryController {

	public static String path;
	
    @FXML
    private TextField PathField;

    @FXML
    private Label ErrorLabelPath;
    
    public static String binPath;
    
    @FXML
    private TextField binField;

    @FXML
    private Label binErrorLabelPath;
    

    @FXML
    void browseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Java Project Path");
        Window window = PathField.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(window);
        if (selectedDirectory != null) {
        	path = selectedDirectory.getAbsolutePath();
        PathField.setText(path);
           
            //System.out.println(path);
            //System.out.println(Java.IsJavaProject(path));
            switch(Java.IsJavaProject(path)) {
            case -1:
                setErrorLabel(ErrorLabelPath,"Error Path Doesn't Exist", "red");
                break;
            case 0:
                setErrorLabel(ErrorLabelPath,"Src Folder Is Empty", "red");
                break;
            case 1:
            	 setErrorLabel(ErrorLabelPath,"Java Project", "green");
            	//path= Java.ConcatSrc(path);
            	 
            	 	if(binErrorLabelPath.getText().equals("Bin Folder")) {
                openMetricScene(path);
            	 	}
                break;
            case 2:
                setErrorLabel(ErrorLabelPath,"Not A Java Project", "red");
                break;
        }

        
        } else {
        		PathField.setText("");
            setErrorLabel(ErrorLabelPath,"No directory selected", "red");
        }
    }
    
    
    @FXML
    void isBinFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Bin Path");
        Window window = PathField.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(window);
        if (selectedDirectory != null) {
        	binPath = selectedDirectory.getAbsolutePath();
        binField.setText(binPath);
           
            //System.out.println(path);
            //System.out.println(Java.IsJavaProject(path));
            switch(Java.IsBinFolder(binPath)) {
            case -1:
                setErrorLabel(binErrorLabelPath,"Error Path Doesn't Exist", "red");
                break;
            case 0:
                setErrorLabel(binErrorLabelPath,"Bin Folder Is Empty", "red");
                break;
            case 1:
            	 setErrorLabel(binErrorLabelPath,"Bin Folder", "green");
            	 	if(ErrorLabelPath.getText().equals("Java Project")) {
                openMetricScene(path);
            	 }
                break;
            case 2:
                setErrorLabel(binErrorLabelPath,"Not A Bin Folder", "red");
                break;
        }

        
        } else {
        		binField.setText("");
            setErrorLabel(binErrorLabelPath,"No directory selected", "red");
        }
    }

    private void setErrorLabel(Label errorLabel,String text, String color) {
        errorLabel.setStyle("-fx-text-fill: " + color + ";");
        errorLabel.setText(text);
    }
    
    private void openMetricScene(String pathProject) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ressource/Fxml Folder/MetricJava.fxml"));
            Parent root = fxmlLoader.load();
            MetricController metricController = fxmlLoader.getController();
            metricController.initialize(pathProject);
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("/ressource/Css Folder/application.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
