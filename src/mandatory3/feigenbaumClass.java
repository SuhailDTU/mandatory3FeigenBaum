package mandatory3;

public class feigenbaumClass {

    private double lambda = 1;
    private double out = 0.1;

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public void setOut(double out) {
        this.out = out;
    }

    public double[] feigenbaum() {

        out = lambda * out * (1 - out);

        return new double[] {out, lambda};
    }

    public boolean increaseLambda() {

        if (lambda < 4) {
            lambda += 0.01;
            out = 0.1;
            for (int i = 0; i < 500; i++) {
                out = lambda * out * (1 - out);
            }
            return true;
        }else {
            return false;
        }

    }
}
