// MineSweeper Game
import java.util.Scanner;


public class MineSweeper{	

	// "Global" Constants and Variables
	public static int mineCount = 0;
	public static final int MINELIMIT = 10;
	public static final double MINESPAWNCHANCE = 0.10;
	public static final int SIZE = 8;

	public static final String VERTICALSEPERATOR = "| ";
	public static final String HORIZONTALSEPARATOR = "  ----------------------------";
	public static final String SEPARATORTWOCHAR = "| ";
	public static final String SEPARATORTHREECHAR = "|  ";
	public static final String SEPARATORMINE = "| M";
	public static String output;

	public static void main(String[] args){

		// New scanner object
		Scanner input = new Scanner(System.in);
		
		// Many Grids
		char[][] grid = new char[SIZE][SIZE];
		boolean[][] mineLocation = new boolean[SIZE][SIZE];
		int[][] hiddenGrid = new int[SIZE][SIZE];
		
		// Generate mines until it hits the MINELIMIT
		while(mineCount != MINELIMIT){
			mineLocation = generateMineLocations(mineLocation, SIZE);
		}

		// Generates the grid with the numbers underneath
		hiddenGrid = generateHiddenGrid(mineLocation, SIZE);

		// the cover for the grid
		grid = gridcover(grid);

		// show the grid
		// displayGrid(hiddenGrid, mineLocation); <-- for testing
		displayGrid(grid);
		
		// Always run
		while(true){
			int row = -1;
			int col = -1;

			// while its out of bounds
			while((row < 0 || row >= SIZE) || (col < 0 || col >= SIZE)){

				// get inputs
				System.out.print("(Player 1) What row would you like to dig up?: ");
				row = input.nextInt();
				System.out.print("(Player 1) What column would you like to dig up?: ");
				col = input.nextInt();

				// output this if inputs are still out of bounds
				if ((row < 0 || row >= SIZE || col < 0 || col >= SIZE)){
					System.out.println("row or column doesn't exist!");
				}
			}

			// Clear the right tiles
			clearGrid(grid, hiddenGrid, col, row, SIZE);

			// Show the grid but with the number underneath shown if we uncovered it in the clearGrid method
			displayOverlayedGrid(hiddenGrid, grid);

			// If you pick the coordinates of a mine, BOOM you lose
			if (mineLocation[row][col]){
				displayGrid(hiddenGrid, mineLocation);
				System.out.println("GAME OVER: YOU LOSE!");
				break;
			}

			// if you clear everything except the mines, you win
			if (winCondition(mineLocation, grid)) {
				displayGrid(hiddenGrid, mineLocation);
				System.out.println("GAME OVER: YOU WIN!");
				break;
			} 
		}
	}

	public static boolean[][] generateMineLocations(boolean[][] mineLocation, final int SIZE){
		int randRow;
		int randCol; 
		boolean flag = false;

		// for each element in the 2d array..
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {

				// try to spawn the thingy only if its currently below the limit
				if ((Math.random() < MINESPAWNCHANCE) && (mineCount < MINELIMIT)) {
					randCol = (int)(Math.random() * SIZE - 1 ) ;
					randRow = (int)(Math.random() * SIZE - 1) ;
					mineCount++;
					mineLocation[randRow][randCol] = true;
				}

				// break out of both loops if the number of mines reach the MINELIMIT
				flag = (mineCount == MINELIMIT);
				if (flag) break;
			}

			if (flag) break;
		}
	
