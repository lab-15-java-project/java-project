package model;

import controller.ClickController;
import jdk.nashorn.internal.ir.WhileNode;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BishopChessComponent extends ChessComponent{

    private static Image Bishop_White;
    private static Image Bishop_BLACK;

    private Image BishopImage;

    public void loadResource() throws IOException {
        if (Bishop_White == null) {
            Bishop_White = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (Bishop_BLACK == null) {
            Bishop_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                BishopImage = Bishop_White;
            } else if (color == ChessColor.BLACK) {
                BishopImage = Bishop_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color);
    }


    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0, list1.size()).clear();
        defeatRange(chessComponents, arrayList);
        ChessboardPoint source = getChessboardPoint();
        int x = source.getX();
        int y = source.getY();
        for (int i = 1; ; i++) {
            if (x + i >= 8 || y + i >= 8) {
                break;
            }
            if (!(chessComponents[x + i][y + i] instanceof EmptySlotComponent)) {
                if (chessColor != chessComponents[x + i][y + i].chessColor) {
                    list.add(new ChessboardPoint(x + i, y + i));
                }
                break;
            }
            list.add(new ChessboardPoint(x + i, y + i));
        }
        for (int i = -1; ; i--) {
            if (x + i < 0 || y + i < 0) {
                break;
            }
            if (!(chessComponents[x + i][y + i] instanceof EmptySlotComponent)) {
                if (chessColor.getColor() != chessComponents[x + i][y + i].chessColor.getColor()) {
                    list.add(new ChessboardPoint(x + i, y + i));
                }
                break;
            }
            list.add(new ChessboardPoint(x + i, y + i));
        }
        for (int i = 1; ; i++) {
            if (x - i < 0 || y + i >= 8) {
                break;
            }
            if (!(chessComponents[x - i][y + i] instanceof EmptySlotComponent)) {
                if (chessColor.getColor() != chessComponents[x - i][y + i].chessColor.getColor()) {
                    list.add(new ChessboardPoint(x - i, y + i));
                }
                break;
            }
            list.add(new ChessboardPoint(x - i, y + i));
        }
        for (int i = -1; ; i--) {
            if (x - i >= 8 || y + i < 0) {
                break;
            }
            if (!(chessComponents[x - i][y + i] instanceof EmptySlotComponent)) {
                if (chessColor.getColor() != chessComponents[x - i][y + i].chessColor.getColor()) {
                    list.add(new ChessboardPoint(x - i, y + i));
                }
                break;
            }
            list.add(new ChessboardPoint(x - i, y + i));
        }
        willCheckmate(chessComponents,arrayList,chessboard);
        return list;
    }
    @Override
    public List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList) {
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        for (int i=1;;i++){
            if (x+i>=8||y+i>=8){
                break;
            }
            if (!(chessComponents[x+i][y+i] instanceof EmptySlotComponent
                    ||chessComponents[x+i][y+i] instanceof KingChessComponent)){
                list1.add(new ChessboardPoint(x+i,y+i));
                break;
            }
            if (chessComponents[x+i][y+i] instanceof KingChessComponent&&chessComponents[x+i][y+i].chessColor==chessColor){
                list1.add(new ChessboardPoint(x+i,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x+i,y+i));
        }
        for (int i=-1;;i--){
            if (x+i<0||y+i<0){
                break;
            }
            if (!(chessComponents[x+i][y+i] instanceof EmptySlotComponent
                    ||chessComponents[x+i][y+i] instanceof KingChessComponent)){
                    list1.add(new ChessboardPoint(x+i,y+i));
                break;
            }
            if (chessComponents[x+i][y+i] instanceof KingChessComponent&&chessComponents[x+i][y+i].chessColor==chessColor){
                list1.add(new ChessboardPoint(x+i,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x+i,y+i));
        }
        for (int i=1;;i++){
            if (x-i<0||y+i>=8){
                break;
            }
            if (!(chessComponents[x-i][y+i] instanceof EmptySlotComponent
                    ||chessComponents[x-i][y+i] instanceof KingChessComponent)){
                    list1.add(new ChessboardPoint(x-i,y+i));
                break;
            }
            if (chessComponents[x-i][y+i] instanceof KingChessComponent&&chessComponents[x-i][y+i].chessColor==chessColor){
                list1.add(new ChessboardPoint(x-i,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x-i,y+i));
        }
        for (int i=-1;;i--){
            if (x-i>=8||y+i<0){
                break;
            }
            if (!(chessComponents[x-i][y+i] instanceof EmptySlotComponent
                    ||chessComponents[x-i][y+i] instanceof KingChessComponent)){
                    list1.add(new ChessboardPoint(x-i,y+i));
                break;
            }
            if (chessComponents[x-i][y+i] instanceof KingChessComponent&&chessComponents[x-i][y+i].chessColor==chessColor){
                list1.add(new ChessboardPoint(x-i,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x-i,y+i));
        }
        return list1;
    }

    @Override
    public List<ChessboardPoint> getList() {
        return list;
    }

    @Override
    public List<ChessboardPoint> getList1() {
        return list1;
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
        g.drawImage(BishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "S";
        }
        else return "s";
    }
}
