package ma.youcode.lineperm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt; 
import ma.youcode.lineperm.model.User;

public class UserService {

    Map <String,User> userMap = new HashMap<>();

    Scanner scanner = new Scanner(System.in);

    public UserService(){
        loadFile();
    }

    public boolean login(String user, String password){
        if(userMap.containsKey(user)){
            User userP = userMap.get(user);
            if(BCrypt.checkpw(password,userP.getPassword())){
                System.out.println("Vous ete conncte ");
                return true;
            }
        }
        System.out.println("Mot de pass ou user incorect !");
        return false;
    }
    
    
    public boolean signup(String user , String password){
        if(userMap.containsKey(user)){
            System.out.println("Ce nom d'utilisateur est deja pris !");
            return false;
        }

        if(ajouterText(user,password)){
            System.out.println("inscription avec succes");
            loadFile();
            return true;
        }
        return false;
    }

    public void loadFile(){
        userMap.clear();
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/java/ma/youcode/lineperm/Users.txt"))){
            String line;
            while((line = br.readLine()) != null){
                String[] identifiants = line.split(":");
                if(identifiants.length == 2){
                    String loginFichier = identifiants[0].trim();
                    String mdpFichier = identifiants[1].trim();
                    User user = new User(loginFichier, mdpFichier);
                    userMap.put(loginFichier, user);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public boolean ajouterText(String loginSaisi, String mdpSaisi){
        String passwordhash = BCrypt.hashpw(mdpSaisi, BCrypt.gensalt());
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/ma/youcode/lineperm/Users.txt" , true))){
            bw.write(loginSaisi + " : " + passwordhash);
            bw.newLine();
            User newUser =new User(loginSaisi,passwordhash);
            userMap.put(loginSaisi, newUser);
            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}