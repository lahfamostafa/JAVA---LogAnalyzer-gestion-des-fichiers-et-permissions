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

    public String lireLigne() {
        if (utilisateurConnecte == null) {
            System.out.print("lineperm> ");

        } else {
            System.out.print(utilisateurConnecte + "@ligneperm> ");
        }
        return scanner.nextLine();
    }

    public void traiter(String ligne) {
        String netoyee = ligne.trim();
        if (netoyee.isEmpty()) {
            return;
        }
        String[] commandes = ligne.split("\\s+");

        switch (commandes[0].trim()) {
            case "signup":
                login();
                break;
            case "login":
                signup();
                break;
            case "help":
                System.out.println("help");
                break;
            case "logout":
                logout();
                break;
            case "exit":
                exit();
                break;
            case "stats":
                menuStats();
                break;
            case "ls":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                fs.lsCommande();
                break;
            case "chmod":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.chmodCommande(commandes[2].trim(), utilisateurConnecte, commandes[1].trim());
                break;
            case "cat":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.catCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "nano":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.nanoCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "rm":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.rmCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "touch":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                fs.touchCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            default:
                System.out.println("commande not found");
                break;
        }
    }

    public void exit() {
        System.out.println("Au revoir.");
        actif = false;
    }

    public void signup() {
        if (utilisateurConnecte != null) {
            System.out.println("Vous aves deja connecte !");
            return;
        }

        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if (us.login(user, password)) {
            utilisateurConnecte = user;
        }
        return;
    }

    public void login() {
        if (utilisateurConnecte != null) {
            System.out.println("Vous aves deja connecte !");
            return;
        }

        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if (us.signup(user, password)) {
            utilisateurConnecte = user;
        }
        return;
    }

    public void logout() {
        System.out.println("Vous ete deconnecte.");
        utilisateurConnecte = null;
    }

    private void menuStats(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n\nBienvenue dans LogAnalyzer. Choisissez une statistique par son numero.");
            System.out.println("=== LogAnalyzer ===");
            System.out.println("1) Nombre total d'actions");
            System.out.println("2) Nombre d'acces refuses");
            System.out.println("3) Utilisateurs distincts");
            System.out.println("4) Actions par utilisateur");
            System.out.println("5) Top 3 des fichiers consultes");
            System.out.println("6) Acces refusés d'un utilisateur");
            System.out.println("7) Utilisateur le plus actif");
            System.out.println("8) Repartition des actions par type");
            System.out.println("0) Quitter");
            System.out.print("Choix :");
            int choix = scanner.nextInt();
            LogAnalyzer log = new LogAnalyzer();
            switch (choix) {
                case 1:
                    log.totalActions();
                    break;
                case 2:
                    log.totalRefuse();
                    break;
                case 3:
                    log.usersDistinct();
                    break;
                case 4:
                    log.actionParUser();
                    break;
                case 5:
                    log.top3Files();
                    break;
                case 6:
                    log.userRefuse();
                    break;
                case 7:
                    log.userActif();
                    break;
                case 8:
                    log.actionsParType();
                    break;
                case 0 :return;
            
                default: System.out.println("Choix invalide");
            }
        }

    }

}