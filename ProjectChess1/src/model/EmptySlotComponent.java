package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(KingImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(null, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size,Chessboard chessboard) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size,chessboard);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination, List<ChessComponent> arrayList, Chessboard CB) {
        return false;
    }

    @Override
    public void loadResource() throws IOException {
        //No resource!
    }
    @Override
    public String toString() {
        return "_";
    }

    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList,Chessboard chessboard) {
        return new ArrayList<>();
    }

    @Override
    public List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList) {
        return new ArrayList<>();
    }

    @Override
    public List<ChessboardPoint> getList1() {
        return new ArrayList<>();
    }

    @Override
    public void specialGetCanMoveTo(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {

    }

    @Override
    public void willCheckmate(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {

    }

    @Override
    public List<ChessboardPoint> getList() {
        return new ArrayList<>();
    }

}
