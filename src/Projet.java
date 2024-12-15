import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Projet {
    private int id;
    private String titre;
    private String description;
    private String dateDebut;
    private String dateFin;

    // Constructeur
    public Projet(int id, String titre, String description, String dateDebut, String dateFin) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // create
    public static void create(String titre, String description, String dateDebut, String dateFin) {
        String query = "INSERT INTO Projets (titre, description, date_debut, date_fin) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.executeUpdate();
            System.out.println("Projet ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du projet : " + e.getMessage());
        }
    }

    // le read
    public static ResultSet read() {
        String query = "SELECT * FROM Projets";
        try {
            Connection conn = Database.getConnection();
            return conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des projets : " + e.getMessage());
            return null;
        }
    }

    // update
    public static void update(int id, String titre, String description, String dateDebut, String dateFin) {
        String query = "UPDATE Projets SET titre = ?, description = ?, date_debut = ?, date_fin = ? WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            System.out.println("Projet mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du projet : " + e.getMessage());
        }
    }


    // le delete
    public static void delete(int id) {
        String deleteRelationsQuery = "DELETE FROM Employes_Projets WHERE projet_id = ?";
        String deleteProjectQuery = "DELETE FROM Projets WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            // Supprimer les relations dans la table intermédiaire
            try (PreparedStatement stmtRelations = conn.prepareStatement(deleteRelationsQuery)) {
                stmtRelations.setInt(1, id);
                stmtRelations.executeUpdate();
                System.out.println("Relations du projet supprimées avec succès.");
            }

            // Supprimer le projet lui-même
            try (PreparedStatement stmtProject = conn.prepareStatement(deleteProjectQuery)) {
                stmtProject.setInt(1, id);
                stmtProject.executeUpdate();
                System.out.println("Projet supprimé avec succès !");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du projet : " + e.getMessage());
        }
    }

    public static void assignEmployeToProjet(int projetId, int employeId) {
        String query = "INSERT INTO Employes_Projets (employe_id, projet_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeId);
            stmt.setInt(2, projetId);
            stmt.executeUpdate();
            System.out.println("Employé ID " + employeId + " assigné au projet ID " + projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation de l'employé au projet : " + e.getMessage());
        }
    }




}
