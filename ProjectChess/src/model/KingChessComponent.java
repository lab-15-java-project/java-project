package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class KingChessComponent extends ChessComponent {
    private static Image King_WHITE;
    private static Image King_BLACK;

    private Image KingImage;



    //读取加载车棋子的图片
    public void loadResource() throws IOException {
        if (King_WHITE == null) {
            King_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }

        if (King_BLACK == null) {
            King_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                KingImage = King_WHITE;
            } else if (color == ChessColor.BLACK) {
                KingImage = King_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, ChessColor color, ClickController listener, int size, int size1, Chessboard chessboard) {
        super(chessboardPoint, color, listener, size, size1, chessboard);
        initiateKingImage(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination, List<ChessComponent> arrayList, Chessboard chessboard) {
        list.subList(0,list.size()).clear();
        getCanMoveTo(chessComponents,arrayList,chessboard);
        for (int i=0;i<list.size();i++){
            if (list.get(i).getX()==destination.getX()&&list.get(i).getY()==destination.getY()){
                return true;
            }
        }
        return false;
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
        g.drawImage(KingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "K";
        }
        else return "k";
    }

    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0,list1.size()).clear();
        defeatRange(chessComponents,arrayList);
        ChessboardPoint source=getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (x<7){
            if ( chessColor != chessComponents[x + 1][y].chessColor&&checkMate(x+1,y,arrayList)){
                list.add(new ChessboardPoint(x+1,y));
            }
        }
        if (x>0){
            if ( chessColor!= chessComponents[x -1][y].chessColor&&checkMate(x-1,y,arrayList)){
                list.add(new ChessboardPoint(x-1,y));
            }
        }
        if (y+1<8){
            if ( chessColor != chessComponents[x][y+1].chessColor&&checkMate(x,y+1,arrayList)){
                list.add(new ChessboardPoint(x,y+1));
            }
        }
        if (y>0){
            if ( chessColor!= chessComponents[x ][y-1].chessColor&&checkMate(x,y-1,arrayList)){
                list.add(new ChessboardPoint(x,y-1));
            }
        }
        if (x+1<8&&y+1<8){
            if ( chessColor != chessComponents[x + 1][y+1].chessColor&&checkMate(x+1,y+1,arrayList)){
                list.add(new ChessboardPoint(x+1,y+1));
            }
        }
        if (x>0&&y>0){
            if ( chessColor!= chessComponents[x -1][y-1].chessColor&&checkMate(x-1,y-1,arrayList)){
                list.add(new ChessboardPoint(x-1,y-1));
            }
        }
        if (x+1<8&&y>0){
            if ( chessColor != chessComponents[x + 1][y-1].chessColor&&checkMate(x+1,y-1,arrayList)){
                list.add(new ChessboardPoint(x+1,y-1));
            }
        }
        if (x>0&&y<7){
            if ( chessColor!= chessComponents[x -1][y+1].chessColor&&checkMate(x-1,y+1,arrayList)){
                list.add(new ChessboardPoint(x-1,y+1));
            }
        }
        return list;
    }

    @Override
    public List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList) {
        ChessboardPoint source=getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
        if (x<7){
            list1.add(new ChessboardPoint(x+1,y));
        }
        if (x>0){
            list1.add(new ChessboardPoint(x-1,y));
        }
        if (y+1<8){
            list1.add(new ChessboardPoint(x,y+1));
        }
        if (y>0){
            list1.add(new ChessboardPoint(x,y-1));
        }
        if (x+1<8&&y+1<8){
            list1.add(new ChessboardPoint(x+1,y+1));
        }
        if (x>0&&y>0){
            list1.add(new ChessboardPoint(x-1,y-1));
        }
        if (x+1<8&&y>0){
            list1.add(new ChessboardPoint(x+1,y-1));
        }
        if (x>0&&y<7){
            list1.add(new ChessboardPoint(x-1,y+1));
        }
        return list1;
    }

    @Override
    public List<ChessboardPoint> getList1() {
        return list1;
    }

    @Override
    public void specialGetCanMoveTo(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {

    }

    @Override
    public void willCheckmate(ChessComponent[][] chessComponents, List<ChessComponent> arrayList, Chessboard chessboard) {

    }

    @Override
    public List<ChessboardPoint> getList() {
        return list;
    }

    public boolean checkMate(int x,int y,List<ChessComponent> arrayList){
        for (int i=0;i<arrayList.size();i++){
            if (arrayList.get(i).chessColor!=chessColor&&!(arrayList.get(i) instanceof EmptySlotComponent)){
                for (int j=0;j<arrayList.get(i).getList1().size();j++){
                    if (x==arrayList.get(i).getList1().get(j).getX()&&y==arrayList.get(i).getList1().get(j).getY()){
                        System.out.println(arrayList.get(i).toString());
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
