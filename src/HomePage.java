import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Barre latérale (Aside)
        VBox aside = new VBox(20);
        aside.setPadding(new Insets(20));
        aside.setStyle("-fx-background-color: #2c3e50; -fx-pref-width: 200px;");

        Label logo = new Label("CompanyManager");
        logo.setFont(new Font("Arial", 18));
        logo.setTextFill(Color.WHITE);

        Button btnDashboard = new Button("Tableau de Bord");
        Button btnEmployes = new Button("Employés");
        Button btnProjets = new Button("Projets");
        Button btnSettings = new Button("Paramètres");

        // Style des boutons
        aside.getChildren().addAll(logo, btnDashboard, btnEmployes, btnProjets, btnSettings);
        aside.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
            }
        });

        // Contenu principal
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        // Actions des boutons de la barre latérale
        btnEmployes.setOnAction(e -> afficherEmployes(mainContent));
        btnProjets.setOnAction(e -> afficherProjets(mainContent));
        btnSettings.setOnAction(e -> afficherParametres(mainContent));

        // Mise en page générale
        HBox root = new HBox();
        root.getChildren().addAll(aside, mainContent);

        // Scène principale
        Scene scene = new Scene(root, 1000, 700); // Agrandissement de la fenêtre
        primaryStage.setTitle("CompanyManager - Accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afficherEmployes(VBox mainContent) {
        mainContent.getChildren().clear();
        Label title = new Label("Liste des Employés");
        title.setFont(new Font("Arial", 18));

        VBox employesList = new VBox(10);
        try (ResultSet rs = Employe.read()) {
            while (rs.next()) {
                VBox employeBox = new VBox(5);
                employeBox.setPadding(new Insets(10));
                employeBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-background-color: #ecf0f1; -fx-padding: 10px;");

                int employeId = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String poste = rs.getString("poste");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");

                Label employeLabel = new Label(
                        "Nom: " + nom + "\nPrénom: " + prenom + "\nPoste: " + poste + "\nEmail: " + email + "\nTéléphone: " + telephone
                );
                employeLabel.setStyle("-fx-font-size: 14px;");

                Button btnUpdate = new Button("Modifier");
                Button btnDelete = new Button("Supprimer");

                btnUpdate.setOnAction(e -> {
                    Stage updateStage = new Stage();
                    FormEmploye formEmploye = new FormEmploye(employeId);
                    formEmploye.start(updateStage);
                    updateStage.setOnHiding(event -> afficherEmployes(mainContent));
                });

                btnDelete.setOnAction(e -> {
                    Employe.delete(employeId);
                    afficherEmployes(mainContent);
                });

                HBox actions = new HBox(10, btnUpdate, btnDelete);
                employeBox.getChildren().addAll(employeLabel, actions);
                employesList.getChildren().add(employeBox);
            }
        } catch (SQLException e) {
            employesList.getChildren().add(new Label("Erreur lors du chargement des employés."));
        }

        Button btnAddEmploye = new Button("Ajouter un Employé");
        btnAddEmploye.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 5px;");
        btnAddEmploye.setOnAction(e -> {
            FormEmploye formEmploye = new FormEmploye();
            Stage employeStage = new Stage();
            formEmploye.start(employeStage);
            afficherEmployes(mainContent);
        });

        mainContent.getChildren().addAll(title, employesList, btnAddEmploye);
    }

    private void afficherProjets(VBox mainContent) {
        mainContent.getChildren().clear();
        Label title = new Label("Liste des Projets");
        title.setFont(new Font("Arial", 18));

        VBox projetsList = new VBox(10);
        try (ResultSet rs = Projet.read()) {
            while (rs.next()) {
                VBox projetBox = new VBox(5);
                projetBox.setPadding(new Insets(10));
                projetBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-background-color: #ecf0f1; -fx-padding: 10px;");

                int projetId = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String dateDebut = rs.getString("date_debut");
                String dateFin = rs.getString("date_fin");

                Label projetLabel = new Label(
                        "Titre: " + titre + "\nDescription: " + description + "\nDate de début: " + dateDebut + "\nDate de fin: " + dateFin
                );
                projetLabel.setStyle("-fx-font-size: 14px;");

                // Récupération des employés assignés
                String employesAssignes = getEmployesAssignes(projetId);
                Label employesLabel = new Label("Employés: " + (employesAssignes.isEmpty() ? "Aucun employé en charge du projet pour le moment." : employesAssignes));
                employesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

                Button btnUpdate = new Button("Modifier");
                Button btnDelete = new Button("Supprimer");
                Button btnAssign = new Button("Assigner Employés");

                btnUpdate.setOnAction(e -> {
                    Stage updateStage = new Stage();
                    FormProjet formProjet = new FormProjet(projetId, titre, description, dateDebut, dateFin);
                    formProjet.start(updateStage);
                    updateStage.setOnHiding(event -> afficherProjets(mainContent));
                });

                btnDelete.setOnAction(e -> {
                    Projet.delete(projetId);
                    afficherProjets(mainContent);
                });

                btnAssign.setOnAction(e -> assignEmployesToProject(projetId));

                HBox actions = new HBox(10, btnUpdate, btnDelete, btnAssign);
                projetBox.getChildren().addAll(projetLabel, employesLabel, actions);
                projetsList.getChildren().add(projetBox);
            }
        } catch (SQLException e) {
            projetsList.getChildren().add(new Label("Erreur lors du chargement des projets."));
        }

        Button btnAddProjet = new Button("Ajouter un Projet");
        btnAddProjet.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 5px;");
        btnAddProjet.setOnAction(e -> {
            FormProjet formProjet = new FormProjet();
            Stage projetStage = new Stage();
            formProjet.start(projetStage);
            afficherProjets(mainContent);
        });

        mainContent.getChildren().addAll(title, projetsList, btnAddProjet);
    }

    private String getEmployesAssignes(int projetId) {
        StringBuilder employes = new StringBuilder();

        String query = "SELECT e.nom, e.prenom FROM Employes e INNER JOIN Employes_Projets ep ON e.id = ep.employe_id WHERE ep.projet_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projetId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employes.append(rs.getString("nom")).append(" ").append(rs.getString("prenom")).append(", ");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des employés assignés : " + e.getMessage());
        }

        if (employes.length() > 0) {
            employes.setLength(employes.length() - 2); // Supprimer la dernière virgule
        }

        return employes.toString();
    }

    private void assignEmployesToProject(int projetId) {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Assigner un Employé au Projet ID: " + projetId
        );
        title.setFont(new Font("Arial", 18));

        VBox employeList = new VBox(10);
        try (ResultSet rs = Employe.read()) {
            while (rs.next()) {
                int employeId = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");

                HBox employeBox = new HBox(10);
                Label employeLabel = new Label(nom + " " + prenom);
                Button btnAssign = new Button("Assigner");

                btnAssign.setOnAction(e -> {
                    Projet.assignEmployeToProjet(projetId, employeId);
                    stage.close();
                    System.out.println("Employé ID " + employeId + " assigné au projet ID " + projetId);
                    afficherProjets((VBox) stage.getOwner().getScene().getRoot().lookup(".main-content"));
                });

                employeBox.getChildren().addAll(employeLabel, btnAssign);
                employeList.getChildren().add(employeBox);
            }
        } catch (SQLException e) {
            employeList.getChildren().add(new Label("Erreur lors du chargement des employés."));
        }

        root.getChildren().addAll(title, employeList);
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Assigner Employé");
        stage.setScene(scene);
        stage.show();
    }
// --------------------------------------------------------------------------------- RAPPORT ---------------------------------------------------------------------------------
public void afficherParametres(VBox mainContent) {
    mainContent.getChildren().clear();

    Label title = new Label("Rapport de projets");
    title.setFont(new Font("Arial", 18));

    // Bouton pour afficher la liste des projets avec leurs employés
    Button btnRapportProjets = new Button("Liste des Projets avec leurs Employés");
    btnRapportProjets.setOnAction(e -> Rapport.afficherRapportProjets(mainContent));

    // Bouton pour afficher la liste des employés avec leurs projets associés
    Button btnRapportEmployes = new Button("Liste des Employés avec leurs Projets");
    btnRapportEmployes.setOnAction(e -> Rapport.afficherRapportEmployes(mainContent));

    mainContent.getChildren().addAll(title, btnRapportProjets, btnRapportEmployes);
}
    public static void main(String[] args) {
        launch(args);
    }
}
