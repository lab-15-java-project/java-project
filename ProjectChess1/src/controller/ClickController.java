package controller;

import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Objects;


public class ClickController extends JFrame {
    private  Chessboard chessboard;
    private ChessComponent first;
    ChessGameFrame chessGameFrame;
    private boolean q=false;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    public ClickController(ChessGameFrame chessGameFrame){
        this.chessGameFrame=chessGameFrame;
    }


    public void onClick(ChessComponent chessComponent) {
        q=false;
        ChessComponent[][] array=chessboard.getChessComponents();
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
                first.getList().clear();
                first.getCanMoveTo(chessboard.getChessComponents(), chessboard.getArrayList(),chessboard);
                if (!chessboard.getCheckMating()||first instanceof KingChessComponent){
                    for (int i=0;i<first.getList().size();i++){
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].setSomeoneSelected(true);
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].repaint();
                    }
                }
                if (chessboard.getCheckMating()){
                    for (int i=0;i<first.getSpecialList().size();i++){
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].setSomeoneSelected(true);
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].repaint();
                    }
                }
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                first.getList().clear();
                first.getCanMoveTo(chessboard.getChessComponents(), chessboard.getArrayList(),chessboard);
                if (!chessboard.getCheckMating()||first instanceof KingChessComponent){
                    for (int i=0;i<chessComponent.getList().size();i++){
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].setSomeoneSelected(false);
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].repaint();
                    }
                }
                if (chessboard.getCheckMating()){
                    for (int i=0;i<first.getSpecialList().size();i++){
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].setSomeoneSelected(false);
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].repaint();
                    }
                }
                ChessComponent recordFirst = first;
                first.getList().clear();
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                first.getCanMoveTo(chessboard.getChessComponents(),chessboard.getArrayList(),chessboard);
                if (!chessboard.getCheckMating()||first instanceof KingChessComponent){
                    for (int i=0;i<first.getList().size();i++){
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].setSomeoneSelected(false);
                        array[first.getList().get(i).getX()][first.getList().get(i).getY()].repaint();
                    }
                }
                if (chessboard.getCheckMating()){
                    first.specialGetCanMoveTo(chessboard.getChessComponents(),chessboard.getArrayList(),chessboard);
                    for (int i=0;i<first.getSpecialList().size();i++){
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].setSomeoneSelected(false);
                        array[first.getSpecialList().get(i).getX()][first.getSpecialList().get(i).getY()].repaint();
                    }
                }
                first.getSpecialList().clear();
                chessComponent.setSomeoneSelected(false);
                chessComponent.repaint();
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();

                //吃过路兵
                if (first instanceof PawnChessComponent&&((PawnChessComponent) first).getMove()==1&&(first.getChessboardPoint().getX()==3||first.getChessboardPoint().getX()==4)){
                    if (first.getChessboardPoint().getY()>0&&first.getChessboardPoint().getY()<7){
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                                &&!(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent)
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()!=first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            left(a);
                        }
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                                &&!(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent)
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()!=first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            right(a);
                        }
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()!=first.getChessColor()
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()==first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            right(a);
                        }
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()==first.getChessColor()
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()!=first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            left(a);
                        }
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()!=first.getChessColor()
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()!=first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            if (a==0){
                                String[] o1={"Left","Right"};
                                int a1=JOptionPane.showOptionDialog(null,"Which one?","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o1,"left");
                                if (a1==1){
                                    right(a);
                                }
                                else {
                                    left(a);
                                }
                            }
                        }
                    }
                    if (first.getChessboardPoint().getY()==0
                            &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                            &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()!=first.getChessColor()){
                        String[] o={"Yes","No"};
                        int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                        right(a);

                    }
                    if (first.getChessboardPoint().getY()==7
                            &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                            &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()!=first.getChessColor()){
                        String[] o={"Yes","No"};
                        int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                        left(a);
                    }
                    if (q){
                        chessboard.swapColor();
                    }
                }
                first.setSelected(false);
                repaint();
                first = null;
                repaint();
                if (chessboard.getCheckMating()){
                    JOptionPane.showMessageDialog(null,"Check!","situation",JOptionPane.WARNING_MESSAGE );
                }
            }
        }
        chessboard.repaint();
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint(),chessboard.getArrayList(),chessboard);
    }
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(Color.RED);
        g.fillOval(0, 0, this.getWidth(), this.getHeight());
    }
    public void left(int a){
        if (a==0&&chessboard.getCurrentColor()==ChessColor.BLACK){
            chessboard.remove(first);
            chessboard.getArrayList().remove(first);
            chessboard.add(first=new EmptySlotComponent(first.getChessboardPoint(),first.getLocation(),chessboard.getClickController(),chessboard.getCHESS_SIZE(),chessboard));
            chessboard.getArrayList().add(first);
            chessboard.setChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first);
            chessboard.swapChessComponents(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1),chessboard.getChess(first.getChessboardPoint().getX()+1,first.getChessboardPoint().getY()));
            chessboard.repaint();
            first.repaint();
            q=true;
        }
        if (a==0&&chessboard.getCurrentColor()==ChessColor.WHITE){
            chessboard.remove(first);
            chessboard.getArrayList().remove(first);
            chessboard.add(first=new EmptySlotComponent(first.getChessboardPoint(),first.getLocation(),chessboard.getClickController(),chessboard.getCHESS_SIZE(),chessboard));
            chessboard.getArrayList().add(first);
            chessboard.setChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first);
            chessboard.swapChessComponents(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1),chessboard.getChess(first.getChessboardPoint().getX()-1,first.getChessboardPoint().getY()));
            chessboard.repaint();
            first.repaint();
            q=true;
        }
    }
    public void right(int a){
        if (a==0&&chessboard.getCurrentColor()==ChessColor.BLACK){
            chessboard.remove(first);
            chessboard.getArrayList().remove(first);
            chessboard.add(first=new EmptySlotComponent(first.getChessboardPoint(),first.getLocation(),chessboard.getClickController(),chessboard.getCHESS_SIZE(),chessboard));
            chessboard.getArrayList().add(first);
            chessboard.setChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first);
            chessboard.swapChessComponents(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1),chessboard.getChess(first.getChessboardPoint().getX()+1,first.getChessboardPoint().getY()));
            chessboard.repaint();
            first.repaint();
            q=true;
        }
        if (a==0&&chessboard.getCurrentColor()==ChessColor.WHITE){
            chessboard.remove(first);
            chessboard.getArrayList().remove(first);
            chessboard.add(first=new EmptySlotComponent(first.getChessboardPoint(),first.getLocation(),chessboard.getClickController(),chessboard.getCHESS_SIZE(),chessboard));
            chessboard.getArrayList().add(first);
            chessboard.setChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first);
            chessboard.swapChessComponents(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1),chessboard.getChess(first.getChessboardPoint().getX()-1,first.getChessboardPoint().getY()));
            chessboard.repaint();
            first.repaint();
            q=true;
        }
    }
}
