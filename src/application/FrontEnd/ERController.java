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

public class ERController {

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
        String Ratio = String.format("%.2f", encapsulation.GetTaux());
        String RationSvgPath="M 1.75 0 h 12.5 C 15.216 0 16 0.784 16 1.75 v 12.5 A 1.75 1.75 0 0 1 14.25 16 H 1.75 A 1.75 1.75 0 0 1 0 14.25 V 1.75 C 0 0.784 0.784 0 1.75 0 Z M 1.5 1.75 v 12.5 c 0 0.138 0.112 0.25 0.25 0.25 h 12.5 a 0.25 0.25 0 0 0 0.25 -0.25 V 1.75 a 0.25 0.25 0 0 0 -0.25 -0.25 H 1.75 a 0.25 0.25 0 0 0 -0.25 0.25 Z M 11.75 3 a 0.75 0.75 0 0 1 0.75 0.75 v 7.5 a 0.75 0.75 0 0 1 -1.5 0 v -7.5 a 0.75 0.75 0 0 1 0.75 -0.75 Z m -8.25 0.75 a 0.75 0.75 0 0 1 1.5 0 v 5.5 a 0.75 0.75 0 0 1 -1.5 0 Z M 8 3 a 0.75 0.75 0 0 1 0.75 0.75 v 3.5 a 0.75 0.75 0 0 1 -1.5 0 v -3.5 A 0.75 0.75 0 0 1 8 3 Z";
        
        TreeItemData rootItemData = new TreeItemData("Ratio Encapsulation : "+Ratio,RationSvgPath);
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



