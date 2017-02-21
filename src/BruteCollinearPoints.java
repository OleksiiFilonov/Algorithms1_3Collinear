import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

	private final List<LineSegment> lineSegments;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		if (points == null) {
			throw new NullPointerException("The input points array is null");
		}

		lineSegments = new ArrayList<>();

		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < points.length; j++) {
				final double slopeToJ = calculateSlope(points, i, j);
				for (int k = j + 1; k < points.length; k++) {
					final double slopeToK = calculateSlope(points, i, k);
					if (slopeToJ == slopeToK) {
						for (int l = k + 1; l < points.length; l++) {
							final double slopeToL = calculateSlope(points, i, l);
							if (slopeToJ == slopeToL) {
								// sort the array and create LineSegment
								final List<Point> collinearPoints = new ArrayList<>(4);
								collinearPoints.add(points[i]);
								collinearPoints.add(points[j]);
								collinearPoints.add(points[k]);
								collinearPoints.add(points[l]);
								sort(collinearPoints);
								lineSegments.add(new LineSegment(min(collinearPoints), max(collinearPoints)));
								break;
							}
						}
					}
				}
			}
		}
	}

	private double calculateSlope(Point[] points, int i, int j) {
		final double slopeToJ = points[i].slopeTo(points[j]);
		if (Double.NEGATIVE_INFINITY == slopeToJ)
			throw new IllegalArgumentException("duplication points: " + points[i]);
		return slopeToJ;
	}

	// the number of line segments
	public int numberOfSegments() {
		return lineSegments.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return lineSegments.toArray(new LineSegment[] {});
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
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}

}
