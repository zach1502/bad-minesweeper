// MineSweeper Game
import java.util.Scanner;

public class MineSweeper
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		final int SIZE = 9;
		char[][] grid = new char[SIZE][SIZE];
		boolean[][] mineLocation = new boolean[SIZE][SIZE];
		int[][] hiddenGrid = new int[SIZE][SIZE];
		

		mineLocation = generateMineLocations(mineLocation, SIZE);
		hiddenGrid = generateHiddenGrid(mineLocation, SIZE);
		displayGrid(hiddenGrid, mineLocation);
		grid = gridcover(grid);

		displayGrid(grid);
		
		while(true){
			int row = 0;
			int col = 0;

			try {


				System.out.print("(Player 1) What row would you like to dig up?: ");
				row = input.nextInt();
				System.out.print("(Player 1) What column would you like to dig up?: ");
				col = input.nextInt();

			} catch (Exception e) {
				System.out.println("row or column doesn't exist!");
			}

			clearGrid(grid, hiddenGrid, col, row, SIZE);

			displayOverlayedGrid(hiddenGrid, grid);

			if (mineLocation[row][col]){
				System.out.println("GAME OVER: YOU LOSE!");
				displayGrid(hiddenGrid, mineLocation);
				break;
			}

			if (winCondition(mineLocation, grid)) {
				System.out.println("GAME OVER: YOU WIN!");
				displayGrid(hiddenGrid, mineLocation);
				break;
			} 
		}
	}

	// Display the grid
	public static void displayGrid(char[][] grid)
	{
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++)
			System.out.print(i + "  ");
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++)
		{
			System.out.println("  ----------------------------");
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++)
			{
				if(grid[i][j] != 'x')
					System.out.print("|  ");
				else 
					System.out.print("| " + grid[i][j]);
			}
			System.out.println("|");
		}
		
		System.out.println("  ----------------------------");
	}

	public static boolean[][] generateMineLocations(boolean[][] mineLocation, final int SIZE){
		int randRow;
		int randCol; 
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {
				if ((Math.random() < 0.10)) {
					randCol = (int)(Math.random() * SIZE - 1 ) ;
					randRow = (int)(Math.random() * SIZE - 1) ;

					mineLocation[randRow][randCol] = true;
				}
			}
		}

		return mineLocation;
	}

	public static int[][] generateHiddenGrid(boolean[][] mineLocation, final int SIZE){
		int[][] hiddenGrid = new int[SIZE][SIZE];
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {
				if (i < 0 || j < 0) {
					continue;
				}
				if(j >= SIZE || i >= SIZE){
					continue;
				}

				if (mineLocation[i][j]){

					if (j==0 && i==0){
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i+1][j+1] += 1;
					}

					// top right corner
					if (j==SIZE-1 && i==0){
						hiddenGrid[i][j-1] += 1;
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i+1][j-1] += 1;
					}

					// bottom left corner
					if (j==0 && i==SIZE-1){
						hiddenGrid[i-1][j] += 1;
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i-1][j+1] += 1;
					}

					// bottom right corner
					if (j==SIZE-1 && i==SIZE-1){
						hiddenGrid[i-1][j-1] += 1;
						hiddenGrid[i][j-1] += 1;
						hiddenGrid[i-1][j] += 1;
					}
					else if (i == 0) {
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i+1][j+1] += 1;
						hiddenGrid[i+1][j-1] += 1;
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i][j-1] += 1;
					}

					else if(j == 0){
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i+1][j+1] += 1;
						hiddenGrid[i-1][j+1] += 1;
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i-1][j] += 1;
					}

					else if(i == SIZE-1){
						hiddenGrid[i-1][j] += 1;
						hiddenGrid[i-1][j+1] += 1;
						hiddenGrid[i-1][j-1] += 1;
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i][j-1] += 1;
					}

					else if(j == SIZE -1){
						hiddenGrid[i][j-1] += 1;
						hiddenGrid[i+1][j-1] += 1;
						hiddenGrid[i-1][j-1] += 1;
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i-1][j] += 1;
					}

					else{
						hiddenGrid[i+1][j] += 1;
						hiddenGrid[i-1][j] += 1;
						hiddenGrid[i+1][j+1] += 1;
						hiddenGrid[i][j+1] += 1;
						hiddenGrid[i-1][j+1] += 1;
						hiddenGrid[i+1][j-1] += 1;
						hiddenGrid[i][j-1] += 1;
						hiddenGrid[i-1][j-1] += 1;
					}
				}
			}
		}

		return hiddenGrid;
	}

	public static void displayGrid(int[][] grid)
	{
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++)
			System.out.print(i + "  ");
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++){
			System.out.println("  ----------------------------");
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++)
			{
				System.out.print("| " + grid[i][j]);
			}
			System.out.println("|");
		}
		
		System.out.println("  ----------------------------");
	}

	public static void displayOverlayedGrid(int[][] hiddenGrid, char[][] grid){
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++)
			System.out.print(i + "  ");
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++){
			System.out.println("  ----------------------------");
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++)
			{
				if (grid[i][j] == 'x') {
					System.out.print("| " + grid[i][j]);
				}
				else{
					if (hiddenGrid[i][j] == 0) {
						System.out.print("|  ");
					}
					else{
						System.out.print("| " + hiddenGrid[i][j]);
					}
				}
			}
			System.out.println("|");
		}
			
		System.out.println("  ----------------------------");
	}
	

	public static char[][] gridcover(char[][] grid){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = 'x';
			}
		}
		return grid;
	}

	public static char[][] clearGrid(char[][] grid, int[][] hiddenGrid, int col, int row, final int SIZE){

		// outside
		if(row >= SIZE || col >= SIZE || row < 0 || col < 0){
			return grid;
		}

		// inside
		if(hiddenGrid[row][col] == 0 && grid[row][col] != ' '){
			grid[row][col] = ' ';
			grid = clearGrid(grid, hiddenGrid, col, row+1, SIZE);
			grid = clearGrid(grid, hiddenGrid, col-1, row, SIZE);
			grid = clearGrid(grid, hiddenGrid, col, row-1, SIZE);
			grid = clearGrid(grid, hiddenGrid, col+1, row, SIZE);
		}

		// mark as visited
		grid[row][col] = ' ';

		return grid;
	}

	public static void displayGrid(int[][] grid, boolean[][] mineLocation){
		// Display the col numbers
		System.out.print("\n   ");
		for(int i = 0; i < grid[0].length; i++)
			System.out.print(i + "  ");
		System.out.println();
		
		
		for(int i = 0; i < grid.length; i++)
		{
			System.out.println("  ----------------------------");
			
			// Display the row number
			System.out.print(i + " ");
			
			// Display the squares
			for(int j = 0; j < grid[i].length; j++)
			{
				if (mineLocation[i][j]) {
					System.out.print("| M");
				}
				else{
					System.out.print("| " + grid[i][j]);
				}
			}
			System.out.println("|");
		}
		
		System.out.println("  ----------------------------");
	}

	public static boolean winCondition(boolean[][] mineLocation, char[][] grid){
		int mineCount=0;
		int xCount=0;

		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {
				if(mineLocation[i][j]){
					mineCount++;
				}

				if (grid[i][j] == 'x') {
					xCount++;
				}
			}
		}

		return (mineCount >= xCount);
	}
}
