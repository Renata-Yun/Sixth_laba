package lab_6;

public class Complex
{
    public double re, im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public void set(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public void addAssign(Complex other) {
        this.re += other.re;
        this.im += other.im;
    }

    public void subAssign(Complex other) {
        this.re -= other.re;
        this.im -= other.im;
    }

    public void negAssign() {
        this.re = -this.re;
        this.im = -this.im;
    }

    public void mulAssign(Complex other) {
        // c1*c2 = (re1*re2 - im1*im2) + (im1*re2 + re1*im2)i
        double re = this.re*other.re - this.im*other.im;
        double im = this.im*other.re + this.re*other.im;
        this.re = re;
        this.im = im;
    }

    public double sqrLength() {
        return this.re*this.re + this.im*this.im;
    }

    @Override
    public String toString() {
        return "lab4.Complex{" + re + " + " + im + "i}";
    }
}
