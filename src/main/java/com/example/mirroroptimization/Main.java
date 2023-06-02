package com.example.mirroroptimization;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ayna Optimizasyonu");

        // Form bileşenlerini oluştur
        Label plateWidthLabel = new Label("Ayna plakasının genişliği:");
        TextField plateWidthField = new TextField();
        Label plateHeightLabel = new Label("Ayna plakasının yüksekliği:");
        TextField plateHeightField = new TextField();
        Label pieceCountLabel = new Label("Ayna parça sayısı:");
        TextField pieceCountField = new TextField();
        Button submitButton = new Button("Gönder");

        // Form düzenini oluştur
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(plateWidthLabel, 0, 0);
        gridPane.add(plateWidthField, 1, 0);
        gridPane.add(plateHeightLabel, 0, 1);
        gridPane.add(plateHeightField, 1, 1);
        gridPane.add(pieceCountLabel, 0, 2);
        gridPane.add(pieceCountField, 1, 2);
        gridPane.add(submitButton, 0, 3, 2, 1);

        submitButton.setOnAction(e -> {
            String plateWidthText = plateWidthField.getText();
            String plateHeightText = plateHeightField.getText();

            if (plateWidthText.isEmpty() || plateHeightText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setHeaderText(null);
                alert.setContentText("Ayna plakası ölçüleri boş olamaz!");
                alert.showAndWait();
                return;
            }

            int plateWidth, plateHeight, pieceCount;

            try {
                plateWidth = Integer.parseInt(plateWidthText);
                plateHeight = Integer.parseInt(plateHeightText);
                pieceCount = Integer.parseInt(pieceCountField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setHeaderText(null);
                alert.setContentText("Ayna plakası ölçüleri ve parça sayısı geçerli bir sayı olmalıdır!");
                alert.showAndWait();
                return;
            }

            if (plateWidth <= 0 || plateHeight <= 0 || pieceCount <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setHeaderText(null);
                alert.setContentText("Ayna plakası ölçüleri ve parça sayısı 0 veya negatif olamaz!");
                alert.showAndWait();
                return;
            }

            // Ayna plakasını oluştur
            MirrorPlate plate = new MirrorPlate(plateWidth, plateHeight);

            // Ayna parçalarını kullanıcıdan al ve ayna plakasına ekle
            List<MirrorPiece> pieces = new ArrayList<>();
            int rowIndex = 4;
            for (int i = 0; i < pieceCount; i++) {
                Label widthLabel = new Label("Ayna parçası " + (i + 1) + " - Genişlik:");
                TextField widthField = new TextField();
                Label heightLabel = new Label("Ayna parçası " + (i + 1) + " - Yükseklik:");
                TextField heightField = new TextField();

                gridPane.add(widthLabel, 0, rowIndex);
                gridPane.add(widthField, 1, rowIndex);
                gridPane.add(heightLabel, 0, rowIndex + 1);
                gridPane.add(heightField, 1, rowIndex + 1);

                Button addPieceButton = new Button("Ayna Parçasını Ekle");
                gridPane.add(addPieceButton, 0, rowIndex + 2, 2, 1);
                addPieceButton.setOnAction(event -> {
                    int pieceWidth, pieceHeight;

                    try {
                        pieceWidth = Integer.parseInt(widthField.getText());
                        pieceHeight = Integer.parseInt(heightField.getText());
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Uyarı");
                        alert.setHeaderText(null);
                        alert.setContentText("Ayna parçası ölçüleri geçerli bir sayı olmalıdır!");
                        alert.showAndWait();
                        return;
                    }

                    if (pieceWidth <= 0 || pieceHeight <= 0) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Uyarı");
                        alert.setHeaderText(null);
                        alert.setContentText("Ayna parçası ölçüleri 0 veya negatif olamaz!");
                        alert.showAndWait();
                        return;
                    }

                    MirrorPiece piece = new MirrorPiece(pieceWidth, pieceHeight);
                    pieces.add(piece);
                    plate.addPiece(piece); // Ayna plakasına parçayı ekliyoruz

                    // Yeni ayna parçası için etiketleri ve alanları temizle
                    widthField.setText("");
                    heightField.setText("");
                });


               // gridPane.add(addPieceButton, 0, rowIndex + 2, 2, 1);
                rowIndex += 3;
            }

            Button optimizeButton = new Button("Optimize Et");
            gridPane.add(optimizeButton, 0, rowIndex + 3, 2, 1);
            optimizeButton.setOnAction(event -> {
                if (pieces.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Uyarı");
                    alert.setHeaderText(null);
                    alert.setContentText("En az bir ayna parçası eklenmelidir!");
                    alert.showAndWait();
                    return;
                }
                // Ayna parçalarını yerleştir
                CuttingOptimizer optimizer = new CuttingOptimizer(plate);
                optimizer.placePieces();

                // Ayna plakasını ve ayna parçalarını göster
                Group root = new Group();
                Scene scene = new Scene(root, plateWidth * 2, plateHeight * 2); // Ölçeği 2 kat büyüt

                // Ayna plakasını çiz
                Rectangle plateRect = new Rectangle(0, 0, plateWidth * 2, plateHeight * 2);
                plateRect.setFill(Color.YELLOW); // Plaka iç bölgesini sarı olarak ayarla
                plateRect.setStroke(Color.BLUE); // Plaka kenarlarını mavi olarak ayarla
                root.getChildren().add(plateRect);

                // Ayna parçalarını çiz
                for (MirrorPiece piece : pieces) {
                    Rectangle pieceRect = new Rectangle(piece.getPosX() * 2, piece.getPosY() * 2, piece.getWidth() * 2, piece.getHeight() * 2);
                    pieceRect.setFill(Color.LIGHTGRAY);
                    pieceRect.setStroke(Color.RED); // Kenar rengini kırmızı olarak ayarla
                    root.getChildren().add(pieceRect);
                }

                primaryStage.setScene(scene);
                primaryStage.show();
            });
        });

        Scene scene = new Scene(gridPane, 400, 400); // Ana pencere boyutu
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
