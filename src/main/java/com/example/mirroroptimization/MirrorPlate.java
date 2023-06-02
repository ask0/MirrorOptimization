package com.example.mirroroptimization;

import java.util.ArrayList;
import java.util.List;

public class MirrorPlate {
    private int width;
    private int height;
    private List<MirrorPiece> pieces;

    public MirrorPlate(int width, int height) {
        this.width = width;
        this.height = height;
        this.pieces = new ArrayList<>();
    }

    // Getters, setters...

    public void addPiece(MirrorPiece piece) {
        pieces.add(piece);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<MirrorPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<MirrorPiece> pieces) {
        this.pieces = pieces;
    }
}
