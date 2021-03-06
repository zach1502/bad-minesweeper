// MineSweeper Game
import java.util.Scanner;

public class MineSweeper{	

	// "Global" Constants and Variables
	public static final double MINESPAWNCHANCE = 0.10; // touch
	public static final int SIZE = 78; // touch
	public static int xCount = 0; // Don't touch
	public static int mineCount = 0; // Don't touch
	public static int MineLimit = 1; // touch

	public static final String INSERTNEWLINE = "\n   ";
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
		boolean firstMove = true;

		// the cover for the grid
		grid = gridcover(grid);

		// Generate mines until it hits the MineLimit
		while(mineCount != MineLimit){
			System.out.println("Generating more mines");
			mineLocation = generateMineLocations(mineLocation, SIZE);
		}

		// Generates the grid with the numbers underneath
		int[][] surroundingMinesGrid = generatesurroundingMinesGrid(mineLocation, SIZE);

		// show the grid
		// displayGrid(surroundingMinesGrid, mineLocation); <-- for testing
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

			// if its the first move AND there's a mine AND the entire grid is not filled with mines
			if (firstMove && mineLocation[row][col] && MineLimit != SIZE * SIZE){

				// move mine
				mineLocation = moveMine(mineLocation, row, col);

				// regenerate the hidden grid
				generatesurroundingMinesGrid(mineLocation, SIZE);
			}

			// Clear the right tiles
			firstMove = false;
			int prevSquare = 0;
			clearGrid(grid, surroundingMinesGrid, col, row, SIZE, prevSquare, mineLocation);

			// Show the grid but with the number underneath shown if we uncovered it in the clearGrid method
			displayOverlayedGrid(surroundingMinesGrid, grid);

			// If you pick the coordinates of a mine, BOOM you lose
			if (mineLocation[row][col]){
				displayGrid(surroundingMinesGrid, mineLocation);
				System.out.println("GAME OVER: YOU LOSE!");
				break;
			}

