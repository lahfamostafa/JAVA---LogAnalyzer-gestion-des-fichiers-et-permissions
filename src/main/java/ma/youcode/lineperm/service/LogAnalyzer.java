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
    // 3
    public void usersDistinct(){
        System.out.println("\nTotal des utilisateurs distincts : ");
        logsList.stream().map(Log::getUser).distinct().forEach(System.out::println);
    }
    // 4
    public void actionParUser(){
        Map<String,Long> u = logsList.stream().collect(Collectors.groupingBy(Log::getUser , Collectors.counting()));
        System.out.println("\nActions par utilisateur : \n"+u);
    }
    // 5
    public void top3Files(){
        Map<String,Long> fichiers = logsList.stream().collect(Collectors.groupingBy(Log::getFichier , Collectors.counting()));
        List<Map.Entry<String,Long>> top3Files = fichiers.entrySet().stream().sorted((e1 ,e2)->e2.getValue().compareTo(e1.getValue())).limit(3).toList();
        System.out.println("\nTop 3 des fichiers consultes : ");
        System.out.print(top3Files + "  ");
    }
    
}
