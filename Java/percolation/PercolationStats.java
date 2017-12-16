// percolation code
// developed by Chi-Ken Lu
// Start Date Nov. 8th 2016
//

//import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.Scanner;
//import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
   
   private double[] x; 
   private int[] randsite;
   private Percolation perco;
   private int N,T; 
   
   public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
   {
       if (n <= 0 || trials <= 0) throw new IllegalArgumentException(); 
       
       randsite = new int[n*n];
       
       x = new double[trials];
           
       N = n;
       T = trials;
       
       for (int i = 0; i < trials; i++)
       {
           int nsite;
           double avg;
           perco = new Percolation(n); 
            
           for (int k = 0; k < n*n; k++)           
               randsite[k] = k;
           StdRandom.shuffle(randsite);
           
           for (int k = 0; k < n; k++)
           {
               int index = randsite[k];
               int row,col;
               col = index % n + 1;
               row = (index-col+1)/n + 1;                
               perco.open(row,col);          
           } 
           nsite = n;
           while (!perco.percolates())
           {
               int index = randsite[nsite];
               int row,col;
               col = index % n + 1;
               row = (index-col+1)/n + 1; 
               perco.open(row,col);
               nsite = nsite+1;          
           }         
            x[i] = 1.0*nsite/N/N;
       }      
   }
   
   public double mean() { return StdStats.mean(x); }                          // sample mean of percolation threshold
   
   public double stddev() { return StdStats.stddev(x); }                        // sample standard deviation of percolation threshold
   
   public double confidenceLo() {
       double avg = StdStats.mean(x);
       double ss = StdStats.stddev(x);
       double Lo = avg - 1.96*ss/Math.sqrt(T);
       return Lo;
   }                  // low  endpoint of 95% confidence interval
   
   public double confidenceHi() {
       double avg = StdStats.mean(x);
       double ss = StdStats.stddev(x);
       double Hi = avg + 1.96*ss/Math.sqrt(T);
       return Hi;
   }                 // high endpoint of 95% confidence interval

   public static void main(String[] args)    // test client (described below)
   {
       int[] z;
       z = new int[2];
       int i=0;
       
       for (String s: args) {
           z[i] = Integer.parseInt(s);
           i++;
        }
       int g = z[0];
       int p = z[1];
        
       PercolationStats temPercoS = new PercolationStats(g,p);
       StdOut.println("mean                    = " + temPercoS.mean());
       StdOut.println("stddev                  = " + temPercoS.stddev());
       StdOut.println("95% confidence interval = " + temPercoS.confidenceLo() + ", " + temPercoS.confidenceHi());
        
   }
   
}