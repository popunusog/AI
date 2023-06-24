import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    private final double constA = 0.04;
    private List<Double> vectorWeight;
    private double t;

    public Perceptron(int vectorSize) {
        this.vectorWeight = new ArrayList<>();
        for (int i = 0; i < vectorSize; i++)
            this.vectorWeight.add(1.0);

        this.t = 1.0;
    }

    public void normalize() {
        double sum = 0;

        for (double wectorElement : this.vectorWeight)
            sum += Math.pow(wectorElement, 2.0);
        double normalizedV = sum;
        normalizedV = Math.sqrt(normalizedV);

        List<Double> OutputvectorW = new ArrayList<>();

        for (int i = 0; i < this.vectorWeight.size(); i++)
            OutputvectorW.add(i, (this.vectorWeight.get(i) / normalizedV));
        //NIE
        this.t = t / normalizedV;
        this.vectorWeight = OutputvectorW;
    }

    //W’=W+(d-y)𝛼X
    //t’=t-(d-y)𝛼
    public void learn(double Dataarr[], int trueValue) {

        double scalar = 0.0;
        for (int i = 0; i < Dataarr.length; i++)
            scalar += Dataarr[i] * this.vectorWeight.get(i);

        int estimatedValue;
        if (scalar >= this.t) {
            estimatedValue = 1;
        } else {
            estimatedValue = 0;
        }
        if (estimatedValue != trueValue) {
            List<Double> vectorWPrime = new ArrayList<>(this.vectorWeight);
            for (int i = 0; i < Dataarr.length; i++)
                vectorWPrime.set(i, (this.vectorWeight.get(i) + ((trueValue - estimatedValue) * constA * Dataarr[i])));

            this.vectorWeight = vectorWPrime;
            this.t = t + (trueValue - estimatedValue) * constA * -1;
        }
    }

    public double judge(double Dataarr[]) {
        double scalar = 0;
        for (int i = 0; i < Dataarr.length; i++)
            scalar += Dataarr[i] * this.vectorWeight.get(i);

        return (scalar - this.t);
    }


}

