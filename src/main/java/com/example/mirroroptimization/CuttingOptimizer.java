package com.example.mirroroptimization;

import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CuttingOptimizer {
    private MirrorPlate plate;
    private List<MirrorPiece> pieces;

    public CuttingOptimizer(MirrorPlate plate) {
        this.plate = plate;
        this.pieces = plate.getPieces();
    }
    public void placePieces() {
        sortPiecesByAreaDescending();

        for (MirrorPiece piece : pieces) {
            if (!placePiece(piece)) {
                throw new RuntimeException("Cannot place all pieces without overlapping.");
            }
        }
        sortPiecesByPosition();
    }


    private boolean placePiece(MirrorPiece piece) {
        for (int y = 0; y <= plate.getHeight() - piece.getHeight(); y++) {
            for (int x = 0; x <= plate.getWidth() - piece.getWidth(); x++) {
                if (tryPlacePiece(piece, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sortPiecesByPosition() {
        pieces.sort(Comparator.comparingInt(MirrorPiece::getPosY).thenComparingInt(MirrorPiece::getPosX));
    }


    private boolean tryPlacePiece(MirrorPiece piece, int x, int y) {
        if (x + piece.getWidth() > plate.getWidth() || y + piece.getHeight() > plate.getHeight()) {
            return false;
        }

        for (MirrorPiece existingPiece : pieces) {
            if (existingPiece != piece && intersect(piece, x, y, existingPiece)) {
                return false;
            }
        }
        piece.setPosX(x);
        piece.setPosY(y);
        return true;
    }

    private boolean intersect(MirrorPiece piece, int x, int y, MirrorPiece existingPiece) {
        int left1 = x;
        int right1 = x + piece.getWidth();
        int top1 = y;
        int bottom1 = y + piece.getHeight();

        int left2 = existingPiece.getPosX();
        int right2 = existingPiece.getPosX() + existingPiece.getWidth();
        int top2 = existingPiece.getPosY();
        int bottom2 = existingPiece.getPosY() + existingPiece.getHeight();
        return left1 < right2 && right1 > left2 && top1 < bottom2 && bottom1 > top2;
    }

    private void sortPiecesByAreaDescending() {
        pieces.sort(Comparator.comparingInt(MirrorPiece::getArea).reversed());
    }
}
