import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.Scanner;

public class PercolationStats {
	//setting variables that are used throughout multiple functions
	Percolation perc;
	double stoTrials[];
	int trials;
	int n;
	//performing the Monte-Carlo simulation each time a new PercolationStats object is created
	//has a parameter to know how many trials it will run.
	public PercolationStats(int n, int trials) {
		//throwing exception if the inputs are out of range
		if(n <= 0 || trials <= 0) {
			throw new IllegalArgumentException("The size and number of trials must be > 0.");
		}
		
		//setting the trials array, trials number and array size to this object created
		this.stoTrials = new double[trials];
		this.trials = trials;
		this.n = n;
		
		//creating integer variables to store the random number, column and row number, and the number of open sites
		//creating a double variable for the ratio of open to total sites
		int rand, col, row, numOpen;
		double ratio;
		
		for(int i = 0; i < trials; i++) { //for loop for the number of trials being undergone
			this.perc = new Percolation(n); //new percolation object for this PercolationStats object
			
			//while the grid doesn't percolate, continue creating new random numbers, convert the number
			//to row and column number, then open that row and column
			while(!perc.percolates()) {
				rand = StdRandom.uniform((int) Math.pow(this.n,2) + 1);
				row = rand / this.n;
				col = rand-this.n*row;
				perc.open(row, col);
			}
			numOpen = perc.numberOfOpenSites(); //number of open sites
			ratio = numOpen / Math.pow(this.n, 2); // calculating the ratio
			this.stoTrials[i] = ratio; //storing the current trial in the double array
			
		}
	}
	
	//calculating the mean of the set of trials
	public double mean() {
		return StdStats.mean(this.stoTrials);
	}
	
	//calculating the standard deviation in the set of trials
	public double stdDev() {
		return StdStats.stddev(this.stoTrials);
	}
	
	//calculating the low end of the 95% confidence level estimate for the percolation threshold
	public double confidenceLo() {
		return mean() - stdDev();
	}
	
	//calculating the high end of the 95% confidence level estimate level estimate for the percolation threshold
	public double confidenceHi() {
		return mean() + stdDev();
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in); //creating new scanner object to actively listen for input
		int n = in.nextInt(); //setting the n variable to the next entered integer input
		int T = in.nextInt(); //inputting the number of trials
		in.close(); //closing the input stream
		PercolationStats mC = new PercolationStats(n,T); //creating new PercolationStats object
		System.out.println("mean                    =" + mC.mean()); //outputting the mean value for that object
		System.out.println("stddev                  =" + mC.stdDev()); //outputting the stddev value for that object
		System.out.println("95% confidence interval = [" + mC.confidenceLo() + ", " + mC.confidenceHi() + "]"); //outputting the confidence interval for the prediction.
	}
}