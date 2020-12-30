package lab_6.fractals;

import lab_6.Complex;
import lab_6.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class Mandelbrot implements FractalGenerator
{
    private final int MAX_ITERS = 2000;

    @Override
    public Rectangle2D.Double getInitialRange()
    {
        return new Rectangle2D.Double(-2.0, -1.5, 3.0, 3.0);
    }

    @Override
    public int numIterations(double x, double y) {
        Complex z = new Complex(0.0, 0.0);
        Complex c = new Complex(x, y);

        int iter = 0;
        do {
            z.mulAssign(z); // *z (z = z^2)
            z.addAssign(c); // +c (z = z^2 + c)
            iter += 1;
        } while(z.sqrLength() < 4.0 && iter < MAX_ITERS);

        if (iter == MAX_ITERS) {
            return -1;
        } else {
            return iter;
        }
    }
}