			// if you clear everything except the mines, you win
			if (winCondition(mineLocation, grid)) {
				displayGrid(surroundingMinesGrid, mineLocation);
				System.out.println("GAME OVER: YOU WIN!");
				break;
			} 
		}

		input.close();
	}

	public static boolean[][] generateMineLocations(boolean[][] mineLocation, final int SIZE){
		int randRow;
		int randCol; 
		boolean flag = false;

		// Would cause an infinite loop otherwise
		if (MineLimit > SIZE * SIZE) MineLimit = SIZE * SIZE;

		// for each element in the 2d array..
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {

				// try to spawn the thingy only if its currently below the limit
				if ((Math.random() < MINESPAWNCHANCE) && (mineCount < MineLimit)) {
					randCol = (int)(Math.random() * SIZE - 1 ) ;
					randRow = (int)(Math.random() * SIZE - 1) ;
					mineCount++;
					mineLocation[randRow][randCol] = true;
				}

				// break out of both loops if the number of mines reach the MineLimit
				flag = (mineCount == MineLimit);
				if (flag) break;
			}

			if (flag) break;
		}
	
		return mineLocation;
	}

	public static int[][] generatesurroundingMinesGrid(boolean[][] mineLocation, final int SIZE){

		// surroundingMinesGrid aka the grid with the numbers and stuff
		int[][] surroundingMinesGrid = new int[SIZE][SIZE];

		// fill every space
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {

				// try catch = superior
				if (mineLocation[i][j]){
					if(i+1 < SIZE){
					    surroundingMinesGrid[i+1][j] += 1;
					}
					if(i-1 >= 0){
						surroundingMinesGrid[i-1][j] += 1;
					}
					if(i+1 < SIZE && j+1 < SIZE){
					    surroundingMinesGrid[i+1][j+1] += 1;
					}
					if(j+1 < SIZE){
					    surroundingMinesGrid[i][j+1] += 1;
					}
					if(i-1 >= 0 && j+1 < SIZE){
					    surroundingMinesGrid[i-1][j+1] += 1;
					}
					if(i+1 < SIZE && j-1 >= 0){
					    surroundingMinesGrid[i+1][j-1] += 1;
					}
					if(j-1 >= 0){
					    surroundingMinesGrid[i][j-1] += 1;
					}
					if(i-1 >= 0 && j-1 >= 0){
					    surroundingMinesGrid[i-1][j-1] += 1;
					}
				}
			}
		}

		return surroundingMinesGrid;
	}

	public static boolean[][] moveMine(boolean[][] mineLocation, int row, int col){

		// keep running
		while(true){

			// random coords
			int randCol = (int)(Math.random() * SIZE - 1 );
			int randRow = (int)(Math.random() * SIZE - 1);

			// if there isn't a mine there at random Coords
			if (!mineLocation[randRow][randCol]){

				// move the mine over there
				mineLocation[row][col] = false;
				mineLocation[randRow][randCol] = true;
				break;
			}
		}

		return mineLocation;
	}

	public static char[][] clearGrid(char[][] grid, int[][] surroundingMinesGrid, int col, int row, final int SIZE, int prevSquare, boolean[][] mineLocation){

		// 100% outside
		if(row >= SIZE || col >= SIZE || row < 0 || col < 0) return grid;

		// visited the element already
		if(grid[row][col] == ' ') return grid;

		// Prevents recursion going into an infinite loop by marking it as visited
		grid[row][col] = ' ';

		// if the hidden number is 0 
		if(surroundingMinesGrid[row][col] == 0 ){

			/* C = Clicked
			--------
			7| 0| 1|
			--------
			6| C| 2|
			--------
			5| 4| 3|
			-------- */
			// recursion go brrrrr (Down)
			if (prevSquare != 0){
				prevSquare = 4;
				grid = clearGrid(grid, surroundingMinesGrid, col, row+1, SIZE, prevSquare, mineLocation);

			}

			// left
			if (prevSquare != 2){
				prevSquare = 6;
				grid = clearGrid(grid, surroundingMinesGrid, col-1, row, SIZE, prevSquare, mineLocation);
			}

			// right
			if (prevSquare != 6){
				prevSquare = 2;
				grid = clearGrid(grid, surroundingMinesGrid, col+1, row, SIZE, prevSquare, mineLocation);
			}

			// up
			if (prevSquare != 4){
				prevSquare = 0;
				grid = clearGrid(grid, surroundingMinesGrid, col, row-1, SIZE, prevSquare, mineLocation);
			}

			// left-down
			if (prevSquare != 1){
				prevSquare = 5;
				grid = clearGrid(grid, surroundingMinesGrid, col-1, row+1, SIZE, prevSquare, mineLocation);
			}

			// left-up
			if (prevSquare != 3){
				prevSquare = 7;
				grid = clearGrid(grid, surroundingMinesGrid, col-1, row-1, SIZE, prevSquare, mineLocation);
			}

			// right-up
			if (prevSquare != 5){
				prevSquare = 1;
				grid = clearGrid(grid, surroundingMinesGrid, col+1, row-1, SIZE, prevSquare, mineLocation);
			}

			// right-down
			if (prevSquare != 7){
				prevSquare = 3;
				grid = clearGrid(grid, surroundingMinesGrid, col+1, row+1, SIZE, prevSquare, mineLocation);
			}
		}

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

	// fill all elements with 'x'
	public static char[][] gridcover(char[][] grid){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = 'x';
			}
		}
		return grid;
	}

	// Display the grid
	public static void displayGrid(char[][] grid){
		// Display the col numbers
		System.out.print(INSERTNEWLINE);
		for(int i = 0; i < grid[0].length; i++){
			System.out.printf(" %d ", i);
		}
		System.out.println();
		
		for(int i = 0; i < grid.length; i++){
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
	public static void displayGrid(int[][] grid){

		// Display the col numbers
		System.out.print(INSERTNEWLINE);
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
		System.out.print(INSERTNEWLINE);
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

				output = (mineLocation[i][j])? (SEPARATORMINE) : (SEPARATORTWOCHAR + grid[i][j]);
				System.out.print(output);
			}
			System.out.println(VERTICALSEPERATOR);
		}
		
		System.out.println(HORIZONTALSEPARATOR);
	}

	public static void displayOverlayedGrid(int[][] surroundingMinesGrid, char[][] grid){

		// Display the col numbers
		System.out.print(INSERTNEWLINE);
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
					output = (surroundingMinesGrid[i][j] == 0)? (SEPARATORTHREECHAR):(SEPARATORTWOCHAR + surroundingMinesGrid[i][j]);
				}

				System.out.print(output);
			}
			System.out.println(VERTICALSEPERATOR);
		}
			
		System.out.println(HORIZONTALSEPARATOR);
	}
}
