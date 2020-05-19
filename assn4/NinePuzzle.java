/* NinePuzzle.java
   CSC 225 - Spring 2017
   Assignment 4 - Template for the 9-puzzle
   
   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt
	
   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the 
   empty square. For example, a sample board with the middle square empty is
   
    1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
   
   An input file can contain an unlimited number of boards; each will be 
   processed separately.
  
   B. Bird    - 07/11/2014
   M. Simpson - 11/07/2015
*/

import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;

public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;


	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	
	
	public static boolean SolveNinePuzzle(int[][] B){
			
				
				int inputIndex = getIndexFromBoard(B);
				int goalIndex = getIndexFromBoard(goalBoard());
				int[][] list = adjacencyList();
				
				//Visited array will track which vertices have been visited.
				//Marked 1 if visited, 0 otherwise.
				int[] visited = new int[NUM_BOARDS+1];
				for(int i = 0; i < NUM_BOARDS; i++) 
				{
					visited[i] = 0;
				}
				
				int curr;
				Queue<Integer> bfsQueue = new LinkedList<Integer>();
				
				//bfsPath array will track all the vertices in our path.
				int[] bfsPath = new int[NUM_BOARDS+1];
				for(int i = 0; i < NUM_BOARDS; i++)
				{
					bfsPath[i] = -1;
				}
				
				//Start our bfs traversal from goal vertex, add goal index to queue
				bfsQueue.add(goalIndex);
				//mark goal vertex as visited
				visited[goalIndex] = 1;
				
				while(!bfsQueue.isEmpty())
				{	
			
					curr = bfsQueue.remove();
					
					//If we have found our input vertex then
					if(curr == inputIndex)
					{	
						
						int boardNum = curr;
	
						while(bfsPath[boardNum] != -1)
						{	
							//Print all boards starting from v to u starting with vertex v.
							printBoard(getBoardFromIndex(boardNum));
							//Backtrack to the previous board
							boardNum = bfsPath[boardNum];
						}
						printBoard(getBoardFromIndex(boardNum));
						return true;
					}
					//Otherwise if we haven't found our input vertex then bfs traverse..
					else
					{	
						//Go through our adjacencylist and find edges of curr vertex
						for(int edgeTo = 0; edgeTo < 4; edgeTo++)
						{	
							//If we have an edge from curr to some vertex then
							if(list[curr][edgeTo] != -1)
							{	
								//If that vertex has not been visited then
								if(visited[list[curr][edgeTo]] == 0)
								{	
									//Mark that vertex as visited
									visited[list[curr][edgeTo]] = 1;
									//Enqueue that vertex.
									bfsQueue.add(list[curr][edgeTo]);
									//Add that vertex to our path in order.
									bfsPath[list[curr][edgeTo]] = curr;
									
									
								}
							}
						}
					}
				}
				
				
			
				return false;
			}
	
	//Construct our goal board
	private static int[][] goalBoard(){
		int[][] board = new int[3][3];
		int goalEntry = 1;
		for(int goalRow = 0; goalRow < 3; goalRow++)
		{
			for(int goalCol = 0; goalCol < 3; goalCol++)
			{	
				while(goalEntry < 9) 
				{					
					board[goalRow][goalCol] = goalEntry;
					goalEntry++;
					break;
				}
						
				if(goalEntry == 9)
				{
					board[goalRow][goalCol] = 0;
				}
						
			}
		}
		return board;
	}
	
	
	//This method will construct our adjacency list based on the vertices we come across.
	private static int[][] adjacencyList() {
		
		int[][] list = new int[NUM_BOARDS+1][4];
		
		//Initialize our adjacencylist with every value being = -1
		for(int boardIndex = 0; boardIndex <= NUM_BOARDS; boardIndex++)
		{
			
			for(int boardCols = 0;boardCols<4;boardCols++)
			{
				list[boardIndex][boardCols] = -1;
			}
		}
		
		
		//Cases for different boards
		for(int k = 0;k<=NUM_BOARDS;k++)
		{
			int tileX = 0;
			int tileY = 0;
			
			int[][] oldBoard = getBoardFromIndex(k);	
			for(int boardRow=0;boardRow<3;boardRow++)
			{
				
				for(int boardCol=0;boardCol<3;boardCol++)
				{
					
					if(oldBoard[boardRow][boardCol] == 0)
					{
						tileX = boardRow;
						tileY = boardCol;
					}
					
				}
				
			}
	
			//Cases for different moves.
			if(tileX > 0)
			{
				int numEdges = 0;
				int [][] updatedBoard = getBoardFromIndex(k);
				updatedBoard[tileX][tileY] = updatedBoard[tileX - 1][tileY];
				updatedBoard[tileX - 1][tileY] = 0;
				while(list[getIndexFromBoard(updatedBoard)][numEdges] != -1)
				{
					numEdges++;
				}
				list[getIndexFromBoard(updatedBoard)][numEdges] = k;
			}
			
			if(tileX < 2)
			{
				int numEdges = 0;
				int [][] updatedBoard = getBoardFromIndex(k);
				updatedBoard[tileX][tileY] = updatedBoard[tileX + 1][tileY];
				updatedBoard[tileX + 1][tileY] = 0;
				while(list[getIndexFromBoard(updatedBoard)][numEdges] != -1)
				{
					numEdges++;
				}
				list[getIndexFromBoard(updatedBoard)][numEdges] = k;
				
			}
			
			if(tileY > 0)
			{
				int numEdges = 0;
				int [][] updatedBoard = getBoardFromIndex(k);
				updatedBoard[tileX][tileY] = updatedBoard[tileX][tileY - 1];
				updatedBoard[tileX][tileY -1] = 0;
				while(list[getIndexFromBoard(updatedBoard)][numEdges] != -1)
				{
					numEdges++;
				}
				list[getIndexFromBoard(updatedBoard)][numEdges] = k;
				
			}
			
			if(tileY < 2)
			{
				int numEdges = 0;
				int [][] updatedBoard = getBoardFromIndex(k);
				updatedBoard[tileX][tileY] = updatedBoard[tileX][tileY + 1];
				updatedBoard[tileX][tileY+1] = 0;
				while(list[getIndexFromBoard(updatedBoard)][numEdges] != -1)
				{
					numEdges++;
				}
				list[getIndexFromBoard(updatedBoard)][numEdges] = k;
				
			}
		}
		return list;
	}
	
	
	
	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284. 
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}
	

	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}
