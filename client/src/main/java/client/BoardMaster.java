package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class BoardMaster {

    public static void main(String[] args) {
        ChessBoard board = new ChessGame().getBoard();
        System.out.println(new BoardMaster().createBoard(board, true));
        System.out.println(new BoardMaster().createBoard(board, false));
    }

    public String createBoard(ChessBoard board, boolean white){
        StringBuilder result = new StringBuilder();
        result.append(columnAndRows(white));
        for(int i = 0; i < 8; i++){
            int row = white ? 8 - i : 1 + i;
            result.append(" ").append(row).append(" ");
            for(int j = 0; j < 8; j++) {
                    int col = white ? 1+j : 8-j;
                    result.append(squares(board, row, col));
            }
            result.append(" ").append(row).append(" ").append("\n");
        }
        result.append(columnAndRows(white));
        return result.toString();
    }

    private String squares(ChessBoard board, int row, int col){
        String color;
        if ((row + col) % 2 ==1){
            color = EscapeSequences.SET_BG_COLOR_WHITE;
        } else {
            color = EscapeSequences.SET_BG_COLOR_BLACK;
        }

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if(piece == null){
            return  color + "   " + EscapeSequences.RESET_BG_COLOR;
        }

        String pieceColor;
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        } else{
            pieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        return color + pieceColor +letterForPiece(piece)
                + EscapeSequences.RESET_TEXT_COLOR +EscapeSequences.RESET_BG_COLOR;
    }

    private String letterForPiece(ChessPiece piece){
        switch (piece.getPieceType()){
            case KING:
                return " K ";
            case QUEEN:
                return " Q ";
            case BISHOP:
                return " B ";
            case KNIGHT:
                return " N ";
            case ROOK:
                return " R ";
            default:
                return " P ";
        }
    }

    private String columnAndRows(boolean white){
        StringBuilder labels = new StringBuilder("   ");
        for(int j = 0; j < 8; j++) {
            int col = white ? 1+j : 8-j;
            char letter = (char) ('a' + col - 1);
            labels.append(" ").append(letter).append(" ");
        }
        return labels.append("\n").toString();
    }


}
