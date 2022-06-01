package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


//这个类表示国际象棋里面的车
public class RookChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image ROOK_WHITE;
    private static Image ROOK_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image rookImage;

    //读取加载车棋子的图片
    public void loadResource() throws IOException {
        if (ROOK_WHITE == null) {
            ROOK_WHITE = ImageIO.read(new File("./images/rook-white.png"));
        }

        if (ROOK_BLACK == null) {
            ROOK_BLACK = ImageIO.read(new File("./images/rook-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateRookImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                rookImage = ROOK_WHITE;
            } else if (color == ChessColor.BLACK) {
                rookImage = ROOK_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RookChessComponent(ChessboardPoint chessboardPoint, ChessColor color, ClickController listener, int size, int size2, Chessboard chessboard) {
        super(chessboardPoint, color, listener, size, size2, chessboard);
        initiateRookImage(color);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents
     * @param arrayList
     * @return 车棋子移动的合法性
     */

    @Override
    public List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard) {
        list1.subList(0,list1.size()).clear();
        defeatRange(chessComponents,arrayList);
        ChessboardPoint source = getChessboardPoint();
        int x=source.getX();
        int y=source.getY();
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
        return list1;
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
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(rookImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
    @Override
    public String toString() {
        if (this.chessColor == ChessColor.WHITE){
            return "R";
        }
        else return "r";
    }
    @Override
    public List<ChessboardPoint> getList() {
        return list;
    }

}
