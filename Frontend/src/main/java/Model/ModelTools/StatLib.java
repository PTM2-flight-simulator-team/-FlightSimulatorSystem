package Model.ModelTools;

import java.util.List;
import java.util.Vector;

public class StatLib {

    public static double max(Vector<Double> v) {
        double max = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) > max) {
                max = v.get(i);
            }
        }
        return max;
    }

    public static double min(Vector<Double> v) {
        double min = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) < min) {
                min = v.get(i);
            }
        }
        return min;
    }
    public static double avgZ(Vector<Double> x) {
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            sum += x.get(i);
        }
        return sum / x.size();
    }
    public static double stdZ(Vector<Double> x) {
        double avg = avgZ(x);
        double sum = 0;
        for (int i = 0; i < x.size(); i++) {
            sum += (x.get(i) - avg) * (x.get(i) - avg);
        }
        return Math.sqrt(sum / x.size());
    }
    // simple average
    public static float avg(float[] x) {
        float sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        float avg1 = (sum / x.length);
        return avg1;
    }

    // returns the variance of X and Y
    public static float var(float[] x) {
        float sum = 0;
        float average = avg(x);
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow((x[i] - average), 2);
        }
        float final_var = sum / x.length;
        return final_var;
    }

    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y) {

        float sum = 0;

        for (int i = 0; i < x.length; i++) {
            sum = sum + (x[i] - avg(x)) * (y[i] - avg(y));
        }
        float covariance = (sum / (x.length));
        return covariance;
    }

    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y) {
        float root_x = (float) Math.sqrt(var(x));
        float root_y = (float) Math.sqrt(var(y));
        float final_pear = cov(x, y) / (root_x * root_y);
        return final_pear;
    }

    // perform a linear regression and returns the line equation
    public static Line linear_reg(Point[] points) {
        float sum_x = 0;
        float[] _x = new float[points.length];
        float sum_y = 0;
        float[] _y = new float[points.length];
        for (int i = 0; i < points.length; i++) {
            sum_x += points[i].x;
            _x[i] = points[i].x;
            sum_y += points[i].y;
            _y[i] = points[i].y;
        }
        float a = cov(_x, _y) / var(_x);
        float b = (sum_y / points.length) - a * (sum_x / points.length);
        Line final_line = new Line(a, b);
        return final_line;

    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p, Point[] points) {
        Line equation = linear_reg(points);
        float distance = Math.abs(equation.f(p.x) - p.y);
        return distance;
    }

    // returns the deviation between point p and the line
    public static float dev(Point p, Line l) {
        float distance = Math.abs(l.f(p.x) - p.y);
        return distance;
    }
}