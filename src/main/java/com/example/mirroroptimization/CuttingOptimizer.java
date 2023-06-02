package com.example.mirroroptimization;

import javafx.scene.control.Alert;

public class CuttingOptimizer {
    private MirrorPlate plate;

    public CuttingOptimizer(MirrorPlate plate) {
        this.plate = plate;
    }

    public void placePieces() {
        int x = 0, y = 0;

        for (MirrorPiece piece : plate.getPieces()) {
            // Parçayı yerleştir
            piece.setPosX(x);
            piece.setPosY(y);

            // Diğer tüm parçalarla çakışıp çakışmadığını kontrol et
            for (MirrorPiece otherPiece : plate.getPieces()) {
                if (piece != otherPiece && piece.isOverlap(otherPiece)) {
                    // Çakışma varsa, parçayı sağa hareket ettir ve çakışmayı kontrol etmeye devam et
                    x += otherPiece.getWidth();
                    piece.setPosX(x);

                    // Eğer parça plaka sınırlarını aşıyorsa, bir alt satıra geç ve x koordinatını sıfırla
                    if (x + piece.getWidth() > plate.getWidth()) {
                        x = 0;
                        y += otherPiece.getHeight();
                        piece.setPosX(x);
                        piece.setPosY(y);
                    }

                    // Yeni parça için yer ayır
                    x += piece.getWidth();
                }
            }

            // Eğer parça plaka sınırlarını aşıyorsa, hata ver ve döngüyü kır
            if (x + piece.getWidth() > plate.getWidth() || y + piece.getHeight() > plate.getHeight()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Ayna parçaları plaka sınırlarını aştı!");
                alert.showAndWait();
                break;
            }
        }
    }
}
