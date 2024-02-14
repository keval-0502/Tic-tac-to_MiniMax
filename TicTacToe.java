import java.util.ArrayList;// // package for resizable array by ArrayList class
import java.util.Scanner; // package for taking input by Scanner class

public class TicTacToe {
    private static Scanner sc = new Scanner(System.in); // object sc from class Scanner for taking inputs from user
    private static Board board = new Board(); // object board from class Board
    private static String name;
    
    private static boolean gameEnded = false; // setting false so that when game has ended it can be set to true
    private static boolean player = true; // player available or present

    public static void main(String[] args){
    	System.out.println("A GAME OF TIC TAC TOE AGAINST THE COMPUTER DEVELOPED BY\nNAME - KEVAL BAVISI - SAP ID - 60001200019");
    	System.out.println("WELCOME TO TIC TAC TOE GAME CREATED FOR JAVA MINI PROJECT"); // welcome message
        System.out.print("ENTER THE PLAYER NAME: "); // player name 
        name = sc.nextLine(); // save name in the String name
        System.out.println("WELCOME "+ name +" TRY AND BEAT THE COMPUTER");
        System.out.println("THE COMPUTER PAWN IS CIRCLE OR O");
        System.out.println(name + " YOUR PAWN IS CROSS OR X \nBEST OF LUCK AGAINST THE COMPUTER"); // user has the pawn of cross or X
        System.out.println("THIS IS YOUR PLAYING BOARD");
        System.out.println(board); // print the board. it should be empty at the start of the game
        while(!gameEnded){ //while the game is not ended 
            Position position = null;
            if(player){ // if player is true i.e present
            	System.out.println(name+ " MAKE YOUR MOVE");
                position = makeMove(); //run makemove
                board = new Board(board, position, PlayerSign.Cross); // place x on the move placed
            }else{
            	System.out.println("THE COMPUTER MOVE IS :");
                board = findBestMove(board); // computer move and run findbestmove optimal move for computer
            }               
            player = !player; // player is not present
                System.out.println(board); // print final state of board
                evaluateGame(); // run evaluategame to check who won
        }
    }

    private static Board findBestMove(Board board) { //as COMP is 'o' best move is only available for the token 'o'. [x is user input].  
        ArrayList<Position> positions = board.getFreePositions();
        Board bestMove = null;
        int previous = Integer.MIN_VALUE; // store min value of or the previous value for position
        for(Position p : positions){
            Board Move = new Board(board, p, PlayerSign.Circle); // place 'o'
            int current = min(Move); 
            //System.out.println("Move Score: " + current);
            if(current > previous){
                bestMove = Move;
                previous = current; // previous value is equal to new value
            }
        }
        return bestMove;
    }

    public static int max(Board board){
        GameState gameState = board.getGameState();
        if(gameState == GameState.CircleWin)
            return 1;
        else if(gameState == GameState.CrossWin)
            return -1;
        else if(gameState == GameState.Draw)
            return 0;
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MIN_VALUE;
        for(Position p : positions){
            Board b = new Board(board, p, PlayerSign.Circle);
            int move = min(b);
            if(move > best)
                best = move;
        }       
        return best;
    }

    public static int min(Board board){ // preset integer values in the inbuilt class Board.
        GameState gameState = board.getGameState();
        if(gameState == GameState.CircleWin)
            return 1;
        else if(gameState == GameState.CrossWin)
            return -1;
        else if(gameState == GameState.Draw)
            return 0;
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MAX_VALUE;
        for(Position p : positions){
            Board b = new Board(board, p, PlayerSign.Cross);
            int move = max(b);
            if(move < best)
                best = move;
        }
        return best;
    }

    private static void evaluateGame(){ // method name evaluateGame
        GameState gameState = board.getGameState(); // get current state of the game
        gameEnded = true; // if game ended
        switch(gameState){ //switch case
            case CrossWin : 
                System.out.println(name+" You Won!"); // x won i.e user won
                break;
            case CircleWin : 
                System.out.println("SORRY " +name+ " COMPUTER WON!!"); // o won i.e computer won
                break;
            case Draw : 
                System.out.println("ITS A DRAW!"); // no one wins
                break;
            default : gameEnded = false; // game hasnt ended
                break;
        }
    }

