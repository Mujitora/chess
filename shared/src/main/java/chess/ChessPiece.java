package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType pieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }
    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        switch (getPieceType()) {
            case KING:
                int[][] kingMoveableDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {0, -1}, {-1, 0}};
                for (int[] direction : kingMoveableDirections) {
                    int rowMove = direction[0];
                    int colMove = direction[1];
                    int currentRow = myPosition.getRow();
                    int currentCol = myPosition.getColumn();

                    while (true) {
                        currentRow += rowMove;
                        currentCol += colMove;
                        if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                            break;
                        } else {
                            ChessPiece currentPiece = board.getPiece(new ChessPosition(currentRow, currentCol));
                            if (currentPiece == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                break;
                            } else {
                                if (currentPiece.getTeamColor().equals(this.getTeamColor())) {
                                    break;
                                } else {
                                    validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                    break;
                                }

                            }

                        }
                    }
                }
                break;
            case PAWN:
            {
                int move;
                //change forwards or backwards for the different colors
                if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    move = 1;
                } else {
                    move = -1;
                }
                int currentRow = myPosition.getRow();
                int currentCol = myPosition.getColumn();

                // ahead
                int rowInFront = currentRow + move;
                ChessPiece forwardPiece = null;
                if (rowInFront <= 8 && rowInFront >= 1) {
                    forwardPiece = board.getPiece(new ChessPosition(rowInFront, currentCol));
                    if (forwardPiece == null) {
                        boolean promotion = (rowInFront == 8 && this.getTeamColor() == ChessGame.TeamColor.WHITE) || (rowInFront == 1 && this.getTeamColor() == ChessGame.TeamColor.BLACK);
                        if (promotion) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, currentCol), PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, currentCol), PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, currentCol), PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, currentCol), PieceType.ROOK));
                        }else {

                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, currentCol), null));
                        }
                    }
                }
                // first turn
                boolean firstMove = (currentRow == 2 && this.getTeamColor() == ChessGame.TeamColor.WHITE) || (currentRow == 7 && this.getTeamColor() == ChessGame.TeamColor.BLACK);
                if(firstMove && forwardPiece == null){
                    int twoAhead = currentRow + (move * 2);
                    ChessPiece twoAheadPiece = board.getPiece(new ChessPosition(twoAhead, currentCol));
                    if (twoAheadPiece == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(twoAhead, currentCol), null));
                    }
                }
                //ahead and to the right
                int colOnRight = currentCol + 1;
                if (rowInFront <= 8 && colOnRight <= 8 && rowInFront >= 1 && colOnRight >= 1) {
                    ChessPiece forwardRightPiece = board.getPiece(new ChessPosition(rowInFront, colOnRight));
                    if (forwardRightPiece != null && !forwardRightPiece.getTeamColor().equals(this.getTeamColor())) {
                        boolean promotion = (rowInFront == 8 && this.getTeamColor() == ChessGame.TeamColor.WHITE) || (rowInFront == 1 && this.getTeamColor() == ChessGame.TeamColor.BLACK);
                        if (promotion) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnRight), PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnRight), PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnRight), PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnRight), PieceType.ROOK));
                        }else {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnRight), null));
                        }
                    }
                }
                //ahead and to the left
                int colOnLeft = currentCol - 1;
                if (rowInFront <= 8 && colOnLeft <= 8 && rowInFront >= 1 && colOnLeft >= 1) {
                    ChessPiece forwardLeftPiece = board.getPiece(new ChessPosition(rowInFront, colOnLeft));
                    if (forwardLeftPiece != null && !forwardLeftPiece.getTeamColor().equals(this.getTeamColor())) {
                        boolean promotion = (rowInFront == 8 && this.getTeamColor() == ChessGame.TeamColor.WHITE) || (rowInFront == 1 && this.getTeamColor() == ChessGame.TeamColor.BLACK);
                        if (promotion) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnLeft), PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnLeft), PieceType.KNIGHT));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnLeft), PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnLeft), PieceType.ROOK));
                        }else {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(rowInFront, colOnLeft), null));
                        }
                    }
                }
                break;
        }
            case ROOK:
                int[][] rookMoveableDirections = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
                for (int[] direction : rookMoveableDirections) {
                    int rowMove = direction[0];
                    int colMove = direction[1];
                    int currentRow = myPosition.getRow();
                    int currentCol = myPosition.getColumn();

                    while (true) {
                        currentRow += rowMove;
                        currentCol += colMove;
                        if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                            break;
                        } else {
                            ChessPiece currentPiece = board.getPiece(new ChessPosition(currentRow, currentCol));
                            if (currentPiece == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                            } else {
                                if (currentPiece.getTeamColor().equals(this.getTeamColor())) {
                                    break;
                                } else {
                                    validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                    break;
                                }

                            }

                        }
                    }
                }
                break;
            case QUEEN:
                int[][] queenMoveableDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {0, -1}, {-1, 0}};
                for (int[] direction : queenMoveableDirections) {
                    int rowMove = direction[0];
                    int colMove = direction[1];
                    int currentRow = myPosition.getRow();
                    int currentCol = myPosition.getColumn();

                    while (true) {
                        currentRow += rowMove;
                        currentCol += colMove;
                        if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                            break;
                        } else {
                            ChessPiece currentPiece = board.getPiece(new ChessPosition(currentRow, currentCol));
                            if (currentPiece == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                            } else {
                                if (currentPiece.getTeamColor().equals(this.getTeamColor())) {
                                    break;
                                } else {
                                    validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case BISHOP:
                int[][] bishopMoveableDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
                for (int[] direction : bishopMoveableDirections) {
                    int rowMove = direction[0];
                    int colMove = direction[1];
                    int currentRow = myPosition.getRow();
                    int currentCol = myPosition.getColumn();

                    while (true) {
                        currentRow += rowMove;
                        currentCol += colMove;
                        if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                            break;
                        } else {
                            ChessPiece currentPiece = board.getPiece(new ChessPosition(currentRow, currentCol));
                            if (currentPiece == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                            } else {
                                if (currentPiece.getTeamColor().equals(this.getTeamColor())) {
                                    break;
                                } else {
                                    validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                    break;
                                }
                            }
                        }

                    }
                }
                break;

            case KNIGHT:
                int[][] knightMoveableDirections = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2},{1,-2},{-1,-2}};
                for (int[] direction : knightMoveableDirections) {
                    int rowMove = direction[0];
                    int colMove = direction[1];
                    int currentRow = myPosition.getRow();
                    int currentCol = myPosition.getColumn();

                    while (true) {
                        currentRow += rowMove;
                        currentCol += colMove;
                        if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                            break;
                        }else {
                            ChessPiece currentPiece = board.getPiece(new ChessPosition(currentRow, currentCol));
                            if (currentPiece == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                break;
                            } else {
                                if(currentPiece.getTeamColor().equals(this.getTeamColor())){
                                    break;
                                } else{
                                    validMoves.add(new ChessMove(myPosition, new ChessPosition(currentRow, currentCol), null));
                                    break;
                                }

                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("an invalid piece type was passed by get piece type");
        }

        return validMoves;
    }
}
