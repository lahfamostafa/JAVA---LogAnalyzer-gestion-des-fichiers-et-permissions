package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.youcode.lineperm.model.Fichier;

public class FileService {

    private final Path saveFoder = Path.of("src/main/java/ma/youcode/lineperm/files");
    Map <String,Fichier> fileMap = new HashMap<>();
    private final Path dataFile = Path.of("src/main/java/ma/youcode/lineperm/Files.txt");
    private LogAnalyzer logA = new LogAnalyzer();

    public FileService() {
        loadFile();
    }

    public void lsCommande(){
        for(Fichier f:fileMap.values()){
            System.out.printf("%-12s %-18s %s%n",f.getPersmission(),f.getName(),f.getProprietaire());
        }
    }
    
    public void touchCommande(String FileName , String propr){
        try {
            Path filePath = saveFoder.resolve(FileName);
            if (Files.exists(filePath)) {
                System.out.println("Le fichier " + FileName + " existe deja !");
                return;
            }

            Fichier fichier = new Fichier(propr, FileName);
            String csvLine = String.join(",",
                fichier.getName(), fichier.getProprietaire(),
                String.valueOf(fichier.isOwnerWrite()), String.valueOf(fichier.isOwnerRead()), String.valueOf(fichier.isOwnerDelete()),
                String.valueOf(fichier.isAutherRead()), String.valueOf(fichier.isAutherWrite()), String.valueOf(fichier.isAutherDelete())
            ) + System.lineSeparator();

            Files.writeString(dataFile, csvLine, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            fileMap.put(FileName, fichier);
            Files.createFile(filePath);
            System.out.println("Fichier " + FileName + " cree avec succes.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadFile(){
        fileMap.clear();
        if (!Files.exists(dataFile)) return;
        try {
            List<String> lignes = Files.readAllLines(dataFile);
            for(String ligne :lignes){
                if (ligne.isBlank()) continue;
                String[] line = ligne.split(",");
                String FileName = line[0];
                String Owner = line[1];
                boolean OW = Boolean.parseBoolean(line[2]);
                boolean OR = Boolean.parseBoolean(line[3]);
                boolean OD = Boolean.parseBoolean(line[4]);
                boolean AW = Boolean.parseBoolean(line[5]);
                boolean AR = Boolean.parseBoolean(line[6]);
                boolean AD = Boolean.parseBoolean(line[7]);

                Fichier fichier =new Fichier(Owner, FileName, OW, OR, OD, AW, AR, AD);
                fileMap.put(FileName, fichier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void catCommande(String file ,String user){
        if(user == null){
            System.out.println("Tu dois se connecter d'abord");
            return ;
        }
        Fichier f = fileMap.get(file);
        if (f == null) {
            System.out.println("Le fichier est introuvable");
            return ;
        }
        if (f.isAutherRead() || (user.equals(f.getProprietaire()))) {
            Path foldesFiles = saveFoder.resolve(file);
            try {
                if (!Files.exists(foldesFiles)) {
                    System.out.println("Fichier inexistant.");
                    return ;
                }
                List<String> lines = Files.readAllLines(foldesFiles);
                if(lines.isEmpty()){
                    System.out.println("(fichier vide)");
                    return ;
                }
                for(String ligne:lines){
                    System.out.println(ligne);
                }
                logA.saveLog(user,"LECTURE",file,true);
                return ;
            } catch (Exception e) {
                logA.saveLog(user,"LECTURE",file,false);
                System.out.println(e.getStackTrace());
                return ;
            }
        }
        logA.saveLog(user,"LECTURE",file,false);
        System.out.println("vous n'avez pas la permission d'ecrire dans : "+file);
    }

    public void nanoCommande(String file ,String user){
        if(user == null){
            System.out.println("Tu dois se connecter d'abord");
            return ;
        }
        Fichier f = fileMap.get(file);
        if (f == null) {
            System.out.println("Le fichier est introuvable");
            return ;
        }
        if (f.isAutherWrite() || (user.equals(f.getProprietaire()))) {
            Path foldesFiles = saveFoder.resolve(file);
            System.out.println("=== Contenu actuel de " + file + " ===");
            System.out.println("--- Entrez votre texte (tapez 'EOF' sur une nouvelle ligne pour enregistrer) ---");
            catCommande(file,user);
    
            Scanner scanner = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
    
            while (true) {
                String ligne = scanner.nextLine();
                if(ligne.contains("EOF"))break ;
                sb.append(ligne);
                sb.append(System.lineSeparator());
            }
            try {
                Files.writeString(foldesFiles, sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Modifications enregistrees dans " + file);
                logA.saveLog(user,"ECRITURE",file,true);
                return ;
            } catch (IOException e) {
                System.err.println("Erreur lors de l'enregistrement dans le fichier : " + e.getMessage());
                logA.saveLog(user,"ECRITURE",file,false);
                return ;
            }
            }
            logA.saveLog(user,"ECRITURE",file,false);
            System.out.println("vous n'avez pas la permission de lire : "+file);
    }

    public void chmodCommande(String file ,String user, String newPerm){
        if(user == null){
            System.out.println("Tu dois se connecter d'abord");
            return ;
        }
        Fichier f = fileMap.get(file);
        if (f == null) {
            System.out.println("Le fichier est introuvable");
            return ;
        }
        if (user.equals(f.getProprietaire())) {
            Fichier fichier = fileMap.get(file);
    
            if (fichier == null) {
                System.out.println("Fichier introuvable : " + file);
                return;
            }
    
            String ancientPerm = fichier.getPersmission();
    
            if (newPerm.startsWith("-")) {
                if(newPerm.contains("w")) fichier.setAutherWrite(false);
                if(newPerm.contains("r")) fichier.setAutherRead(false);
                if(newPerm.contains("d")) fichier.setAutherDelete(false);
            }else{
                if(newPerm.contains("w")) fichier.setAutherWrite(true);
                if(newPerm.contains("r")) fichier.setAutherRead(true);
                if(newPerm.contains("d")) fichier.setAutherDelete(true);
            }
            
            System.out.println(file + " : " + ancientPerm + "  ->  " + fichier.getPersmission());
            try {
                List<String> lignes = new ArrayList<>();
                for(Fichier li:fileMap.values()){
                    String ligne = li.getName() + "," +
                                li.getProprietaire() + "," +
                                li.isOwnerRead() + "," +
                                li.isOwnerWrite() + "," +
                                li.isOwnerDelete() + "," +
                                li.isAutherRead() + "," +
                                li.isAutherWrite() + "," +
                                li.isAutherDelete();
                    lignes.add(ligne);
                }
                Files.write(dataFile, lignes);
            } catch (Exception e) {
                System.out.println("Erreur lors de la mise a jour de Files.txt : " +e.getStackTrace());
            }
                return ;
            }
            System.out.println("vous n'avez pas la permission de modifier les permssions : "+file);
    }

    public boolean rmCommande(String file ,String user){
        logA.saveLog(user,"SUPPRIMER",file,false);
        return true;
    }
}