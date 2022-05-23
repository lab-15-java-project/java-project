package controller;

import model.*;
import view.ChessGameFrame;
import view.Chessboard;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class ClickController extends JFrame{
    private  Chessboard chessboard;
    private ChessComponent first;
    ChessGameFrame chessGameFrame;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    public ClickController(ChessGameFrame chessGameFrame){
        this.chessGameFrame=chessGameFrame;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
                first.getCanMoveTo(chessboard.getChessComponents(), chessboard.getArrayList(),chessboard);
                repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                first.getList().subList(0,first.getList().size()).clear();
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, chessComponent);
                if (Objects.equals(first.toString(), "p")&&first.getChessColor()== ChessColor.BLACK&&first.getChessboardPoint().getX()==7){
                    Object[] o={"Bishop","Queen","Rook","Knight"};
                    int optional=JOptionPane.showOptionDialog(null,"Promote to:","Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Queen");
                    chessboard.remove(first);
                    switch (optional){
                        case 0:
                            chessboard.initBishopOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 1:
                            chessboard.initQueenOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 2:
                            chessboard.initRookOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 3:
                            chessboard.initKnightOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                    }
                }
                if (Objects.equals(first.toString(), "P")&&first.getChessColor()== ChessColor.WHITE&&first.getChessboardPoint().getX()==0){
                    Object[] o={"Bishop","Queen","Rook","Knight"};
                    int optional=JOptionPane.showOptionDialog(null,"Promote to:","Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Queen");
                    chessboard.remove(first);
                    switch (optional){
                        case 0:
                            chessboard.initBishopOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 1:
                            chessboard.initQueenOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 2:
                            chessboard.initRookOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                        case 3:
                            chessboard.initKnightOnBoard(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first.getChessColor());
                            break;
                    }
                }
                chessboard.Check();
                chessboard.repaint();
                chessboard.swapColor();
                chessGameFrame.changeCurrentPlayer("Current action player : "+chessboard.getCurrentColor());
                if (first instanceof PawnChessComponent&&((PawnChessComponent) first).getMove()==1){
                    if (first.getChessboardPoint().getY()>0&&first.getChessboardPoint().getY()<7){
                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent
                                &&!(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent)
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1).getChessColor()!=first.getChessColor()){
                            String[] o={"Yes","No"};
                            int a=JOptionPane.showOptionDialog(null,"En passant？","En passant",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Yes");
                            if (a==0){
                                remove(first);
                                chessboard.getArrayList().remove(first);
                                add(first=new EmptySlotComponent(first.getChessboardPoint(),first.getLocation(),chessboard.getClickController(),chessboard.getCHESS_SIZE()));
                                chessboard.getArrayList().add(first);
                                chessboard.setChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY(),first);
                                chessboard.swapColor();
                                chessGameFrame.changeCurrentPlayer("Current action player : "+chessboard.getCurrentColor());
                                chessboard.repaint();
                            }
                        }

                        if (chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1)instanceof PawnChessComponent
                                &&!(chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()-1)instanceof PawnChessComponent)
                                &&chessboard.getChess(first.getChessboardPoint().getX(),first.getChessboardPoint().getY()+1).getChessColor()!=first.getChessColor()){
                            JOptionPane.showConfirmDialog(null,"En passant？","En passant",JOptionPane.YES_NO_OPTION);
                        }
                    }
                }
                first.setSelected(false);
                System.out.println(first.getChessColor());
                repaint();
                first = null;
                repaint();
                String str = "Current action player : "+chessboard.getCurrentColor();
                if (chessboard.getCheckMating()){
                    JOptionPane.showMessageDialog(null,"checkmate!","situation",JOptionPane.WARNING_MESSAGE );
                }
            }
        }
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
}
