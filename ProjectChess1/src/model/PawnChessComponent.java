package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PawnChessComponent extends ChessComponent {

    private static Image Pawn_WHITE;
    private static Image Pawn_BLACK;

    private Image PawnImage;
    private List<ChessboardPoint> defeatRange=new ArrayList<>();


    //读取加载车棋子的图片
    public void loadResource() throws IOException {
        if (Pawn_WHITE == null) {
            Pawn_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (Pawn_BLACK == null) {
            Pawn_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }

    public int getMove() {
        return move;
    }

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                PawnImage = Pawn_WHITE;
            } else if (color == ChessColor.BLACK) {
                PawnImage = Pawn_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,Chessboard chessboard) {
        super(chessboardPoint, location, color, listener, size,chessboard);
        initiatePawnImage(color);
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
        g.drawImage(PawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "P";
        }
        else return "p";
    }
    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0,list1.size()).clear();
        defeatRange(chessComponents,arrayList);
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (chessColor==ChessColor.BLACK){
            if (x==1){
                for (int i=1;i<3;i++){
                    if (!(chessComponents[x+i][y] instanceof EmptySlotComponent)){
                        break;
                    }
                    list.add(new ChessboardPoint(x+i,y));
                }
            }
            if (x>1&&x<7){
                if (chessComponents[x+1][y] instanceof EmptySlotComponent){
                    list.add(new ChessboardPoint(x+1,y));
                }
            }
            if (y==0&&x<7){
                if (chessComponents[x+1][y+1].chessColor==ChessColor.WHITE){
                    list.add(new ChessboardPoint(x+1,y+1));
                }
            }
            if (y==7&&x<7){
                if (chessComponents[x+1][y-1].chessColor==ChessColor.WHITE){
                    list.add(new ChessboardPoint(x+1,y-1));
                }
            }
            if (y>0&&y<7&&x<7){
                if (chessComponents[x+1][y+1].chessColor==ChessColor.WHITE){
                    list.add(new ChessboardPoint(x+1,y+1));
                }
                if (chessComponents[x+1][y-1].chessColor==ChessColor.WHITE){
                    list.add(new ChessboardPoint(x+1,y-1));
                }
            }
        }
        if (chessColor==ChessColor.WHITE){
            if (x==6){
                for (int i=-1;i>-3;i--){
                    if (!(chessComponents[x+i][y] instanceof EmptySlotComponent)){
                        break;
                    }
                    list.add(new ChessboardPoint(x+i,y));
                }
            }
            if (x>0&&x<6){
                if (chessComponents[x-1][y] instanceof EmptySlotComponent){
                    list.add(new ChessboardPoint(x-1,y));
                }
            }
            if (y==0&&x>0){
                if (chessComponents[x-1][y+1].chessColor==ChessColor.BLACK){
                    list.add(new ChessboardPoint(x-1,y+1));
                }
            }
            if (y==7&&x>0){
                if (chessComponents[x-1][y-1].chessColor==ChessColor.BLACK){
                    list.add(new ChessboardPoint(x-1,y-1));
                }
            }
            if (y>0&&y<7&&x>0){
                if (chessComponents[x-1][y+1].chessColor==ChessColor.BLACK){
                    list.add(new ChessboardPoint(x-1,y+1));
                }
                if (chessComponents[x-1][y-1].chessColor==ChessColor.BLACK){
                    list.add(new ChessboardPoint(x-1,y-1));
                }
            }
        }
        willCheckmate(chessComponents,arrayList,chessboard);
        return list;
    }
    @Override
    public List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList){
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (chessColor==ChessColor.BLACK&&x<7){
            if (y<7){
                    list1.add(new ChessboardPoint(x+1,y+1));
                }
            if (y>0){
                    list1.add(new ChessboardPoint(x+1,y-1));
                }
        }
        if (chessColor==ChessColor.WHITE&&x>0){
            if (y<7){
                    list1.add(new ChessboardPoint(x-1,y+1));
                }
            if (y>0){
                    list1.add(new ChessboardPoint(x-1,y-1));
            }
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
