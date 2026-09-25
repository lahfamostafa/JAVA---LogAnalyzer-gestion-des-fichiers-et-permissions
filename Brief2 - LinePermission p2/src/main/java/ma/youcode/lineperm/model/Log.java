package ma.youcode.lineperm.model;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private LocalDate date = LocalDate.now();
    private LocalTime heure = LocalTime.now();
    private String user;
    private String action;
    private String fichier;
    private Boolean resultat;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String heureFormat = heure.format(formatter);

    public Log(String user, String action, String fichier, boolean resultat) {
        this.user = user;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }
    public Log(LocalDate date, LocalTime heure, String user, String action, String fichier, boolean resultat) {
        this.date = date;
        this.heure = heure;
        this.user = user;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }
    public LocalDate getDate() {return date;}

    public LocalTime getHeure() {return heure;}

    public String getUser() {return user;}

    public String getAction() {return action;}

    public String getFichier() {return fichier;}

    public Boolean getResultat() {return resultat;}

    private String getStatus(){
        return (resultat ? "OK" : "REFUSEE");
    }

    public String logLogContent(){
        return date +";" + heureFormat+ ";" + user + ";" +action + ";" + fichier + ";" + getStatus();
    }
    
}