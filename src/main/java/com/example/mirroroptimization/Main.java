package com.example.mirroroptimization;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // App Icon
        Image appIcon = new Image(getClass().getResourceAsStream("/squares.png"));
        primaryStage.getIcons().add(appIcon);

        primaryStage.setTitle("Ayna Optimizasyonu");

        // Primary Stage ekran ayarları
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);

        // Icon
        ImageView githubLogo = new ImageView(new Image(getClass().getResource("/github-mark-white.png").toExternalForm()));
        githubLogo.setFitWidth(20);
        githubLogo.setFitHeight(20);

        Hyperlink githubLink = new Hyperlink("github.com/ask0", githubLogo);
        githubLink.setOnAction(e -> {
            getHostServices().showDocument("https://github.com/ask0");
        });

        VBox bottomRegion = new VBox();
        bottomRegion.setPadding(new Insets(10, 10, 10, 10));
        bottomRegion.setSpacing(-28);
        bottomRegion.setStyle("-fx-background-color: #333; -fx-font-size: 14; -fx-text-fill: white;");
        bottomRegion.setMinHeight(30); // Set a minimum height
        bottomRegion.setMaxHeight(30); // Set a maximum height

        Label authorLabel = new Label("");
        bottomRegion.getChildren().addAll(authorLabel, githubLink);

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

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);

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

            double plateWidth, plateHeight;
            int pieceCount;

            try {
                plateWidth = Double.parseDouble(plateWidthText);
                plateHeight = Double.parseDouble(plateHeightText);
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
                    double pieceWidth, pieceHeight;

                    try {
                        pieceWidth = Double.parseDouble(widthField.getText());
                        pieceHeight = Double.parseDouble(heightField.getText());
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

                rowIndex += 3;
            }
            // Optimize et butonu
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
                if (!optimizer.placePieces()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hata");
                    alert.setHeaderText(null);
                    alert.setContentText("Ayna parçaları, plaka ölçülerini aşıyor!");
                    alert.showAndWait();
                    return;
                }


                if (!optimizer.placePieces()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hata");
                    alert.setHeaderText(null);
                    alert.setContentText("Ayna parçaları, plaka ölçülerini aşıyor!");
                    alert.showAndWait();
                    return;
                }

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

                ScrollPane zoomableScrollPane = new ScrollPane();
                zoomableScrollPane.setContent(root);
                zoomableScrollPane.addEventFilter(ScrollEvent.ANY, event1 -> {
                    if (event1.isControlDown()) {
                        double zoomFactor = 1.05;
                        double deltaY = event1.getDeltaY();
                        if (deltaY < 0){
                            zoomFactor = 0.95;
                        }

                        double mouseX = event1.getX();
                        double mouseY = event1.getY();

                        double pivotX = root.getTranslateX() + mouseX;
                        double pivotY = root.getTranslateY() + mouseY;

                        Scale scale = new Scale(zoomFactor, zoomFactor, pivotX, pivotY);
                        root.getTransforms().add(scale);

                        event.consume();
                    }
                });
                BorderPane borderPane = new BorderPane();
                borderPane.setTop(gridPane);
                borderPane.setBottom(bottomRegion);
                borderPane.setCenter(zoomableScrollPane);
                scene = new Scene(borderPane, 600, 600);


                primaryStage.setScene(scene);
                primaryStage.show();


                // Görüntüyü kaydet
                WritableImage wImage = scene.snapshot(null);
                BufferedImage bImage = new BufferedImage((int) scene.getWidth(), (int) scene.getHeight(), BufferedImage.TYPE_INT_ARGB);
                PixelReader reader = wImage.getPixelReader();
                for (int y = 0; y < scene.getHeight(); y++) {
                    for (int x = 0; x < scene.getWidth(); x++) {
                        bImage.setRGB(x, y, reader.getArgb(x, y));
                    }
                }

                // Kaydetme yolu seç
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
                File outFile = fileChooser.showSaveDialog(primaryStage);

                // Dosyaya yaz
                if (outFile != null) {
                    try {
                        ImageIO.write(bImage, "png", outFile);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        });

        scrollPane.setFitToWidth(true);  // içerik genişliği pane genişliğine uyacak şekilde ayarlanır
        scrollPane.setFitToHeight(true); // içerik yüksekliği pane yüksekliğine uyacak şekilde ayarlanır

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(bottomRegion);
        Scene scene = new Scene(borderPane, primaryStage.getWidth() / 2, primaryStage.getHeight() / 2); // Ana pencere boyutu
        gridPane.setStyle("-fx-background-color: gray;");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}