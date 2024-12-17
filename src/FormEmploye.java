import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormEmploye {
    private Integer employeId; // Null si création, non-null si modification

    public FormEmploye() {
        this.employeId = null;
    }

    public FormEmploye(Integer employeId) {
        this.employeId = employeId;
    }

    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de formulaire
        Label lblNom = new Label("Nom");
        TextField txtNom = new TextField();

        Label lblPrenom = new Label("Prénom");
        TextField txtPrenom = new TextField();

        Label lblPoste = new Label("Poste");
        TextField txtPoste = new TextField();

        Label lblEmail = new Label("Email");
        TextField txtEmail = new TextField();

        Label lblContact = new Label("Téléphone");
        TextField txtContact = new TextField();

        if (employeId != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Employes WHERE id = ?")) {
                stmt.setInt(1, employeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    txtNom.setText(rs.getString("nom"));
                    txtPrenom.setText(rs.getString("prenom"));
                    txtPoste.setText(rs.getString("poste"));
                    txtEmail.setText(rs.getString("email"));
                    txtContact.setText(rs.getString("telephone"));
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des données de l'employé : " + e.getMessage());
            }
        }

        // Boutons
        Button btnClear = new Button("Effacer");
        Button btnSubmit = new Button(employeId == null ? "Créer" : "Modifier");

        btnClear.setOnAction(e -> {
            txtNom.clear();
            txtPrenom.clear();
            txtPoste.clear();
            txtEmail.clear();
            txtContact.clear();
        });

        btnSubmit.setOnAction(e -> {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String poste = txtPoste.getText();
            String email = txtEmail.getText();
            String contact = txtContact.getText();

            if (employeId == null) {
                Employe.create(nom, prenom, poste, email, contact);
            } else {
                Employe.update(employeId, nom, prenom, poste, email, contact);
            }
            stage.close();
        });

        // Placement dans la grille
        grid.add(lblNom, 0, 0);
        grid.add(txtNom, 1, 0);
        grid.add(lblPrenom, 0, 1);
        grid.add(txtPrenom, 1, 1);
        grid.add(lblPoste, 0, 2);
        grid.add(txtPoste, 1, 2);
        grid.add(lblEmail, 0, 3);
        grid.add(txtEmail, 1, 3);
        grid.add(lblContact, 0, 4);
        grid.add(txtContact, 1, 4);
        grid.add(btnClear, 0, 5);
        grid.add(btnSubmit, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.setTitle(employeId == null ? "Nouveau Employé" : "Modifier Employé");
        stage.show();
    }
}
