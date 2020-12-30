package lab_6.fractals;

import lab_6.Complex;
import lab_6.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class BurningShip implements FractalGenerator
{
    @Override
    public Rectangle2D.Double getInitialRange() {
        return new Rectangle2D.Double(-2.0, -2.5, 4.0, 4.0);
    }

    private final int MAX_ITERS = 2000;
    @Override
    public int numIterations(double x, double y) {
        Complex z = new Complex(0.0, 0.0);
        Complex c = new Complex(x, y);

        int iter = 0;
        do {
            double absRe = Math.abs(z.re);
            double absIm = Math.abs(z.im);
            z.set(absRe, absIm); // z = z' = |z.re| + i|z.im|
            z.mulAssign(z); // *z (z = z'^2)
            z.addAssign(c); // +c (z = z'^2 + c)
            iter += 1;
        } while(z.sqrLength() < 4.0 && iter < MAX_ITERS);

        if (iter == MAX_ITERS) {
            return -1;
        } else {
            return iter;
        }
    }
}
