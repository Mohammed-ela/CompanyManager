import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:8889/CompanyManager";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Méthode pour obtenir une connexion à la base de données
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL introuvable ! " + e.getMessage());
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à MySQL : " + e.getMessage());
            throw e;
        }
    }



    // Méthode pour tester la connexion
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Connexion réussie à la base de données !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
}
