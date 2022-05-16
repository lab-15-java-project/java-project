package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class KnightChessComponent extends ChessComponent{

    private static Image Knight_WHITE;
    private static Image Knight_BLACK;

    private Image KnightImage;

    //读取加载车棋子的图片
    public void loadResource() throws IOException {
        if (Knight_WHITE == null) {
            Knight_WHITE = ImageIO.read(new File("./images/knight-white.png"));
        }

        if (Knight_BLACK == null) {
            Knight_BLACK = ImageIO.read(new File("./images/knight-black.png"));
        }
    }

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                KnightImage = Knight_WHITE;
            } else if (color == ChessColor.BLACK) {
                KnightImage = Knight_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color);
    }




    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(KingImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(KnightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "N";
        }
        else return "n";
    }

    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0,list1.size()).clear();
        defeatRange(chessComponents,arrayList);
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (x+1<8&&y+2<8){
            if (chessComponents[x+1][y+2].chessColor!=chessColor){
                list.add(new ChessboardPoint(x+1,y+2));
            }
        }
        if (x+2<8&&y+1<8) {
            if (chessComponents[x + 2][y + 1].chessColor != chessColor) {
                list.add(new ChessboardPoint(x + 2, y + 1));
            }
        }
        if (x-1>-1&&y+2<8) {
            if (chessComponents[x - 1][y + 2].chessColor != chessColor) {
                list.add(new ChessboardPoint(x - 1, y + 2));
            }
        }
        if (x-1>-1&&y-2>-1) {
            if (chessComponents[x -1][y - 2].chessColor != chessColor) {
                list.add(new ChessboardPoint(x - 1, y - 2));
            }
        }
        if (x+1<8&&y-2>-1) {
            if (chessComponents[x + 1][y - 2].chessColor != chessColor) {
                list.add(new ChessboardPoint(x + 1, y - 2));
            }
        }
        if (x+2<8&&y-1>-1) {
            if (chessComponents[x + 2][y -1].chessColor != chessColor) {
                list.add(new ChessboardPoint(x + 2, y -1));
            }
        }
        if (x-2>-1&&y+1<8) {
            if (chessComponents[x -2][y + 1].chessColor != chessColor) {
                list.add(new ChessboardPoint(x -2, y + 1));
            }
        }
        if (x-2>-1&&y-1>-1) {
            if (chessComponents[x -2][y -1].chessColor != chessColor) {
                list.add(new ChessboardPoint(x -2, y -1));
            }
        }
        willCheckmate(chessComponents,arrayList,chessboard);
        return list;
    }

    @Override
    public List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList) {
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (x+1<8&&y+2<8){
                list1.add(new ChessboardPoint(x+1,y+2));
        }
        if (x+2<8&&y+1<8) {
                list1.add(new ChessboardPoint(x + 2, y + 1));
        }
        if (x-1>-1&&y+2<8) {
                list1.add(new ChessboardPoint(x - 1, y + 2));
        }
        if (x-1>-1&&y-2>-1) {
                list1.add(new ChessboardPoint(x - 1, y - 2));
        }
        if (x+1<8&&y-2>-1) {
                list1.add(new ChessboardPoint(x + 1, y - 2));
        }
        if (x+2<8&&y-1>-1) {
                list1.add(new ChessboardPoint(x + 2, y -1));
        }
        if (x-2>-1&&y+1<8) {
                list1.add(new ChessboardPoint(x -2, y + 1));
        }
        if (x-2>-1&&y-1>-1) {
                list1.add(new ChessboardPoint(x -2, y -1));
        }
        return list1;
    }

    @Override
    public List<ChessboardPoint> getList1() {
        return list1;
    }



    @Override
    public List<ChessboardPoint> getList() {
        return list;
    }
}
