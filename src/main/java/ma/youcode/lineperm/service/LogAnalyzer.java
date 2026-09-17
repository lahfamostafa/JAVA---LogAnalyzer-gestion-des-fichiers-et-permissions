package ma.youcode.lineperm.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import ma.youcode.lineperm.model.Log;

public class LogAnalyzer {
    private final Path pathFile = Path.of("src/main/java/ma/youcode/lineperm/Logs.log");
    
    public LogAnalyzer() {
    }

    public void saveLog(String user,String action,String file, boolean resultat){
        Log log = new Log(user, action, file, resultat);
        try {
            Files.writeString(pathFile, log.logLogContent()+System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    
    public void loadLogs(){

    }
}