    public static Position makeMove(){ 
        Position position = null; // null address 
        while(true){
            System.out.print("Pick 0, 1 or 2 for column: "); // column is numbered as 0,1,2 
            int column = getColOrRow();
            System.out.print("Pick 0, 1 or 2 for row: "); // row is numbered as 0,1,2
            int row = getColOrRow();
            position = new Position(column, row); // place the pawn in coordinate as (col,row)
            if(board.isMarked(position)) // if position is already marked at the entered vector/address
                System.out.println("Already marked!"); 
            else break;
        }
        return position; // return the position of the move placed
    }

    private static int getColOrRow(){
        int ret = -1; // crosswin value
        while(true){
            try{
                ret = Integer.parseInt(sc.nextLine());//read integer (-1,1,0) value using parseInt which splits them and parses into integers
            } catch (NumberFormatException e){} // invalid string conversion
            if(ret < 0 | ret > 2)
                System.out.print("\nInvalid input. Please pick 0, 1 or 2: ");// if input is less than 0 and more than 2. It is invalid.
            else break;
        }
        return ret; // return value of ret
    }
}

final class Position { 
    private final int column;
    private final int row;

    public Position(int column, int row){ // constructor position
        this.column = column; // current class object column
        this.row = row; // current class object row
    }
    public int getRow(){
  	return this.row; // row value (0,1,2)
    }
    public int getColumn(){
	return this.column; // column value (0,1,2)
    }
}

enum PlayerSign{ //(DATA TYPE)enumeration variable used when we know the possible set of outcomes
    Cross, Circle // x and o
}

enum GameState { //(DATA TYPE)enumeration variable used when we know the possible set of outcomes
     Incomplete, CrossWin, CircleWin, Draw
}

class Board {
    private char[][] board; //e = empty, x = cross, o = circle. // 2D array

    public Board(){ // constructor named board with 0 arguments
        board = new char[3][3];
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 3; x++)
                board[x][y] = 'e'; //Board initially empty
    }

    public Board(Board from, Position position, PlayerSign sign){ // overloading the constructor with 3 set of variables 
        board = new char[3][3];
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 3; x++)
                board[x][y] = from.board[x][y];
        board[position.getColumn()][position.getRow()] = sign==PlayerSign.Cross ? 'x':'o'; // check for cross
    }

    public ArrayList<Position> getFreePositions(){ // uses the imported arraylist package.
        ArrayList<Position> retArr = new ArrayList<Position>();     
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 3; x++)
                if(board[x][y] == 'e')
                    retArr.add(new Position(x, y)); //adds  the col,row number of the free space left on the board to the list
        return retArr; // returns the col,row coordinate of the free space available on the board
    }

    public GameState getGameState(){    // returns the enum components for method getGameState
        if(hasWon('x'))
            return GameState.CrossWin;//enum component
        else if(hasWon('o'))
            return GameState.CircleWin;//enum component
        else if(getFreePositions().size() == 0)// if no free positions on the board
            return GameState.Draw;//if no free spaces then game is drawn
        else return GameState.Incomplete;
    }

    private boolean hasWon(char sign){ // sign is an object of the datatype enum PlayerSign
	int x,y;//x represents column, y represents row

	
	if(board[0][0]==sign && board[1][1] == sign && board [2][2]==sign) // check diagonal for sign
	    return true;
	if(board[0][2]==sign && board[1][1] == sign && board [2][0]==sign) // check another diagonal for sign
	    return true;

	//Check row
	for(x=0;x<3;x++){
	    for(y=0;y<3;y++)
		if(board[x][y] != sign)//checking for (column,row)
		    break;
	    if(y==3)//return true for full row
		return true;
	}

	//Check column
	for(x=0;x<3;x++){
	    for(y=0;y<3;y++)
		if(board[y][x] != sign)//checking for (column,row) by changing plot method from x,y to y,x to make y as a variable of column
		    break;
	    if(y==3)
		return true;// return true for full column
	}
       	return false;//no full column
    }

    public boolean isMarked(Position position){
        if(board[position.getColumn()][position.getRow()] != 'e')// if (condition) IS NOT equal to empty
            return true;//marked
        return false;//not marked
    }

    public String toString(){
        String returnString = "\n";
        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 3; x++){
                if(board[x][y] ==  'x' || board[x][y] == 'o') // OR OPERATION TO CHECK THE ELEMENTS OF BOARD
                    returnString += "["+board[x][y]+"]"; // ADD BOARD STATUS TO RET STRING
                else
                    returnString += "[ ]"; // RET STRING IS EMPTY
            }
            returnString += "\n"; // GO TO NEXT LINE
        }       
        return returnString;
    }   

}