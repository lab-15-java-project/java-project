package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class QueenChessComponent extends ChessComponent{

    private static Image Queen_White;
    private static Image Queen_BLACK;

    private Image QueenImage;

    public void loadResource() throws IOException {
        if (Queen_White == null) {
            Queen_White = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (Queen_BLACK == null) {
            Queen_BLACK = ImageIO.read(new File("./images/queen-black.png"));
        }
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                QueenImage = Queen_White;
            } else if (color == ChessColor.BLACK) {
                QueenImage = Queen_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, ChessColor color, ClickController listener, int size, int size1, Chessboard chessboard) {
        super(chessboardPoint, color, listener, size, size1, chessboard);
        initiateQueenImage(color);
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
        g.drawImage(QueenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "Q";
        }
        else return "q";
    }

    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0,list1.size()).clear();
        defeatRange(chessComponents,arrayList);
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        for (int i=1;;i++){
            if (x+i>=8||y+i>=8){
                break;
            }
            if (!(chessComponents[x+i][y+i] instanceof EmptySlotComponent)){
                if (chessColor!=chessComponents[x+i][y+i].chessColor){
                    list.add(new ChessboardPoint(x+i,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x+i,y+i));
        }
        for (int i=-1;;i--){
            if (x+i<0||y+i<0){
                break;
            }
            if (!(chessComponents[x+i][y+i] instanceof EmptySlotComponent)){
                if (chessColor.getColor()!=chessComponents[x+i][y+i].chessColor.getColor()){
                    list.add(new ChessboardPoint(x+i,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x+i,y+i));
        }
        for (int i=1;;i++){
            if (x-i<0||y+i>=8){
                break;
            }
            if (!(chessComponents[x-i][y+i] instanceof EmptySlotComponent)){
                if (chessColor.getColor()!=chessComponents[x-i][y+i].chessColor.getColor()){
                    list.add(new ChessboardPoint(x-i,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x-i,y+i));
        }
        for (int i=-1;;i--){
            if (x-i>=8||y+i<0){
                break;
            }
            if (!(chessComponents[x-i][y+i] instanceof EmptySlotComponent)){
                if (chessColor.getColor()!=chessComponents[x-i][y+i].chessColor.getColor()){
                    list.add(new ChessboardPoint(x-i,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x-i,y+i));
        }
        for (int i=1;;i++){
            if (x+i>=8){
                break;
            }
            if (!(chessComponents[x+i][y] instanceof EmptySlotComponent)){
                if (chessColor!=chessComponents[x+i][y].chessColor){
                    list.add(new ChessboardPoint(x+i,y));
                }
                break;
            }
            list.add(new ChessboardPoint(x+i,y));
        }
        for (int i=-1;;i--){
            if (x+i<0){
                break;
            }
            if (!(chessComponents[x+i][y] instanceof EmptySlotComponent)){
                if (chessColor!=chessComponents[x+i][y].chessColor){
                    list.add(new ChessboardPoint(x+i,y));
                }
                break;
            }
            list.add(new ChessboardPoint(x+i,y));
        }
        for (int i=1;;i++){
            if (y+i>=8){
                break;
            }
            if (!(chessComponents[x][y+i] instanceof EmptySlotComponent)){
                if (chessColor!=chessComponents[x][y+i].chessColor){
                    list.add(new ChessboardPoint(x,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x,y+i));
        }
        for (int i=-1;;i--){
            if (y+i<0){
                break;
            }
            if (!(chessComponents[x][y+i] instanceof EmptySlotComponent)){
                if (chessColor!=chessComponents[x][y+i].chessColor){
                    list.add(new ChessboardPoint(x,y+i));
                }
                break;
            }
            list.add(new ChessboardPoint(x,y+i));
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
            if (x+i>=8){
                break;
            }
            if (!(chessComponents[x+i][y] instanceof EmptySlotComponent
                    ||(chessComponents[x+i][y] instanceof KingChessComponent&&chessComponents[x+i][y].chessColor!=chessColor))){
                list1.add(new ChessboardPoint(x+i,y));

                break;
            }
            list1.add(new ChessboardPoint(x+i,y));
        }
        for (int i=-1;;i--){
            if (x+i<0){
                break;
            }
            if (!(chessComponents[x+i][y] instanceof EmptySlotComponent
                    ||(chessComponents[x+i][y] instanceof KingChessComponent&&chessComponents[x+i][y].chessColor!=chessColor))){
                list1.add(new ChessboardPoint(x+i,y));

                break;
            }
            list1.add(new ChessboardPoint(x+i,y));
        }
        for (int i=1;;i++){
            if (y+i>=8){
                break;
            }
            if (!(chessComponents[x][y+i] instanceof EmptySlotComponent
                    ||(chessComponents[x][y+i] instanceof KingChessComponent&&chessComponents[x][y+i].chessColor!=chessColor))){
                list1.add(new ChessboardPoint(x,y+i));

                break;
            }
            list1.add(new ChessboardPoint(x,y+i));
        }
        for (int i=-1;;i--){
            if (y+i<0){
                break;
            }
            if (!(chessComponents[x][y+i] instanceof EmptySlotComponent
                    ||(chessComponents[x][y+i] instanceof KingChessComponent&&chessComponents[x][y+i].chessColor!=chessColor))){
                list1.add(new ChessboardPoint(x,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x,y+i));
        }
        for (int i=1;;i++){
            if (x+i>=8||y+i>=8){
                break;
            }
            if (!(chessComponents[x+i][y+i] instanceof EmptySlotComponent
                    ||(chessComponents[x+i][y+i] instanceof KingChessComponent&&chessComponents[x+i][y+i].chessColor!=chessColor))){
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
                    ||(chessComponents[x+i][y+i] instanceof KingChessComponent&&chessComponents[x+i][y+i].chessColor!=chessColor))){
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
                    ||(chessComponents[x-i][y+i] instanceof KingChessComponent&&chessComponents[x-i][y+i].chessColor!=chessColor))){
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
                    ||(chessComponents[x-i][y+i] instanceof KingChessComponent&&chessComponents[x-i][y+i].chessColor!=chessColor))){
                list1.add(new ChessboardPoint(x-i,y+i));
                break;
            }
            list1.add(new ChessboardPoint(x-i,y+i));
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

