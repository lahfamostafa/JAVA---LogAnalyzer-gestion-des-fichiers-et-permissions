package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.LogAnalyzer;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp{
    Scanner scanner = new Scanner(System.in);

    public boolean actif = true;
    public String utilisateurConnecte = null;

    private UserService us = new UserService();
    private FileService fs = new FileService();

    public void demmarer(){
            System.out.println("========================================================");
            System.out.println("        LinePerm ? gestion de fichiers & droits ");
            System.out.println("========================================================");
            System.out.println("Non connecte . Commandes : signup  |  login  |  help  |  exit  |  logout\n");
        while(actif){
            String line = lireLigne();
            traiter(line);
        }
    }

    public void traiter(String ligne){
        String netoyee = ligne.trim();
        if(netoyee.isEmpty()){
            return ;
        }
        String[] commandes = ligne.split("\\s+");

        switch(commandes[0].trim()){
            case "signup": 
            signupLogin(1);
                break;
            case "login": 
            signupLogin(0);
                break;
            case "help": 
                System.out.println("help");break;
            case "logout": 
            logout();
                break;
            case "exit": 
            exit();
                break;
            case "ls":
                if(utilisateurConnecte == null){
                    System.out.println("Tu dois se connecter d'abord");break;
                }
                fs.lsCommande();
                break;
            case "chmod":
                fs.hasAcces(commandes[2].trim(),"c",utilisateurConnecte,commandes[1].trim());
                break;
            case "cat":
                fs.hasAcces(commandes[1].trim(),"r",utilisateurConnecte,"");
                break;
            case "nano":
                fs.hasAcces(commandes[1].trim(),"w",utilisateurConnecte,"");
                break;
            case "rm":
                fs.hasAcces(commandes[1].trim(),"d",utilisateurConnecte,"");
                break;
            case "stats":
                menuStats();
                break;
            case "touch":
                if(utilisateurConnecte == null){
                    System.out.println("Tu dois se connecter d'abord");break;
                }
                fs.touchCommande(commandes[1].trim() , utilisateurConnecte);
                break;
            default :
            System.out.println("commande not found");break;
        }
    }

    public String lireLigne(){
        if(utilisateurConnecte == null){
            System.out.print("lineperm> ");

        }else{
            System.out.print(utilisateurConnecte + "@ligneperm> ");
        }
        return scanner.nextLine();
    }

    private void menuStats(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue dans LogAnalyzer. Choisissez une statistique par son numéro.");
        System.out.println("=== LogAnalyzer ===");
        System.out.println("1) Nombre total d'actions");
        System.out.println("2) Nombre d'accès refusés");
        System.out.println("3) Utilisateurs distincts");
        System.out.println("4) Actions par utilisateur");
        System.out.println("5) Top 3 des fichiers consultés");
        System.out.println("6) Accès refusés d'un utilisateur");
        System.out.println("7) Utilisateur le plus actif");
        System.out.println("8) Répartition des actions par type");
        System.out.println("0) Quitter");
        System.out.println("Choix :");
        int choix = scanner.nextInt();

        switch (choix) {
            case 1:
                LogAnalyzer log = new LogAnalyzer();
                
                break;
        
            default:
                break;
        }
    }

    public void exit(){
        System.out.println("Au revoir.");
        actif = false ;
    }
    
    public void signupLogin(int i){
        if(utilisateurConnecte != null){
            System.out.println("Vous aves deja connecte !");
            return ;
        }
        
        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if(i == 0){
            if(us.login(user,password)){
                utilisateurConnecte = user;
            }
            return;
        }else{
            us.signup(user,password);
            utilisateurConnecte = user;
            return;
        }
    }

    public void logout(){
        System.out.println("Vous ete deconnecte.");
        utilisateurConnecte = null;
    }
    
}