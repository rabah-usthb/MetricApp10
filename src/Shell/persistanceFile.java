package Shell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class persistanceFile {

	static String tabPath = ".tab_map";
	static String nbPath = ".nb_project";
	
	public static boolean fileExist(String path) {
		return Files.exists(Path.of(path));
	}
	
	static HashMap<String,String> readTab() {
		 String path = Path.of(tabPath).toString();
		 
		 try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
	           
	            return (HashMap<String, String>) in.readObject();
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        
	            return new HashMap<>();
	        }
	}
	
	    static void writeNb(String projectPath) {
	    	 String path = Path.of(nbPath).toString();
	    	 int [] nb = readNB();
	    	 int all = nb[0]+1;
	    	 int uniq = nb[1];
	    	 
	    	 if( !readTab().isEmpty() ||!readTab().containsKey(projectPath)) {
	    		 ++uniq; 
	    	 }
	    	 
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path))) {
            out.writeInt(all);
            out.writeInt(uniq);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	static void writeTab(String projectPath,String excelPath) {
		 HashMap<String,String> map = getTab();
		 String path = Path.of(tabPath).toString();
	        map.put(projectPath, excelPath);
	        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
	            out.writeObject(map);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	
	static int[] readNB() {
		    String path = Path.of(nbPath).toString();
		    if(!fileExist(nbPath)) {
			  return new int[]{0, 0};
			}
		    
		    try (DataInputStream in = new DataInputStream(new FileInputStream(path))) {
	            int all  = in.readInt();
	            int uniq = in.readInt();
	            return new int[]{all, uniq};
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new int[]{0, 0};
	        }
	}
	
	
	public static String getnbUniq() {
		if(!fileExist(nbPath)) {
			return "0";
		}
		else {
			return  Integer.toString(readNB()[1]);
		}
	}
	
	public static String getnb() {
		if(!fileExist(nbPath)) {
			return "0";
		}
		else {
			return  Integer.toString(readNB()[0]);
		}
	}
	
	public static HashMap<String,String> getTab(){
		if(!fileExist(tabPath)) {
			return new HashMap<String,String>();
		}
		else {
			return readTab();
		}
	}
	
}
