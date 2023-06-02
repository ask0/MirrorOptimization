package com.example.mirroroptimization;

public class MirrorPiece {
    private int width;
    private int height;
    private int posX;
    private int posY;

    public MirrorPiece(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getArea() {
        return width * height;
    }

    public boolean isOverlap(MirrorPiece otherPiece) {
        int thisRight = posX + width;
        int thisBottom = posY + height;
        int otherRight = otherPiece.posX + otherPiece.width;
        int otherBottom = otherPiece.posY + otherPiece.height;

        if (posX >= otherRight || otherPiece.posX >= thisRight)
            return false;
        if (posY >= otherBottom || otherPiece.posY >= thisBottom)
            return false;

        return true;
    }
}
