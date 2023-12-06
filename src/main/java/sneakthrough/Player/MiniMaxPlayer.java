package sneakthrough.Player;

import sneakthrough.AI.MiniMaxMain.BoardState;
import sneakthrough.AI.MiniMaxMain.MiniMax;
import sneakthrough.Logic.Board;

public class MiniMaxPlayer implements Player {
    public MiniMax minimax ;

    public String player ;

    public int depth ;
    public MiniMaxPlayer(String player)
    {
        this.minimax = new MiniMax();
        this.player = player ;
        this.depth = 3 ;
    }

    public String getPlayer()
    {
        return this.player ;
    }

    @Override
    public void makeMove(Board board)
    {
        BoardState state = new BoardState(board,this.player);
        int[][] moveToMake = minimax.chooseBestMove(state,this.depth);
        state.makeMove(moveToMake);
    }

    public boolean hasWon(Board board){
        //if player is white
        if(this.player.equals("white")){
            for(int i = 0; i < board.getSize(); i++){
                if(board.getGrid()[0][i] != null && board.getGrid()[0][i].getColor().equals("white")){
                    return true;
                }
            }
        }
        //if player is black
        else{
            for(int i = 0; i < board.getSize(); i++){
                if(board.getGrid()[board.getSize() - 1][i] != null && board.getGrid()[board.getSize() - 1][i].getColor().equals("black")){
                    return true;
                }
            }
        }
        return false;
    }
}
