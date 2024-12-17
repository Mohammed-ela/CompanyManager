import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rapport {

    public static void afficherRapportProjets(VBox mainContent) {
        mainContent.getChildren().clear();

        // Bouton de retour
        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-font-size: 14px;");
        btnRetour.setOnAction(e -> new HomePage().afficherParametres(mainContent));

        Label title = new Label("Liste des Projets avec leurs Employés");
        title.setFont(new javafx.scene.text.Font("Arial", 18));

        VBox rapportList = new VBox(10);
        String query = "SELECT e.nom AS employe_nom, e.prenom AS employe_prenom, p.titre AS projet " +
                "FROM Employes e " +
                "LEFT JOIN Employes_Projets ep ON e.id = ep.employe_id " +
                "LEFT JOIN Projets p ON ep.projet_id = p.id " +
                "ORDER BY e.nom, e.prenom;";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            String currentProject = "";
            VBox currentProjectBox = null;

            while (rs.next()) {
                String projet = rs.getString("projet");
                String employe = rs.getString("employe_nom") + " " + rs.getString("employe_prenom");

                // Nouveau projet
                if (!projet.equals(currentProject)) {
                    currentProject = projet;
                    currentProjectBox = new VBox(5);
                    currentProjectBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-background-color: #ecf0f1; -fx-padding: 10px;");

                    Label projetLabel = new Label("Projet : " + projet);
                    projetLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    currentProjectBox.getChildren().add(projetLabel);
                    rapportList.getChildren().add(currentProjectBox);
                }

                // Ajouter les employés au projet
                if (employe.trim().isEmpty()) {
                    employe = "Aucun employé assigné.";
                }
                currentProjectBox.getChildren().add(new Label(" - " + employe));
            }
        } catch (SQLException e) {
            rapportList.getChildren().add(new Label("Erreur lors de la génération du rapport des projets : " + e.getMessage()));
        }

        mainContent.getChildren().addAll(btnRetour, title, rapportList);
    }

    public static void afficherRapportEmployes(VBox mainContent) {
        mainContent.getChildren().clear();

        // Bouton de retour
        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-font-size: 14px;");
        btnRetour.setOnAction(e -> new HomePage().afficherParametres(mainContent));

        Label title = new Label("Liste des Employés avec leurs Projets");
        title.setFont(new javafx.scene.text.Font("Arial", 18));

        VBox rapportList = new VBox(10);
        String query = "SELECT e.nom AS employe_nom, e.prenom AS employe_prenom, p.titre AS projet " +
                "FROM Employes e " +
                "LEFT JOIN Employes_Projets ep ON e.id = ep.employe_id " +
                "LEFT JOIN Projets p ON ep.projet_id = p.id " +
                "ORDER BY e.nom, e.prenom;";


        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            String currentEmployee = "";
            VBox currentEmployeeBox = null;

            while (rs.next()) {
                String employe = rs.getString("employe_nom") + " " + rs.getString("employe_prenom");
                String projet = rs.getString("projet");

                // Nouvel employé
                if (!employe.equals(currentEmployee)) {
                    currentEmployee = employe;
                    currentEmployeeBox = new VBox(5);
                    currentEmployeeBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-background-color: #ecf0f1; -fx-padding: 10px;");

                    Label employeLabel = new Label("Employé : " + employe);
                    employeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    currentEmployeeBox.getChildren().add(employeLabel);
                    rapportList.getChildren().add(currentEmployeeBox);
                }

                // Ajouter les projets à l'employé
                if (projet == null) {
                    projet = "Aucun projet assigné.";
                }
                currentEmployeeBox.getChildren().add(new Label(" - " + projet));
            }
        } catch (SQLException e) {
            rapportList.getChildren().add(new Label("Erreur lors de la génération du rapport des employés : " + e.getMessage()));
        }

        mainContent.getChildren().addAll(btnRetour, title, rapportList);
    }
}
