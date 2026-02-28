package application.FrontEnd;

	import javafx.fxml.FXML;



	import javafx.fxml.FXMLLoader;
	import javafx.geometry.Insets;
	import javafx.geometry.Pos;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.ButtonType;
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
	import javafx.stage.Stage;

	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;

import application.BackEnd.CleanData;
import application.BackEnd.ExceptionStatus;
import application.BackEnd.ImportStatus;
import application.BackEnd.Package;
import application.BackEnd.XMLResult;
import javafx.util.Callback;
public class CompileTimeExceptionController {
        static ArrayList<ExceptionStatus>ListException = new ArrayList<>();
	    @FXML
	    private TreeView<TreeItemData> treeView;
	    @FXML
	    private Label ExceptionLabel;

	    @FXML
	    GridPane pane;
	    
	    public void initialize(String FilePath) {
	        File file = new File(FilePath);
	        CleanData clean = new CleanData(file, 0);
	        ExceptionLabel.setText("Exceptions Of "+file.getName());
	        
	       try {
			//ListException = ExceptionStatus.FetchThrowable(file);
	    	ListException = ExceptionStatus.FetchFromCleanCode(clean);
	//	System.out.println(ListException);
	       } catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	        TreeItemData rootItemData = new TreeItemData("Exceptions","M 12 2 C 6.48 2 2 6.48 2 12 s 4.48 10 10 10 s 10 -4.48 10 -10 S 17.52 2 12 2 Z m 1 15 h -2 v -2 h 2 v 2 Z m 0 -4 h -2 V 7 h 2 v 6 Z");
	        TreeItem<TreeItemData> rootItem = new TreeItem<>(rootItemData);
	        treeView.setRoot(rootItem);
	        String CheckedSvgPath="M 9 16.17 L 4.83 12 l -1.42 1.41 L 9 19 L 21 7 l -1.41 -1.41 Z";
	        for (ExceptionStatus exception : ListException) {
	        	//System.out.println("cddc");
	        	//TreeItem<TreeItemData> ImportItem = createTreeItem(Import);
	            //System.out.println(exception.ExceptionName);
	        	if(exception.DefaultStatus == 0) {
	            	if(exception.CheckedStatus == 0) {
	            	//	System.out.println("cddc");
	            		//System.out.println(exception.ExceptionName);
	            		rootItem.getChildren().add(new TreeItem<>(new TreeItemData(exception.ExceptionName,CheckedSvgPath)));
	            	}
	            	
	            	}
	            else {
	            	if(exception.CheckedStatus == 0) {
	            		//System.out.println(exception.ExceptionName);
		            	rootItem.getChildren().add(new TreeItem<>(new TreeItemData(exception.ExceptionName,CheckedSvgPath)));
		            	}
		            	
	            }
	        }
	        if(rootItem.getChildren().isEmpty()) {
	            rootItem.getChildren().add(new TreeItem<>(new TreeItemData("None","M 12 2 C 6.5 2 2 6.5 2 12 s 4.5 10 10 10 s 10 -4.5 10 -10 S 17.5 2 12 2 Z M 4 12 c 0 -4.4 3.6 -8 8 -8 c 1.8 0 3.5 0.6 4.9 1.7 L 5.7 16.9 C 4.6 15.5 4 13.8 4 12 Z m 8 8 c -1.8 0 -3.5 -0.6 -4.9 -1.7 L 18.3 7.1 C 19.4 8.5 20 10.2 20 12 c 0 4.4 -3.6 8 -8 8 Z")));
	        }
	        
	        setTreeViewStyle();
	        
	     
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
	                        label.getStyleClass().setAll("root-node-label");
	                        svgPath.getStyleClass().setAll("root-node-svg");
	                    } else if (!treeItem.isLeaf()) {
	                        label.getStyleClass().setAll("parent-node-label");
	                        if(treeItem.getValue().label.equals("Default Exceptions")) {
	                        svgPath.getStyleClass().setAll("parent-node-default-svg");
	                        }
	                        else {
	                        	svgPath.getStyleClass().setAll("parent-node-notdefault-svg");
	                        }
	                    } else {
	                        label.getStyleClass().setAll("leaf-node-label");
	                        svgPath.getStyleClass().setAll("leaf-node-svg");
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


