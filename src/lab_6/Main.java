package lab_6;

import lab_6.fractals.Mandelbrot;

public class Main {

    public static void main(String[] args) {
        FractalGenerator gen = new Mandelbrot();

        FractalExplorer exp = new FractalExplorer(750, 750, gen);
        exp.createAndShowGui();
    }
}