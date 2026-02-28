package application.FrontEnd;

import java.io.File;
import java.util.ArrayList;

import application.BackEnd.CleanData;
import application.BackEnd.Encapsulation;
import application.BackEnd.ImportStatus;
import application.BackEnd.SwingComponent;
import application.BackEnd.XMLResult;
import application.FrontEnd.ImportController.CustomTreeCell;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;

public class PublicElementController {

    @FXML
    private TreeView<TreeItemData> treeView;
    @FXML
    private Label EncapsulationLabel;
    @FXML
    private GridPane pane;
    static Encapsulation encapsulation;

    public void initialize(String FilePath) {
        File file = new File(FilePath);
        CleanData clean  = new CleanData(file,1);
        EncapsulationLabel.setText("Encapsulation of "+file.getName());
        encapsulation =   Encapsulation.EncapsulationFetchClean(clean);
        String PublicSvgPath="M 12 2 C 6.48 2 2 6.48 2 12 s 4.48 10 10 10 s 10 -4.48 10 -10 S 17.52 2 12 2 Z m -1 17.93 c -3.95 -0.49 -7 -3.85 -7 -7.93 c 0 -0.62 0.08 -1.21 0.21 -1.79 L 9 15 v 1 c 0 1.1 0.9 2 2 2 v 1.93 Z m 6.9 -2.54 c -0.26 -0.81 -1 -1.39 -1.9 -1.39 h -1 v -3 c 0 -0.55 -0.45 -1 -1 -1 H 8 v -2 h 2 c 0.55 0 1 -0.45 1 -1 V 7 h 2 c 1.1 0 2 -0.9 2 -2 v -0.41 c 2.93 1.19 5 4.06 5 7.41 c 0 2.08 -0.8 3.97 -2.1 5.39 Z"; 
        TreeItemData rootItemData = new TreeItemData("Public "+encapsulation.GetPublic(),PublicSvgPath);
        TreeItem<TreeItemData> rootItem = new TreeItem<>(rootItemData);
        treeView.setRoot(rootItem);
      
        setTreeViewStyle();
        
     
    }


    
    @FXML
    public void initialize() {
        // Add a listener to wait for the Scene to be available
        pane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
            	 KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            	    newScene.getAccelerators().put(ctrlS, () -> {
            	        System.out.println("Ctrl+S detected! Saving file...");
            	        ArrayList<Encapsulation> list  = new ArrayList<Encapsulation>();
            	        list.add(encapsulation);
            	        SaveFileController<Encapsulation> save = new SaveFileController<Encapsulation>("ER",list);
            	      
            	    });
            }
        });
    }
    
    
    
    private void setTreeViewStyle() {
    	treeView.setCellFactory(new Callback<TreeView<TreeItemData>, TreeCell<TreeItemData>>() {
    	    @Override
    	    public TreeCell<TreeItemData> call(TreeView<TreeItemData> param) {
    	        return new CustomTreeCell();
    	    }
    	});

    }
   
    
    public class CustomTreeCell extends TreeCell<TreeItemData> {
        private SVGPath svgPath;
        private Label label;
        private HBox hbox;

        public CustomTreeCell() {
            this.svgPath = new SVGPath();
            this.label = new Label();
            this.hbox = new HBox(svgPath, label);
            this.hbox.setAlignment(Pos.CENTER_LEFT);
            setGraphic(hbox);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setFocusTraversable(true); 
        }

        @Override
        protected void updateItem(TreeItemData item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                // Clear the content when the item is empty or null
                setText(null);
                setGraphic(null);
                // Reset style classes when the item is empty
                label.getStyleClass().clear();
                svgPath.getStyleClass().clear();
            } else {
                TreeItem<TreeItemData> treeItem = getTreeItem();     
             
                    if (treeItem != null) {
                        if (treeItem.getParent() == null) {
                    	label.getStyleClass().setAll("parent-node-label");
                            svgPath.getStyleClass().setAll("parent-node-default-svg");               
                        
                   
                        }
                } else {
                    // Clear style for empty cells
                    setStyle(null);
                }

                if (item.GetSVG() != null && item.GetSVG().getContent() != null && !item.GetSVG().getContent().isEmpty()) {
                    // Set the SVG content if available
                    svgPath.setContent(item.GetSVG().getContent());
                } else {
                    // Clear the SVG content if not available
                    svgPath.setContent(null);
                }

                if (item.GetLabel() != null) {
                    // Set the text label
                    label.setText(item.GetLabel());
                } else {
                    // Clear the text label if not available
                    label.setText(null);
                }
                label.setPadding(new Insets(0, 0, 0, 10)); // Example padding: 10px on the right
                // Set the HBox as the graphic content
                setGraphic(hbox);
            }
        }
    }
    
    
    



        
  
}



