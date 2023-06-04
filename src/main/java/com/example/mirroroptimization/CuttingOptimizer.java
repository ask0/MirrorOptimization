package com.example.mirroroptimization;

import javafx.scene.control.Alert;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CuttingOptimizer {
    private MirrorPlate plate;

    public CuttingOptimizer(MirrorPlate plate) {
        this.plate = plate;
    }

    public boolean placePieces() {
        double x = 0;
        double y = 0;

        for (MirrorPiece piece : plate.getPieces()) {
            if (x + piece.getWidth() <= plate.getWidth() && y + piece.getHeight() <= plate.getHeight()) {
                piece.setPosX(x);
                piece.setPosY(y);
                x += piece.getWidth();
                if (x >= plate.getWidth()) {
                    x = 0;
                    y += piece.getHeight();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private double getMaxHeightInCurrentRow(List<MirrorPiece> pieces, double x) {
        return pieces.stream()
                .filter(p -> p.getPosX() >= x)
                .mapToDouble(MirrorPiece::getHeight)
                .max()
                .orElse(0);
    }
}
