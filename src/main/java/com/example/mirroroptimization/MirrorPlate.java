package com.example.mirroroptimization;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class MirrorPlate {
    private double width;
    private double height;
    private List<MirrorPiece> pieces;

    public MirrorPlate(double width, double height) {
        this.width = width;
        this.height = height;
        this.pieces = new ArrayList<>();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<MirrorPiece> getPieces() {
        return pieces;
    }

    public void addPiece(MirrorPiece piece) {
        pieces.add(piece);
    }
}
