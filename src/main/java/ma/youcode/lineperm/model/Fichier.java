package ma.youcode.lineperm.model;

public class Fichier {
    private String proprietaire;
    private String name;

    private boolean ownerWrite = true;
    private boolean ownerRead = true;
    private boolean ownerDelete = true;

    private boolean autherWrite = false;
    private boolean autherRead = false;
    private boolean autherDelete = false;

    
    public Fichier(String proprietaire, String name, boolean ownerWrite, boolean ownerRead, boolean ownerDelete,
            boolean autherWrite, boolean autherRead, boolean autherDelete) {
        this.proprietaire = proprietaire;
        this.name = name;
        this.ownerWrite = ownerWrite;
        this.ownerRead = ownerRead;
        this.ownerDelete = ownerDelete;
        this.autherWrite = autherWrite;
        this.autherRead = autherRead;
        this.autherDelete = autherDelete;
    }


    public Fichier(String proprietaire, String name) {
        this.proprietaire = proprietaire;
        this.name = name;
    }
    

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwnerWrite() {
        return ownerWrite;
    }

    public void setOwnerWrite(boolean ownerWrite) {
        this.ownerWrite = ownerWrite;
    }

    public boolean isOwnerRead() {
        return ownerRead;
    }

    public void setOwnerRead(boolean ownerRead) {
        this.ownerRead = ownerRead;
    }

    public boolean isOwnerDelete() {
        return ownerDelete;
    }

    public void setOwnerDelete(boolean ownerDelete) {
        this.ownerDelete = ownerDelete;
    }

    public boolean isAutherWrite() {
        return autherWrite;
    }

    public void setAutherWrite(boolean autherWrite) {
        this.autherWrite = autherWrite;
    }

    public boolean isAutherRead() {
        return autherRead;
    }

    public void setAutherRead(boolean autherRead) {
        this.autherRead = autherRead;
    }

    public boolean isAutherDelete() {
        return autherDelete;
    }

    public void setAutherDelete(boolean autherDelete) {
        this.autherDelete = autherDelete;
    }

    public String getPersmission(){
        String ownerPermissions = 
            (ownerRead ? "r" : "-") +
            (ownerWrite ? "w" : "-") +
            (ownerDelete ? "d" : "-");
            
        String autherPermissions = 
            (autherRead ? "r" : "-") +
            (autherWrite ? "w" : "-") +
            (autherDelete ? "d" : "-");

        return ownerPermissions + "|" + autherPermissions;
    }
    
}