		return mineLocation;
	}

	public static int[][] generateHiddenGrid(boolean[][] mineLocation, final int SIZE){

		// hiddenGrid aka the grid with the numbers and stuff
		int[][] hiddenGrid = new int[SIZE][SIZE];

		// fill every space
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {

				// skip 100% out of bounds checks
				if (i < 0 || j < 0 || j >= SIZE || i >= SIZE) {
					continue;
				}

				// try catch = superior
				if (mineLocation[i][j]){
					if(i+1 < SIZE){
					  hiddenGrid[i+1][j] += 1;
					}
					if(i-1 > 0){
					  hiddenGrid[i-1][j] += 1;
					}
					if(i+1 < SIZE && j+1 < SIZE){
					  hiddenGrid[i+1][j+1] += 1;
					}
					if(j+1 < SIZE){
					  hiddenGrid[i][j+1] += 1;
					}
					if(i-1 > 0 && j+1 < SIZE){
					  hiddenGrid[i-1][j+1] += 1;
					}
					if(i+1 < SIZE && j-1 > 0){
					  hiddenGrid[i+1][j-1] += 1;
					}
					if(j-1 > 0){
					  hiddenGrid[i][j-1] += 1;
					}
					if(i-1 > 0 && j-1 > 0){
					  hiddenGrid[i-1][j-1] += 1;
					}
				}
			}
		}

		return hiddenGrid;
	}

	// Display the grid
	public static void displayGrid(char[][] grid){
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++){
			System.out.printf(" %d ", i);
		}
		System.out.println();
		
		for(int i = 0; i < grid.length; i++)
		{
			System.out.println(HORIZONTALSEPARATOR);
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++)
			{	
				output = (grid[i][j] != 'x')? SEPARATORTHREECHAR: (SEPARATORTWOCHAR + grid[i][j]);
				System.out.print(output);
			}
			System.out.println(VERTICALSEPERATOR);
		}
		
		System.out.println(HORIZONTALSEPARATOR);
	}
	
	// Overloaded method
	public static void displayGrid(int[][] grid)
	{
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++){
			System.out.printf(" %d ", i);
		}
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++){
			System.out.println(HORIZONTALSEPARATOR);
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++){
				System.out.print(SEPARATORTWOCHAR + grid[i][j]);
			}
			System.out.println(VERTICALSEPERATOR);
		}
		
		System.out.println(HORIZONTALSEPARATOR);
	}

	// Overload
	public static void displayGrid(int[][] grid, boolean[][] mineLocation){
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++){
			System.out.printf(" %d ", i);
		}
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++)
		{
			System.out.println(HORIZONTALSEPARATOR);
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++){

				output = (mineLocation[i][j])? (SEPARATORMINE) : (SEPARATORTWOCHAR + grid[i][j]);
				System.out.print(output);
			}
			System.out.println(VERTICALSEPERATOR);
		}
		
		System.out.println(HORIZONTALSEPARATOR);
	}

	public static void displayOverlayedGrid(int[][] hiddenGrid, char[][] grid){

		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++){
			System.out.printf(" %d ", i);
		}
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++){
			System.out.println(HORIZONTALSEPARATOR);
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++){
				if(grid[i][j] == 'x'){
					output = (SEPARATORTWOCHAR + grid[i][j]);
				}
				else{
					output = (hiddenGrid[i][j] == 0)? (SEPARATORTHREECHAR):(SEPARATORTWOCHAR + hiddenGrid[i][j]);
				}

				System.out.print(output);
			}
			System.out.println(VERTICALSEPERATOR);
		}
			
		System.out.println(HORIZONTALSEPARATOR);
	}
	
	// fill all elements with 'x'
	public static char[][] gridcover(char[][] grid){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = 'x';
			}
		}
		return grid;
	}

	public static char[][] clearGrid(char[][] grid, int[][] hiddenGrid, int col, int row, final int SIZE){

		// 100% outside
		if(row >= SIZE || col >= SIZE || row < 0 || col < 0) return grid;

		// visited the element already
		if(grid[row][col] == ' ') return grid;

		// if the hidden number is 0 
		if(hiddenGrid[row][col] == 0){

			// Prevents recursion going into an infinite loop by marking it as
			grid[row][col] = ' ';

			// recursion go brrrrr up down left and right
			grid = clearGrid(grid, hiddenGrid, col, row+1, SIZE);
			grid = clearGrid(grid, hiddenGrid, col-1, row, SIZE);
			grid = clearGrid(grid, hiddenGrid, col, row-1, SIZE);
			grid = clearGrid(grid, hiddenGrid, col+1, row, SIZE);
		}

		// mark as visited. Also ensures that we go 1 more beyond the edge to reveal a number
		grid[row][col] = ' ';

		return grid;
	}

	public static boolean winCondition(boolean[][] mineLocation, char[][] grid){
		int xCount=0;

		// counts all 'x's remaining
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {
				if (grid[i][j] == 'x') {
					xCount++;
				}
			}
		}

		// return true if the number of 'x's is the same as the number of mines left
		return (mineCount == xCount);
	}
}
