package Shell;

import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import application.BackEnd.AnalyseAll;
import application.BackEnd.Package;
import application.FrontEnd.MetricController;

public class SimpleShell {
    public static void main(String[] args) throws Exception {
    	final String prompt = "Metric_Shell> ";
        Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).build();   
        
        DefaultParser parser = new DefaultParser();
        
        parser.escapeChars(new char[0]);
        
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)  
                .parser(parser)
                .variable(LineReader.HISTORY_FILE, Path.of(".my_shell_history"))
                .build();
        
      
        while (true) {
            String input = reader.readLine(prompt);
            if (input == null || input.equalsIgnoreCase("exit")) break;
            else {
            	if(input.equalsIgnoreCase("help")) {
            		executeCommand.executeHelp();
            	}
            	else {
            		ParsedLine pl = reader.getParser().parse(input, 0);
            		List<String> words = pl.words();
            		if(words.get(0).equalsIgnoreCase("show")) {
            			if(words.contains("-all")) {
            				executeCommand.executeNb();
            				executeCommand.executeUniq();
            				executeCommand.executeTab();
            			}
            			else { 
            				if(words.contains("-nb") && words.contains("-uniq")) {
                				executeCommand.executeUniq();
                			}
            				else if(words.contains("-nb")) {
            					executeCommand.executeNb();
            					executeCommand.executeUniq();
            				}
            				
            				if(words.contains("-tab")) {
            				executeCommand.executeTab();
            			}
            				
            			}
            		}
            		else if(words.get(0).equalsIgnoreCase("cls")) {
            			executeCommand.executeClear(terminal);
            		}
            		
            		else if (words.get(0).equalsIgnoreCase("analyse")) {
            			String projectPath = words.get(1);
            			String excelPath = words.get(3);
            	  
            			if(executeCommand.checkProject(projectPath)==0) {
            				if(executeCommand.checkExcel(excelPath) == 0) {
            					ArrayList<Package> listPackage = executeCommand.getPackages(projectPath);
            					int nbFiles = executeCommand.getNbFile(listPackage);
            					System.out.println("\n Nb File "+nbFiles);
            					 MetricController.PathProject = projectPath;
            					int status =AnalyseAll.AnalyseExcelShell(terminal, nbFiles, excelPath, listPackage);
            					if(status== 0) {
            						terminal.writer().println(); // New line when complete
            					    terminal.flush();
            						System.out.println("Finished Written On "+excelPath);
            						System.out.println("Successfull Exit Status 0");
            						persistanceFile.writeTab(projectPath, excelPath);
            						persistanceFile.writeNb(projectPath);
            					}
            					else {
            						System.out.println("Failed Exit Status -1");
            					}
            				}
            			}
            		
            		}
            		
            		else {
            			System.out.println("Command Undefined");
            		}
            	}
            }
        }
    }
}