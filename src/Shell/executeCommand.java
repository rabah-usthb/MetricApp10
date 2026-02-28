package Shell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import application.BackEnd.AnalyseAll;
import application.BackEnd.Java;
import application.BackEnd.Package;

public class executeCommand {

	 static char bullet = '\u2022';
	 static char checked ='\u2713';
	 static char cross = '\u2717';
	
	 static void executeHelp() {
		    System.out.println(bullet + " analyse \"project_path\" -all \"excel_path\" : analyze the entire project and store output in an Excel file at the specified path");
		    System.out.println(bullet + " show -nb                : show number of projects analyzed (not unique)");
		    System.out.println(bullet + " show -nb -uniq          : show number of projects analyzed (unique)");
		    System.out.println(bullet + " show -tab               : show paths of analyzed projects and their associated Excel file paths");
		    System.out.println(bullet + " show -all               : show all information");
		    System.out.println(bullet + " cls                     : clear terminal");
		    System.out.println(bullet + " exit                    : quit shell");
		}
	 
	 static void executeClear(Terminal terminal) {
		   terminal.puts(Capability.clear_screen);
           terminal.flush();
	 }
	 
	 static int checkProject(String path) {
		 
		  System.out.println("\n"+bullet + "Checking Project "+path);
		  
		 
		 int status = Java.IsJavaProject(path);
		 
		 switch(status) {
		 case -1:
			 System.out.println("Error Path Doesn't Exist "+cross);
			 System.out.println("Exit Status -1");
			 return -1;
         case 0:
        	 System.out.println("Src Folder Is Empty "+cross);
        	 System.out.println("Exit Status -1");
        	 return -1;
         case 1:
        	 System.out.println("Java Project "+checked);
        	 return 0;
         case 2:
        	 System.out.println("Not A Java Project "+cross);
        	 System.out.println("Exit Status -1");
        	 return -1;
		 }
		 return 0;
		 
	 }
	 
	 
	 static int isExcel(String path) {
		 File file = new File(path);
	
		 if (file.isDirectory()) {
			 return 0;
		 }
		 else if(!file.getName().endsWith(".xlsx")) {
			 return -2;
		 }
		 else {
			 return 1;
		 }
	 }
	 
	 static int checkExcel(String path) {
		 
		  System.out.println("\n"+bullet + "Checking Excel "+path);
		  
		 
		 int status = isExcel(path);
		 
		 switch(status) {
		 case -2:
			 System.out.println("Not An Excel File "+cross);
			 System.out.println("Exit Status -1");
			 return -1;
        case 0:
       	 System.out.println("Folder Not File "+cross);
       	 System.out.println("Exit Status -1");
       	return -1;
        case 1:
       	 System.out.println("Excel File "+checked);
       	 return 0;
		 }
		
		 return 0;
		 
	 }
	
	 /*
	 static void printProgress(Terminal terminal,) throws InterruptedException {
	        final int total = 100;
	        final int width = 50;

	        for (int done = 0; done <= total; done++) {
	            printProgressBar(terminal, done, total, width);
	            Thread.sleep(50);
	        }
	        terminal.writer().println(); // New line when complete
	        terminal.flush();
	    }
*/

	    public static void printProgressBar(Terminal terminal, int done, int total, int width) {
	        int filled = (int) ((done / (double) total) * width);

	        StringBuilder bar = new StringBuilder("\r["); // \r returns to start of line
	        for (int i = 0; i < filled; i++) bar.append('=');
	        for (int i = filled; i < width; i++) bar.append(' ');
	        bar.append("] ")
	           .append(String.format("%d", done))
	           .append('/')
	           .append(String.format("%d", total));

	        terminal.writer().print(bar);
	        terminal.flush();
	    }

	 static int getNbFile (ArrayList<Package> Project) {
		 int nb = 0;
		 for (Package pkg : Project) {
			 nb += pkg.FileNameList.size();
			 if(!pkg.SubPackges.isEmpty()) {
				 nb += getNbFile(pkg.SubPackges);
			 }
		 }
		 return nb;
	 }
	 
	 static ArrayList<Package> getPackages(String path) {
		  ArrayList<Package> listPackage = new ArrayList<>();
		  File projectFile = new File(path);
	      File[] srcFile = projectFile.listFiles();
	      Java.FetchSrcJavaFile(srcFile, listPackage);
	      return listPackage;
	 }
	 
	 static void analyzeAll (String path, String excel,ArrayList<Package> listPackage) {
	      try {
			AnalyseAll.AnalyseExcel(excel,listPackage);
		} catch (IOException e) {
			System.out.println("Unexpected Error IOExpection Exit Status -1");
			e.printStackTrace();
		}
	      
	  	System.out.println("Analysis Sucessfull Exit Status 0");
	 }
	 
	 static void executeUniq() {
		 System.out.println(bullet+" Number Of Uniq Projects Analyzed "+persistanceFile.getnbUniq());
	 }
	 
	 static void executeNb() {
		 System.out.println(bullet+" Number Of Projects Analyzed "+persistanceFile.getnb());
	 }
	 
	 static void writeRow(String project , String excel) {
		 System.out.println("|"+" ".repeat(40)+"|"+" ".repeat(40)+"|");
		 List<String> listPr = new ArrayList<>();
		 List<String> listEx = new ArrayList<>();
		 
		 fetchText(project, listPr);
		 fetchText(excel, listEx);
		 
		 int size = Math.max(listEx.size(), listPr.size());
		 
		
		 
		 for(int i = 0; i<size;i++) {
			  String projectText = (i < listPr.size()) ? listPr.get(i) : " ";
		      String excelText = (i < listEx.size()) ? listEx.get(i) : " ";
		      
			System.out.println("| "+projectText+ " ".repeat(38-projectText.length()) +" | "+excelText+ " ".repeat(38-excelText.length()) +" |");
				
		 }
		 System.out.println("+"+"-".repeat(40)+"+"+"-".repeat(40)+"+");
		 
		 
	 }
	 
	 static void fetchText(String text,List<String> list) {
		 if(text.length()<=38) {
			 list.add(text);
			 return;
		 }
		 else {
			 int startIndex = 0;
			 int EndIndex = 37;
			 while(text.length() > 38) {
				 list.add(text.substring(startIndex,EndIndex+1));
				 text = text.substring(EndIndex+1);
                 
				 if(text.length()>38) {
				 startIndex = EndIndex + 1;
                 EndIndex = EndIndex + 37;
                 }
			 }
			 
			 if(text.length()%38!=0) {
				 list.add(text.substring(startIndex));
			 }
		 }
	 }
	 
	 
	 static void executeTab() {
		 HashMap<String,String> map = persistanceFile.getTab();
		 
		 System.out.println("\n");
		 System.out.println("+"+"-".repeat(40)+"+"+"-".repeat(40)+"+");
		 System.out.println("|"+" ".repeat(12)+"Project Path"+" ".repeat(16)+"|"+" ".repeat(13)+"Excel Path"+ " ".repeat(17)+"|");
		 System.out.println("+"+"-".repeat(40)+"+"+"-".repeat(40)+"+");
		 
		 
		 if(map.isEmpty()) {
			 System.out.println("|"+" ".repeat(40)+"|"+" ".repeat(40)+"|");
			 System.out.println("+"+"-".repeat(40)+"+"+"-".repeat(40)+"+");
		 }
		 else {
			 for (Map.Entry<String, String> entry : map.entrySet()) {
				    String project = entry.getKey();
				    String excel = entry.getValue();
				    writeRow(project,excel);
				}
		 }
		 System.out.println("\n");
	 }

}
