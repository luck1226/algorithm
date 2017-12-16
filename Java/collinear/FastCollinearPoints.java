// Collinear java code
// by Chi-Ken Lu

// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {
    
    private int nLine = 13, nSeg = 13;
    private int number_of_segment = 0, number_of_line = 0;
    private LineSegment[] seg = new LineSegment[nSeg];
    private LineObject[] ln = new LineObject[nLine];
  
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
            else if (slope1 < slope2) return -1;
            else return this.head.compareTo(that.head);           
        }
    }
    
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        
        int N = points.length;
        Point[] ypoints = new Point[N];
        ypoints = points.clone();  // this step is crucially important; array in java act like pointer in C; care needed.
        Arrays.sort(ypoints);
        
        for (int i = 0; i < N; i++) {     
            Arrays.sort(points, ypoints[i].slopeOrder());
            if (N > 1) {  
                if (points[1].slopeTo(ypoints[i]) == Double.NEGATIVE_INFINITY) 
                { throw new IllegalArgumentException(); } 
            }  
            for (int j = 1; j < (N-1); j++) {             
                double s0 = points[j].slopeTo(ypoints[i]);  
                double s1 = points[j+1].slopeTo(ypoints[i]);
                int count = 0;
                int j0 = j;
                while ((s0 == s1) && j+1 < N) { 
                    s0 = s1;
                    if ((j+2) < N) { s1 = points[j+2].slopeTo(ypoints[i]); }
                    j++;
                    count++;
                }                    
                if (count > 1) {
                    Point[] pp = new Point[count+2];
                    pp[0] = ypoints[i];
                    for (int k = 1; k < count+2; k++) { pp[k] = points[j0+k-1]; }
                    Arrays.sort(pp);
                    if (number_of_line == nLine) {         
                        resize(2*nLine); 
                        nLine = 2*nLine;         
                    }
                    ln[number_of_line++] = new LineObject(pp[0], pp[count+1]);
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
        // Point[] ypoints = new Point[n];
    
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            // ypoints[i] = new Point(x, y);
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
    
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            if (segment != null) {
                StdOut.println(segment);
                segment.draw();
            }
    
        }
        StdDraw.show();
    }

}
