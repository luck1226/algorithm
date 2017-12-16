// percolation code
// developed by Chi-Ken Lu
// Start Date Nov. 8th 2016
//

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   
   private WeightedQuickUnionUF uUFf; 
   private int N;
   private int[] site;
   
   public Percolation(int n)                // create n-by-n grid, with all sites blocked
   {
       if ( n <= 0 ) throw new IllegalArgumentException();    
       uUFf = new WeightedQuickUnionUF(n*n+2);
       site = new int[n*n];
       N = n;
   }
       
   public void open(int row, int col)       // open site (row, col) if it is not open already
   {
       if (row <= 0 || row > N) throw new IndexOutOfBoundsException("row index out of bounds");     
       if (col <= 0 || col > N) throw new IndexOutOfBoundsException("column index out of bounds");
       
       int i,index;
       i = (row-1)*N+col-1;
       index = i+1;
       site[i]=1;
       
       if (N == 1) { uUFf.union(0,1); uUFf.union(1,2); }
       
       if (row == 1 && N > 1)
       {
           uUFf.union(0,index);
           if (col == 1)
           {
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
           }
           if (col == N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
           }
           if (col > 1 && col < N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
           }
       }
       if (row == N && N > 1)
       {
           uUFf.union(N*N+1,i+1);
                     
           if (col == 1)
           {
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
           if (col == N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
           if (col > 1 && col < N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
       }
       if (row > 1 && row < N && N > 1)
       {           
           if (col == 1)
           {
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
           if (col == N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
           if (col > 1 && col < N)
           {
               if (site[i]*site[i-1] != 0) { uUFf.union(index,index-1); }
               if (site[i]*site[i+1] != 0) { uUFf.union(index,index+1); }
               if (site[i]*site[i+N] != 0) { uUFf.union(index,index+N); }
               if (site[i]*site[i-N] != 0) { uUFf.union(index,index-N); }
           }
       }      
   }
       
   public boolean isOpen(int row, int col)  // is site (row, col) open?
   {
       if (row <= 0 || row > N) throw new IndexOutOfBoundsException("row index out of bounds");     
       if (col <= 0 || col > N) throw new IndexOutOfBoundsException("column index out of bounds");
       
       int i;
       i = (row-1)*N+col-1;
       return (site[i] == 1);
   }
       
   public boolean isFull(int row, int col)  // is site (row, col) full?
   {
       if (row <= 0 || row > N) throw new IndexOutOfBoundsException("row index out of bounds");     
       if (col <= 0 || col > N) throw new IndexOutOfBoundsException("column index out of bounds");      
 
       if ( !percolates() )
           return uUFf.connected(0,(row-1)*N+col);
       else 
       {
           boolean flag = false; // to denote whether the site is connected to the bottom row
           int i = 0;   
           
           while (i < N || flag)  // to test whether it is connected to the virtual LAST site
           {
               if (site[N*(N-1)+i] != 0) 
               {
                   if ( uUFf.connected((row-1)*N+col,N*(N-1)+i+1) ) 
                   {
                       flag = true;
                   }
               }
               i++;
           }    
           
           if (!flag)
           {   
               return uUFf.connected(0,(row-1)*N+col);
           } else {
               return false;
           }
           
       }
   }
       
   public boolean percolates()  // does the system percolate?
   {
       return uUFf.connected(0,N*N+1);
   }
       
   public static void main(String[] args)   // test client (optional)
   {
       int g = StdIn.readInt();
       Percolation tempPerco = new Percolation(g);
       while (!StdIn.isEmpty()) 
       {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            tempPerco.open(p,q);
        }
       StdOut.println(tempPerco.percolates());
   }
}
