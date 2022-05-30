package view;

import javafx.animation.TranslateTransition;
import model.*;
import controller.ClickController;

import javax.swing.*;
import javax.xml.soap.Node;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chessboard extends JComponent{
    private int x;
    private int y;
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor=ChessColor.NONE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private final List<ChessComponent> arrayList=new ArrayList<>(64);
    private boolean checkMating=false;
    private List<ChessComponent> checkmateChess= new ArrayList<>();
    private ChessboardPoint kingLocation;
    public List<ChessComponent> getArrayList() {
        return arrayList;
    }

    public int getCHESS_SIZE() {
        return CHESS_SIZE;
    }

    public ClickController getClickController() {
        return clickController;
    }

    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }
    public ChessComponent getChess(int x,int y){
        return chessComponents[x][y];
    }

    public void setChess(int x,int y,ChessComponent chessComponent) {
        chessComponents[x][y]=chessComponent;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public List<ChessComponent> getCheckmateChess() {
        return checkmateChess;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            arrayList.remove(chessComponents[row][col]);
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
        arrayList.add(chessComponents[row][col]);
        chessComponent.defeatRange(chessComponents,new ArrayList<>());
        chessComponent.getCanMoveTo(chessComponents,new ArrayList<>(),this);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        checkmateChess.subList(0,checkmateChess.size()).clear();
        checkMating=false;
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            arrayList.remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE,this));
            arrayList.add(chess2);
        }
        chess1.swapLocation(chess2);
        arrayList.remove(chess1);
        arrayList.remove(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        arrayList.add(chessComponents[row1][col1]);
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        arrayList.add(chessComponents[row2][col2]);
        chess1.repaint();
        chess2.repaint();
        //下面是兵升变
        if (Objects.equals(chess1.toString(), "p")&&chess1.getChessColor()== ChessColor.BLACK&&chess1.getChessboardPoint().getX()==7){
            Object[] o={"Bishop","Queen","Rook","Knight"};
            int optional=JOptionPane.showOptionDialog(null,"Promote to:","Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Queen");
            remove(chess1);
            switch (optional){
                case 0:
                    initBishopOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 1:
                    initQueenOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 2:
                    initRookOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 3:
                    initKnightOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
            }
        }
        if (Objects.equals(chess1.toString(), "P")&&chess1.getChessColor()== ChessColor.WHITE&&chess1.getChessboardPoint().getX()==0){
            Object[] o={"Bishop","Queen","Rook","Knight"};
            int optional=JOptionPane.showOptionDialog(null,"Promote to:","Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,o,"Queen");
            remove(chess1);
            switch (optional){
                case 0:
                    initBishopOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 1:
                    initQueenOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 2:
                    initRookOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
                case 3:
                    initKnightOnBoard(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY(),chess1.getChessColor());
                    break;
            }
        }
        repaint();
        Check();
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE,this));
            }
        }
    }
    public void initiateEmptyChessboard(int row,int col) {
        ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(row, col), calculatePoint(row, col), clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initiateTheNormalGame(){
        initiateEmptyChessboard();
        initRookOnBoard(0,0, ChessColor.BLACK);
        initRookOnBoard(0,7,ChessColor.BLACK);
        initRookOnBoard(7,0,ChessColor.WHITE);
        initRookOnBoard(7,7,ChessColor.WHITE);
        initKingOnBoard(0,4,ChessColor.BLACK);
        initKingOnBoard(7,4,ChessColor.WHITE);
        initQueenOnBoard(0,3,ChessColor.BLACK);
        initQueenOnBoard(7,3,ChessColor.WHITE);
        for (int i = 0; i < 8; i++) {
            initPawnOnBoard(1,i,ChessColor.BLACK);
            initPawnOnBoard(6,i,ChessColor.WHITE);
        }
        initKnightOnBoard(0,1,ChessColor.BLACK);
        initKnightOnBoard(0,6,ChessColor.BLACK);
        initKnightOnBoard(7,1,ChessColor.WHITE);
        initKnightOnBoard(7,6,ChessColor.WHITE);
        initBishopOnBoard(0,2,ChessColor.BLACK);
        initBishopOnBoard(0,5,ChessColor.BLACK);
        initBishopOnBoard(7,2,ChessColor.WHITE);
        initBishopOnBoard(7,5,ChessColor.WHITE);
        checkMating=false;
        checkmateChess.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initKingOnBoard(int row, int col, ChessColor color){
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE,this);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(File file) throws IOException {
        String fileName = file.getName();
        List<String> chessData = Files.readAllLines(Paths.get(fileName));
        chessData.forEach(System.out::println);
        initiateEmptyChessboard();
        if (chessData.get(0).equals("B")){
            currentColor = ChessColor.BLACK;
        }
        else if (chessData.get(0).equals("W")){
            currentColor = ChessColor.WHITE;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessData.get(i+1).charAt(j)=='K'){ initKingOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='k'){ initKingOnBoard(i,j,ChessColor.BLACK);}
                if (chessData.get(i+1).charAt(j)=='R'){ initRookOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='r'){ initRookOnBoard(i,j,ChessColor.BLACK);}
                if (chessData.get(i+1).charAt(j)=='P'){ initPawnOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='p'){ initPawnOnBoard(i,j,ChessColor.BLACK);}
                if (chessData.get(i+1).charAt(j)=='N'){ initKnightOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='n'){ initKnightOnBoard(i,j,ChessColor.BLACK);}
                if (chessData.get(i+1).charAt(j)=='Q'){ initQueenOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='q'){ initQueenOnBoard(i,j,ChessColor.BLACK);}
                if (chessData.get(i+1).charAt(j)=='S'){ initBishopOnBoard(i,j,ChessColor.WHITE);}
                if (chessData.get(i+1).charAt(j)=='s'){ initBishopOnBoard(i,j,ChessColor.BLACK);}
            }
        }
        repaint();
    }
    public boolean getCheckMating(){
        return checkMating;
    }

    public ChessboardPoint getKingLocation() {
        return kingLocation;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void Check(){
        int index1=0;
        int index2=0;
        boolean a=true;
        for (int i=0;i<arrayList.size();i++){
            arrayList.get(i).getList1().subList(0,arrayList.get(i).getList1().size()).clear();
            arrayList.get(i).defeatRange(chessComponents,new ArrayList<>());//更新每个棋子的将军范围
            if (arrayList.get(i) instanceof KingChessComponent){
                if (!a){
                    index2=i;
                }
                if (a){
                    index1=i;
                    a=false;
                }
            }
        }
        for (int i=0;i<arrayList.size();i++){
            if (currentColor==arrayList.get(i).getChessColor()){
                arrayList.get(i).getList().subList(0,arrayList.get(i).getList().size()).clear();
                arrayList.get(i).getCanMoveTo(chessComponents,arrayList,this);//更新该棋子的可移动范围
            }
        }
        for (int i=0;i<arrayList.size();i++){
            if (currentColor!=arrayList.get(i).getChessColor()){
                arrayList.get(i).getList().subList(0,arrayList.get(i).getList().size()).clear();
                arrayList.get(i).getCanMoveTo(chessComponents,arrayList,this);//更新该棋子的可移动范围
            }
        }
        for (int i=0;i<arrayList.size();i++) {
            if (arrayList.get(i).getChessColor() != arrayList.get(index1).getChessColor() && !(arrayList.get(i) instanceof EmptySlotComponent)) {
                for (int j = 0; j < arrayList.get(i).getList1().size(); j++) {
                    if (arrayList.get(i).getList1().get(j).getX() == arrayList.get(index1).getChessboardPoint().getX()
                            && arrayList.get(i).getList1().get(j).getY() == arrayList.get(index1).getChessboardPoint().getY()) {
                        checkMating = true;
                        checkmateChess.add(arrayList.get(i));
                        kingLocation=arrayList.get(index1).getChessboardPoint();
                    }
                }
            }
        }
        for (int i=0;i<arrayList.size();i++){
            if (arrayList.get(i).getChessColor()!=arrayList.get(index2).getChessColor()&&!(arrayList.get(i) instanceof EmptySlotComponent)){
                for (int j=0;j<arrayList.get(i).getList1().size();j++){
                    if (arrayList.get(i).getList1().get(j).getX()==arrayList.get(index2).getChessboardPoint().getX()
                            &&arrayList.get(i).getList1().get(j).getY()==arrayList.get(index2).getChessboardPoint().getY()){
                        checkMating=true;
                        checkmateChess.add(arrayList.get(i));
                        kingLocation=arrayList.get(index2).getChessboardPoint();
                    }
                }
            }
        }
        if (checkMating){
            for (int i=0;i<arrayList.size();i++){
                arrayList.get(i).getSpecialList().clear();
                arrayList.get(i).specialGetCanMoveTo(chessComponents,arrayList,this);
            }
        }
        if (arrayList.get(index1).getChessColor()!=currentColor&&arrayList.get(index1).getList().size()==0&&checkMating){
            for (int i=0;i<arrayList.size();i++){
                if (i==index1){continue;}
                if (arrayList.get(i).getChessColor()==arrayList.get(index1).getChessColor()&&arrayList.get(i).getSpecialList().size()!=0){
                    return;
                }
            }
            String s="The "+ currentColor+" wins!!!";
            JOptionPane.showMessageDialog(null, s);
            return;
        }
        if (arrayList.get(index2).getChessColor()!=currentColor&&arrayList.get(index2).getList().size()==0&&checkMating){
            for (int i=0;i<arrayList.size();i++){
                if (i==index2){continue;}
                if (arrayList.get(i).getChessColor()==arrayList.get(index2).getChessColor()&&arrayList.get(i).getSpecialList().size()!=0){
                    return;
                }
            }
            String s="The "+ currentColor+" wins!!!";
            JOptionPane.showMessageDialog(null, s);
            return;
        }
        for (int i=0;i<arrayList.size();i++){
            if (arrayList.get(i).getChessColor()!=currentColor&&arrayList.get(i).getList().size()!=0){
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "draw");
    }
}