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

public class FormProjet {
    private Integer projetId;

    public FormProjet() {
        this.projetId = null;
    }

    public FormProjet(Integer projetId, String titre, String description, String dateDebut, String dateFin) {
        this.projetId = projetId;
    }

    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de formulaire
        Label lblTitre = new Label("Titre");
        TextField txtTitre = new TextField();

        Label lblDescription = new Label("Description");
        TextField txtDescription = new TextField();

        Label lblDateDebut = new Label("Date de début (YYYY-MM-DD)");
        TextField txtDateDebut = new TextField();

        Label lblDateFin = new Label("Date de fin (YYYY-MM-DD)");
        TextField txtDateFin = new TextField();

        if (projetId != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Projets WHERE id = ?")) {
                stmt.setInt(1, projetId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    txtTitre.setText(rs.getString("titre"));
                    txtDescription.setText(rs.getString("description"));
                    txtDateDebut.setText(rs.getString("date_debut"));
                    txtDateFin.setText(rs.getString("date_fin"));
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des données du projet : " + e.getMessage());
            }
        }

        // Boutons
        Button btnClear = new Button("Effacer");
        Button btnSubmit = new Button(projetId == null ? "Créer" : "Modifier");

        btnClear.setOnAction(e -> {
            txtTitre.clear();
            txtDescription.clear();
            txtDateDebut.clear();
            txtDateFin.clear();
        });

        btnSubmit.setOnAction(e -> {
            String titre = txtTitre.getText();
            String description = txtDescription.getText();
            String dateDebut = txtDateDebut.getText();
            String dateFin = txtDateFin.getText();

            if (projetId == null) {
                Projet.create(titre, description, dateDebut, dateFin);
            } else {
                Projet.update(projetId, titre, description, dateDebut, dateFin);
            }
            stage.close();
        });

        grid.add(lblTitre, 0, 0);
        grid.add(txtTitre, 1, 0);
        grid.add(lblDescription, 0, 1);
        grid.add(txtDescription, 1, 1);
        grid.add(lblDateDebut, 0, 2);
        grid.add(txtDateDebut, 1, 2);
        grid.add(lblDateFin, 0, 3);
        grid.add(txtDateFin, 1, 3);
        grid.add(btnClear, 0, 4);
        grid.add(btnSubmit, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.setTitle(projetId == null ? "Nouveau Projet" : "Modifier Projet");
        stage.show();
    }
}
