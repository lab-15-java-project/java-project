package controller;

import view.ChessGameFrame;
import view.Chessboard;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GameController {
    private Chessboard chessboard;
    private ChessGameFrame chessGameFrame;

    public GameController(Chessboard chessboard,ChessGameFrame chessGameFrame) {
        this.chessboard = chessboard;
        this.chessGameFrame=chessGameFrame;
    }

}
