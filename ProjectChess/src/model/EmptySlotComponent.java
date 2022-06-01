package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, ClickController listener, int size, int size1, Chessboard chessboard) {
        super(chessboardPoint, ChessColor.NONE, listener, size ,size1, chessboard);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination, List<ChessComponent> arrayList, Chessboard CB) {
        return false;
    }

    @Override
    public void loadResource() {
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

