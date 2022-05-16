package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是一个抽象类，主要表示8*8棋盘上每个格子的棋子情况，当前有两个子类继承它，分别是EmptySlotComponent(空棋子)和RookChessComponent(车)。
 */
public abstract class ChessComponent extends JComponent {

    /**
     * CHESSGRID_SIZE: 主要用于确定每个棋子在页面中显示的大小。
     * <br>
     * 在这个设计中，每个棋子的图案是用图片画出来的（由于国际象棋那个棋子手动画太难了）
     * <br>
     * 因此每个棋子占用的形状是一个正方形，大小是50*50
     */

   private static final Dimension CHESSGRID_SIZE = new Dimension(1080 / 4 * 3 / 8, 1080 / 4 * 3 / 8);
    private static final Color[] BACKGROUND_COLORS = {Color.WHITE, Color.BLACK};
    /**
     * handle click event
     */
    private ClickController clickController;
    protected List<ChessboardPoint> list=new ArrayList<>();
    protected List<ChessboardPoint> list1=new ArrayList<>();
    protected List<ChessboardPoint> specialList=new ArrayList<>();

    /**
     * chessboardPoint: 表示8*8棋盘中，当前棋子在棋格对应的位置，如(0, 0), (1, 0), (0, 7),(7, 7)等等
     * <br>
     * chessColor: 表示这个棋子的颜色，有白色，黑色，无色三种
     * <br>
     * selected: 表示这个棋子是否被选中
     */
    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    private boolean selected;

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
        this.clickController = clickController;
    }
    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
     */
    public void swapLocation(ChessComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
            clickController.onClick(this);
        }
    }

    /**
     * @param chessboard  棋盘
     * @param destination 目标位置，如(0, 0), (0, 7)等等
     * @param arrayList
     * @return this棋子对象的移动规则和当前位置(chessboardPoint)能否到达目标位置
     * <br>
     * 这个方法主要是检查移动的合法性，如果合法就返回true，反之是false
     */
    public  boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination, List<ChessComponent> arrayList, Chessboard chessboard){
        list.subList(0,list.size()).clear();
        getCanMoveTo(chessComponents,arrayList,chessboard);
        if (chessboard.getCheckMating()){
            specialGetCanMoveTo(chessComponents,arrayList,chessboard);
            for (int i=0;i<specialList.size();i++){
                if (specialList.get(i).getX()==destination.getX()&&specialList.get(i).getY()==destination.getY()){
                    specialList.subList(0, specialList.size()).clear();
                    list.subList(0,list.size()).clear();
                    return true;
                }
            }
            specialList.subList(0, specialList.size()).clear();
            list.subList(0,list.size()).clear();
            return false;
        }
        for (int i=0;i<list.size();i++){
            if (list.get(i).getX()==destination.getX()&&list.get(i).getY()==destination.getY()){
                list.subList(0, list.size()).clear();
                return true;
            }
        }
        list.subList(0, list.size()).clear();
        return false;
    }

    /**
     * 这个方法主要用于加载一些特定资源，如棋子图片等等。
     *
     * @throws IOException 如果一些资源找不到(如棋子图片路径错误)，就会抛出异常
     */
    public abstract void loadResource() throws IOException;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        System.out.printf("repaint chess [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
        Color squareColor = BACKGROUND_COLORS[(chessboardPoint.getX() + chessboardPoint.getY()) % 2];
        g.setColor(squareColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    @Override
    public abstract String toString();
    public abstract List<ChessboardPoint> getCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard);
    public abstract List<ChessboardPoint> defeatRange(ChessComponent[][] chessComponents, List<ChessComponent> arrayList);

    public abstract List<ChessboardPoint> getList();
    public abstract List<ChessboardPoint> getList1();
    /*
在将军时使用的移动规则
 */
    public  void specialGetCanMoveTo(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard){
        if (chessboard.getCheckMating()) {
            if (chessboard.getCheckmateChess().size()==1) {
                int x = chessboard.getCheckmateChess().get(0).getChessboardPoint().getX();
                int y = chessboard.getCheckmateChess().get(0).getChessboardPoint().getY();
                for (ChessboardPoint chessboardPoint : list) {
                    if (x == chessboardPoint.getX() && y == chessboardPoint.getY()) {
                        specialList.add(new ChessboardPoint(x, y));
                    }
                }
                if (chessboard.getCheckmateChess().get(0) instanceof RookChessComponent
                        ||chessboard.getCheckmateChess().get(0) instanceof QueenChessComponent
                        ||chessboard.getCheckmateChess().get(0) instanceof BishopChessComponent){
                    if (chessboard.getCheckmateChess().get(0) instanceof RookChessComponent){
                        if (y==chessboard.getKingLocation().getY()){
                            for (int i=1;i<Math.abs(chessboard.getKingLocation().getX()-x);i++){
                                for (int j=0;j<list.size();j++){
                                    if (Math.min(x,chessboard.getKingLocation().getX())+i==list.get(j).getX()
                                            &&y==list.get(j).getY()){
                                        specialList.add(new ChessboardPoint(Math.min(x,chessboard.getKingLocation().getX())+i, y));
                                        break;
                                    }
                                }
                            }
                        }
                        if (x==chessboard.getKingLocation().getX()){
                            for (int i=1;i<Math.abs(chessboard.getKingLocation().getY()-y);i++){
                                for (int j=0;j<list.size();j++){
                                    if (Math.min(y,chessboard.getKingLocation().getY())+i==list.get(j).getY()
                                            &&x==list.get(j).getX()){
                                        specialList.add(new ChessboardPoint(x, Math.min(y,chessboard.getKingLocation().getY())+i));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (chessboard.getCheckmateChess().get(0) instanceof BishopChessComponent){
                        if (chessboard.getKingLocation().getX()-x==chessboard.getKingLocation().getY()-y) {
                            for (int i = 1; i < Math.abs(chessboard.getKingLocation().getX() - x); i++) {
                                for (int j = 0; j < list.size(); j++) {
                                    if (Math.min(x, chessboard.getKingLocation().getX()) + i == list.get(j).getX()
                                            && Math.min(y, chessboard.getKingLocation().getY()) + i == list.get(j).getY()) {
                                        specialList.add(new ChessboardPoint(Math.min(x, chessboard.getKingLocation().getX()) + i, Math.min(y, chessboard.getKingLocation().getY()) + i));
                                        break;
                                    }
                                }
                            }
                        }
                        if (chessboard.getKingLocation().getX()-x==-chessboard.getKingLocation().getY()-y){
                            for (int i=1;i<Math.abs(chessboard.getKingLocation().getY()-y);i++){
                                for (int j=0;j<list.size();j++){
                                    if (Math.min(y,chessboard.getKingLocation().getY())+i==list.get(j).getY()
                                            &&Math.max(x,chessboard.getKingLocation().getX())-i==list.get(j).getX()){
                                        specialList.add(new ChessboardPoint(Math.max(x,chessboard.getKingLocation().getX())-i, Math.min(y,chessboard.getKingLocation().getY())+i));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (chessboard.getCheckmateChess().get(0) instanceof QueenChessComponent){
                    if (y==chessboard.getKingLocation().getY()){
                        for (int i=1;i<Math.abs(chessboard.getKingLocation().getX()-x);i++){
                            for (int j=0;j<list.size();j++){
                                if (Math.min(x,chessboard.getKingLocation().getX())+i==list.get(j).getX()
                                        &&y==list.get(j).getY()){
                                    specialList.add(new ChessboardPoint(Math.min(x,chessboard.getKingLocation().getX())+i, y));
                                    break;
                                }
                            }
                        }
                    }
                    if (x==chessboard.getKingLocation().getX()){
                        for (int i=1;i<Math.abs(chessboard.getKingLocation().getY()-y);i++){
                            for (int j=0;j<list.size();j++){
                                if (Math.min(y,chessboard.getKingLocation().getY())+i==list.get(j).getY()
                                        &&x==list.get(j).getX()){
                                    specialList.add(new ChessboardPoint(x, Math.min(y,chessboard.getKingLocation().getY())+i));
                                    break;
                                }
                            }
                        }
                    }
                    if (chessboard.getKingLocation().getX()-x==chessboard.getKingLocation().getY()-y) {
                        for (int i = 1; i < Math.abs(chessboard.getKingLocation().getX() - x); i++) {
                            for (int j = 0; j < list.size(); j++) {
                                if (Math.min(x, chessboard.getKingLocation().getX()) + i == list.get(j).getX()
                                        && Math.min(y, chessboard.getKingLocation().getY()) + i == list.get(j).getY()) {
                                    specialList.add(new ChessboardPoint(Math.min(x, chessboard.getKingLocation().getX()) + i, Math.min(y, chessboard.getKingLocation().getY()) + i));
                                    break;
                                }
                            }
                        }
                    }
                    if (chessboard.getKingLocation().getX()-x==-chessboard.getKingLocation().getY()-y){
                        for (int i=1;i<Math.abs(chessboard.getKingLocation().getY()-y);i++){
                            for (int j=0;j<list.size();j++){
                                if (Math.min(y,chessboard.getKingLocation().getY())+i==list.get(j).getY()
                                        &&Math.max(x,chessboard.getKingLocation().getX())-i==list.get(j).getX()){
                                    specialList.add(new ChessboardPoint(Math.max(x,chessboard.getKingLocation().getX())-i, Math.min(y,chessboard.getKingLocation().getY())+i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public  void willCheckmate(ChessComponent[][] chessComponents,List<ChessComponent> arrayList, Chessboard chessboard){
        int index=0;
        for (int i=0;i<arrayList.size();i++){
            if (arrayList.get(i) instanceof KingChessComponent&&arrayList.get(i).chessColor==chessColor){
                index=i;
                break;
            }
        }
        int x=getChessboardPoint().getX();
        int y=getChessboardPoint().getY();
        for (int i=0;i<arrayList.size();i++){
            if (chessColor!=arrayList.get(i).chessColor&&arrayList.get(i)instanceof BishopChessComponent
                    &&arrayList.get(i)instanceof RookChessComponent&&arrayList.get(i)instanceof QueenChessComponent){
                if (arrayList.get(i)instanceof RookChessComponent&&x==arrayList.get(i).getChessboardPoint().getX()
                        &&arrayList.get(index).getChessboardPoint().getX()==x){
                    int k=0;
                    while (k<list.size()){
                        if (list.get(k).getX()!=x){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof RookChessComponent&&y==arrayList.get(i).getChessboardPoint().getY()
                        &&arrayList.get(index).getChessboardPoint().getY()==y){
                    int k=0;
                    while (k<list.size()){
                        if (list.get(k).getY()!=y){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof BishopChessComponent
                        &&x-arrayList.get(i).getChessboardPoint().getX()==y-arrayList.get(i).getChessboardPoint().getY()
                        &&x-arrayList.get(index).getChessboardPoint().getX()==y-arrayList.get(index).getChessboardPoint().getY()){
                    int k=0;
                    while (k<list.size()){
                        if (x-list.get(k).getX()!=y-list.get(k).getY()){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof BishopChessComponent
                        &&x-arrayList.get(i).getChessboardPoint().getX()==arrayList.get(i).getChessboardPoint().getY()-y
                        &&x-arrayList.get(index).getChessboardPoint().getX()==arrayList.get(index).getChessboardPoint().getY()-y){
                    int k=0;
                    while (k<list.size()){
                        if (x-list.get(k).getX()!=list.get(k).getY()-y){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof QueenChessComponent&&x==arrayList.get(i).getChessboardPoint().getX()
                        &&arrayList.get(index).getChessboardPoint().getX()==x){
                    int k=0;
                    while (k<list.size()){
                        if (list.get(k).getX()!=x){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof QueenChessComponent&&y==arrayList.get(i).getChessboardPoint().getY()
                        &&arrayList.get(index).getChessboardPoint().getY()==y){
                    int k=0;
                    while (k<list.size()){
                        if (list.get(k).getY()!=y){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof QueenChessComponent
                        &&x-arrayList.get(i).getChessboardPoint().getX()==y-arrayList.get(i).getChessboardPoint().getY()
                        &&x-arrayList.get(index).getChessboardPoint().getX()==y-arrayList.get(index).getChessboardPoint().getY()){
                    int k=0;
                    while (k<list.size()){
                        if (x-list.get(k).getX()!=y-list.get(k).getY()){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
                if (arrayList.get(i)instanceof QueenChessComponent
                        &&x-arrayList.get(i).getChessboardPoint().getX()==arrayList.get(i).getChessboardPoint().getY()-y
                        &&x-arrayList.get(index).getChessboardPoint().getX()==arrayList.get(index).getChessboardPoint().getY()-y){
                    int k=0;
                    while (k<list.size()){
                        if (x-list.get(k).getX()!=list.get(k).getY()-y){
                            list.remove(k);
                        }
                        else {k++;}
                    }
                }
            }
        }
    }
}
