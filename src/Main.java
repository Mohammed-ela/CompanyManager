import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:8889/CompanyManager";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion réussie !\n");

            afficherTableEmployes(conn);
            afficherTableProjets(conn);
            afficherRelationsEmployesProjets(conn);

        } catch (Exception e) {
            System.err.println("Erreur de connexion ou d'exécution de requête : " + e.getMessage());
        }
    }

    private static void afficherTableEmployes(Connection conn) {
        System.out.println("=== Liste des Employés ===");
        String query = "SELECT * FROM Employes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.isBeforeFirst()) { // Vérifie si le ResultSet est vide
                System.out.println("Aucun employé trouvé.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Nom: " + rs.getString("nom") +
                        ", Prénom: " + rs.getString("prenom") +
                        ", Poste: " + rs.getString("poste") +
                        ", Email: " + rs.getString("email") +
                        ", Téléphone: " + rs.getString("telephone"));
            }
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des employés : " + e.getMessage());
        }
    }

    private static void afficherTableProjets(Connection conn) {
        System.out.println("=== Liste des Projets ===");
        String query = "SELECT * FROM Projets";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.isBeforeFirst()) { // Vérifie si le ResultSet est vide
                System.out.println("Aucun projet trouvé.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Titre: " + rs.getString("titre") +
                        ", Description: " + rs.getString("description") +
                        ", Date de début: " + rs.getDate("date_debut") +
                        ", Date de fin: " + rs.getDate("date_fin"));
            }
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des projets : " + e.getMessage());
        }
    }

    private static void afficherRelationsEmployesProjets(Connection conn) {
        System.out.println("=== Relations Employés-Projets ===");
        String query = "SELECT * FROM Employes_Projets";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.isBeforeFirst()) { // Vérifie si le ResultSet est vide
                System.out.println("Aucune relation employé-projet trouvée.\n");
                return;
            }

            while (rs.next()) {
                System.out.println("Employé ID: " + rs.getInt("employe_id") +
                        ", Projet ID: " + rs.getInt("projet_id"));
            }
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des relations employés-projets : " + e.getMessage());
        }
    }
}
