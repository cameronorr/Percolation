import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	//setting variables used in multiple functions
	int count;
	int n;
	boolean open[];
	int grid[];
	WeightedQuickUnionUF quickUnion;
	
	//Creating the nxn grid, all sites blocked
	public Percolation(int n) {
		if(n <= 0) { //throwing an exception if the size of the array is invalid
			throw new IllegalArgumentException("n must be > 0");
		}
		//setting each global variable to be unique for this specific object created
		this.n= n;
		this.count = 0;
		this.grid = new int[this.n*this.n + 2];
		this.open = new boolean[this.n*this.n + 2];
		
		this.quickUnion = new WeightedQuickUnionUF(this.n*this.n + 2); //new WeightedQuickUnionUF object for this percolation object
		
		//populating the grid and open (array for whether the site is open) with default values
		//for grid, the default values are equal to that element number so each site is its own parent at the start
		//for the open array, populating with false values to say they are closed
		for(int i = 0; i < this.n*this.n + 2; i++) {
			this.grid[i] = i;
			this.open[i] = false;
		}		
	}
	
	//check whether a site is already open, open if not.
	//site # consists of (row,col) coordinate
	public void open(int row, int col) {
		//new variables for the position of the site, and the sites around it excluding diagonals
		int site = this.n*row + col;
		int top = site - this.n;
		int bot = site + this.n;
		int left = site - 1;
		int right = site + 1;
		
		//throwing an exception if the site number or grid length are invalid
		if(site > grid.length || site < 0) {
			throw new IllegalArgumentException("index " + site + " is not between 0 and " + (n-1));
		}
		
		//checking if the site is already open, if not, opening it
		if(!isOpen(row, col)) {
			open[site] = true; //open the site by setting the element value to true
			this.count ++; //increasing the count variable for the number of open sites
			
			if(!(site < this.n) && open[top] == true) { //checking if the site is anywhere but the top row, if thats true, and the above site is open, union the above site to the site
				quickUnion.union(top, site);
			}
			if(!(site > this.n*(this.n-1) - 1) && open[bot] == true) { //checking if the site is anywhere but the bottom row, same as above but for site under
				quickUnion.union(bot, site);
			}
			if(site%this.n != 0 && open[left] == true) { //check if the site is anywhere but the left side, same as above but for the site to the left
				quickUnion.union(left, site);
			}
			if((site + 1)%this.n != 0 && open[right] == true) { //check if the site is anywhere but the right side, same as above but for the site to the right
				quickUnion.union(right, site);
			}
			if(site < this.n) { //checking if the site is in the first row, then union it to the first virtual site
				quickUnion.union(site, grid[this.n*this.n]);
			}
			else if(site > this.n*(this.n-1) - 1) { //checking if the site is in the last row otherwise, then union it to the second virtual site
				quickUnion.union(site, grid[this.n*this.n + 1]);
			}
		}
		
	}
	
	//check whether a site is open
	public boolean isOpen(int row, int col) {
		//calculating the site number from the given row and column
		int site = n*row + col;
		
		//throwing an exception if the site number is out of the grid length, or is negative
		if(site > grid.length || site < 0) {
			throw new IllegalArgumentException("index " + site + " is not between 0 and " + (n-1));
		}
		return open[n*row + col]; //return the state of the open array index of site.
	}
	
	//check whether the site being checked is full by checking whether the virtual site
	//connected to the top open sites is connected to the site being called
	public boolean isFull(int row, int col) {
		int site = n*row + col;
		if(site > grid.length || site < 0) {
			throw new IllegalArgumentException("index " + site + " is not between 0 and " + (n-1));
		}
		
		if(quickUnion.connected(site,grid[n*n])){
			return true;
		}
		else {
			return false;
		}
	
	}
	//returns the value counted as each site is opened - count
	public int numberOfOpenSites() {
		return this.count;
	}
	
	//checks whether the system percolates; i.e., the top virtual site is connected to the bottom virtual site., also, whether there is one path from the top to the bottom
	public boolean percolates() {
		return quickUnion.connected(grid[n*n], grid[n*n + 1]);
	}
}
