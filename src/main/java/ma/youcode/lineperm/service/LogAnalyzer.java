package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import ma.youcode.lineperm.model.Log;

public class LogAnalyzer {
    private final Path pathFile = Path.of("src/main/java/ma/youcode/lineperm/Logs.log");
    
    private List<Log> logsList = new ArrayList<>();
    public LogAnalyzer() {
        loadLogs();
    }

    public void saveLog(String user,String action,String file, boolean resultat){
        Log log = new Log(user, action, file, resultat);
        try {
            Files.writeString(pathFile, log.logLogContent()+System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    
    private void loadLogs(){
        try {
            List<String> logsLis = Files.readAllLines(pathFile);
            for (String string : logsLis) {
                String[] line = string.split(";");
                LocalDate date = LocalDate.parse(line[0]);
                LocalTime heure = LocalTime.parse(line[1]);
                String user = line[2];
                String action = line[3];
                String fichier = line[4];
                String resultat = line[5];
                boolean status = Boolean.parseBoolean(resultat.equals("OK")? "true" : "false");

                Log log = new Log(date, heure, user, action, fichier, status);
                logsList.add(log);
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void afficherLogs(){
        for (Log log : logsList) {
            System.out.println(log.getAction()+"  "+log.getResultat());
        }
    }
    // 1
    public void totalActions(){
        System.out.println("\nTotal des actions : "+ logsList.stream().count());
    }
    // 2
    public void totalRefuse(){
        System.out.println("\nAcces refuses : "+logsList.stream().filter(log -> log.getResultat().equals(false)).count());
    }
    
}
