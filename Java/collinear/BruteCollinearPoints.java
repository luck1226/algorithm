// Collinear java code
// by Chi-Ken Lu

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {
    
    private int nLine = 13, nSeg = 13;
    private int number_of_line = 0, number_of_segment = 0;
    
    private LineObject[] ln = new LineObject[nLine];
    private LineSegment[] seg = new LineSegment[nSeg];
     
    private class LineObject implements Comparable<LineObject> { 
        private Point head;
        private Point tail;
        public LineObject(Point head, Point tail) { 
            this.head = head;
            this.tail = tail;
        }
        private double slope() {
            return head.slopeTo(tail);
        }    
        public int compareTo(LineObject that) {        
            double slope1 = this.slope(), slope2 = that.slope();
            if (slope1 > slope2) return 1;
            if (slope1 < slope2) return -1;
            return 0;
        }
    }
    
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points  
        
        int N = points.length;
         
        // if (N == 0) { throw new IllegalArgumentException(); }
        
        for (int i = 0; i < N; i++) {
            if (points[i] == null) { throw new IllegalArgumentException(); }
            
            for (int j = i+1; j < N; j++) {
                if (points[j] == null) { throw new IllegalArgumentException(); }
                double s1 = points[i].slopeTo(points[j]);
                if (s1 == Double.NEGATIVE_INFINITY) { throw new IllegalArgumentException(); }
                
                for (int k = j+1; k < N; k++) {  
                    if (points[k] == null) { throw new IllegalArgumentException(); }
                    double s2 = points[j].slopeTo(points[k]);
                    if (s2 == Double.NEGATIVE_INFINITY) { throw new IllegalArgumentException(); }
                    
                    for (int l = k+1; l < N && s1 == s2; l++) {
                        if (points[l] == null) { throw new IllegalArgumentException(); }
                        double s3 = points[k].slopeTo(points[l]);
                        if (s3 == Double.NEGATIVE_INFINITY) { throw new IllegalArgumentException(); }
             
                        if (s2 == s3) {                              
                            Point[] pp = new Point[4];
                            pp[0] = points[i];
                            pp[1] = points[j];
                            pp[2] = points[k];
                            pp[3] = points[l];                         
                            Arrays.sort(pp);
                            if (number_of_line == nLine) { 
                                resize(2*nLine); 
                                nLine = 2*nLine; 
                            }
                            ln[number_of_line++] = new LineObject(pp[0], pp[3]);                  
                        }                             
                    }   
                }   
            }
        }    
        
        // after collecting all lines, below is for selecting 
        // only distinct line, and save them into LineSegment
        // StdOut.println("@ number of line " + number_of_line);
        
        if (number_of_line > 0) { 
            Arrays.sort(ln, 0, number_of_line); 
            seg[number_of_segment++] = new LineSegment(ln[0].head, ln[0].tail);
        }
        
        boolean first_time = true;
        for (int g = 1; g < number_of_line; g++) {    
            // StdOut.println(ln[g].head + " @ " + ln[g].tail);
            if (ln[g].slope() != ln[g-1].slope()) {
                
                if (first_time) { 
                    if (number_of_segment == nSeg) {
                        resizeSegment(2*nSeg);
                        nSeg = 2*nSeg;
                    }
                    seg[number_of_segment++] = new LineSegment(ln[g].head, ln[g].tail); 
                }
                else {
                    if ((number_of_segment+1) == nSeg) {
                        resizeSegment(2*nSeg);
                        nSeg = 2*nSeg;
                    }
                    seg[++number_of_segment] = new LineSegment(ln[g].head, ln[g].tail);
                    // first_time = true;
                }
            }
            else if (ln[g].slope() == ln[g].head.slopeTo(ln[g-1].head) || ln[g].slope() == ln[g].head.slopeTo(ln[g-1].tail)) 
            {
                Point[] qq = new Point[4];
                qq[0] = ln[g].head;
                qq[1] = ln[g].tail;
                qq[2] = ln[g-1].head;
                qq[3] = ln[g-1].tail;
                Arrays.sort(qq);
                if (first_time) { 
                    number_of_segment--;
                    first_time = false;
                }
                seg[number_of_segment] = new LineSegment(qq[0], qq[3]);
                ln[g] = new LineObject(qq[0], qq[3]);
            }
            else {
                if (first_time) { 
                    if (number_of_segment == nSeg) {
                        resizeSegment(2*nSeg);
                        nSeg = 2*nSeg;
                    }
                    seg[number_of_segment++] = new LineSegment(ln[g].head, ln[g].tail); 
                }
                else {
                    if ((number_of_segment+1) == nSeg) {
                        resizeSegment(2*nSeg);
                        nSeg = 2*nSeg;
                    }
                    seg[++number_of_segment] = new LineSegment(ln[g].head, ln[g].tail);
                    // first_time = true;
                }
            } 
        }
        if (number_of_line > 0) {
            int xx = 0;
            while (seg[xx] != null) { xx++; }
            number_of_segment = xx;
        }
    }
          
    private void resize(int capacity) {
        LineObject[] copy = new LineObject[capacity];
        for (int i = 0; i < nLine; i++) { copy[i] = ln[i]; }
        ln = copy;
    }
    
    private void resizeSegment(int capacity) {
        LineSegment[] copySeg = new LineSegment[capacity];
        for (int i = 0; i < nSeg; i++) { copySeg[i] = seg[i]; }
        seg = copySeg;
    }
    
    public int numberOfSegments() {       // the number of line segments
        return number_of_segment;
    }
   
    public LineSegment[] segments() {               // the line segments    
        LineSegment[] n_seg = new LineSegment[number_of_segment];
        for (int x = 0; x < number_of_segment; x++) {
            n_seg[x] = seg[x];
        }
        return n_seg;
    }
   
    public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        if (segment != null) {
            StdOut.println(segment);
            segment.draw();
        }
    }
    StdDraw.show();
   }
}