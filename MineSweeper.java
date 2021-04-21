// Five Stones Game
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
		grid = gridcover(grid);

		displayGrid(grid);
		
		do{
			int row = 0;
			int col = 0;

			try {

				System.out.print("(Player 1) What row would you like to place a stone?: ");
				row = input.nextInt();
				System.out.print("(Player 1) What column would you like to place a stone?: ");
				col = input.nextInt();
				clearGrid(grid, hiddenGrid, col, row);

				displayOverlayedGrid(hiddenGrid, grid);
			} catch (Exception e) {}

			if (mineLocation[row][col]){
				System.out.println("GAME OVER: YOU LOSE");
				displayGrid(hiddenGrid, mineLocation);
				break;
			}
			
			
		}while(true);
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
				if ((Math.random() < 0.05)) {
					randCol = (int)(Math.random() * SIZE - 1 ) +1;
					randRow = (int)(Math.random() * SIZE - 1) +1;

					mineLocation[randRow][randCol] = true;
				}
			}
		}

		return mineLocation;
	}

	public static int[][] generateHiddenGrid(boolean[][] mineLocation, int SIZE){
		int[][] hiddenGrid = new int[SIZE][SIZE];
		for (int i = 0; i < mineLocation.length; i++) {
			for (int j = 0; j < mineLocation.length; j++) {
				if (mineLocation[i][j] == true){
					try {
						hiddenGrid[i+1][j] += 1;
					} catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i-1][j] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i+1][j+1] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i][j+1] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i-1][j+1] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i+1][j-1] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i][j-1] += 1;
					}catch(Exception e){
						// silent
					}
					try {
						hiddenGrid[i-1][j-1] += 1;
					}catch(Exception e){
						// silent
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

	public static char[][] clearGrid(char[][] grid, int[][] hiddenGrid, int col, int row){

		if (hiddenGrid[row][col] == 0){
			try {
			grid[row+1][col+1] = ' ';
			} catch(Exception e){}
			try {
			grid[row][col+1] = ' ';
			}catch(Exception e){}
			try {
			grid[row-1][col+1] = ' ';
			}catch(Exception e){}
			try {
			grid[row+1][col] = ' ';
			}catch(Exception e){}
			try {
			grid[row][col] = ' ';
			}catch(Exception e){}
			try {
			grid[row-1][col] = ' ';
			}catch(Exception e){}
			try {
			grid[row+1][col-1] = ' ';
			}catch(Exception e){}
			try {
			grid[row][col-1] = ' ';
			}catch(Exception e){}
			try {
			grid[row-1][col-1] = ' ';
			}catch(Exception e){}
		}

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
}