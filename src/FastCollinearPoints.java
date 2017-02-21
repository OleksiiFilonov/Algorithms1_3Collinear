import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

	private final List<LineSegment> lineSegments;

	// finds all line segments containing 4 or more points
	public FastCollinearPoints(final Point[] points) {
		if (points == null) {
			throw new NullPointerException("The input points array is null");
		}
		lineSegments = new ArrayList<>();
		for (int i = 0; i < points.length - 3; i++) {
			Point pivot = points[i];
			Point[] copy = Arrays.copyOf(points, points.length);
			Arrays.sort(copy, i + 1, copy.length, pivot.slopeOrder());
			List<Point> collinear = new LinkedList<>();
			double previousSlope = 0.00;
			for (int j = i; j < copy.length; j++) {
				double slopeToCurrent = pivot.slopeTo(copy[j]);
				if (previousSlope == slopeToCurrent) {
					collinear.add(copy[j]);
				} else {
					previousSlope = slopeToCurrent;
					if (collinear.size() > 2) {
						collinear.add(pivot);
						addSegment(collinear);
						collinear.clear();
					} else {
						collinear.clear();
						collinear.add(copy[j]);
					}
				}
			}
			if (collinear.size() > 2) {
				collinear.add(pivot);
				addSegment(collinear);
			}
		}
	}

	private void addSegment(List<Point> collinear) {
		sort(collinear);
		lineSegments.add(new LineSegment(min(collinear), max(collinear)));
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
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
