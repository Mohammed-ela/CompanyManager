import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private String poste;
    private String email;
    private String telephone;

    // Constructeur
    public Employe(int id, String nom, String prenom, String poste, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.email = email;
        this.telephone = telephone;
    }

    // Le create
    public static void create(String nom, String prenom, String poste, String email, String telephone) {
        String query = "INSERT INTO Employes (nom, prenom, poste, email, telephone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, poste);
            stmt.setString(4, email);
            stmt.setString(5, telephone);
            stmt.executeUpdate();
            System.out.println("Employé ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'employé : " + e.getMessage());
        }
    }
    
    // Le read
    public static ResultSet read() {
        String query = "SELECT * FROM Employes";
        try {
            Connection conn = Database.getConnection();
            return conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des employés : " + e.getMessage());
            return null;
        }
    }

    // le update
    public static void update(int id, String nom, String prenom, String poste, String email, String telephone) {
        String query = "UPDATE Employes SET nom = ?, prenom = ?, poste = ?, email = ?, telephone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, poste);
            stmt.setString(4, email);
            stmt.setString(5, telephone);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            System.out.println("Employé mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'employé : " + e.getMessage());
        }
    }
    // le delete
    public static void delete(int id) {
        String query = "DELETE FROM Employes WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Employé supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'employé : " + e.getMessage());
        }
    }
}

