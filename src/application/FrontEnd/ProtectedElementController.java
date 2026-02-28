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

public class ProtectedElementController {

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
        String ProtectedSvgPath="M 12 1 L 3 5 v 6 c 0 5.55 3.84 10.74 9 12 c 5.16 -1.26 9 -6.45 9 -12 V 5 l -9 -4 Z m 0 10.99 h 7 c -0.53 4.12 -3.28 7.79 -7 8.94 V 12 H 5 V 6.3 l 7 -3.11 v 8.8 Z";  
        TreeItemData rootItemData =new TreeItemData("Protected "+encapsulation.GetProtected(),ProtectedSvgPath);
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
}



