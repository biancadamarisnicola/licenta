package ANN;

import java.util.Arrays;

/**
 * Created by bianca on 19.08.2016.
 */
public class Neuron {

    //constants
    private final double MIN_WEIGHT = -1;
    private final double MAX_WEIGHT = 1;
    private final int RAND_MAX = 32767;

    //fields
    private int noOfInputs;
    private Double weights[];
    private double output, error;

    public Neuron(int n) {
        noOfInputs = n;
        weights = new Double[noOfInputs];
        for (int i = 0; i < n; i++) {
            weights[i] = MIN_WEIGHT +
                    (Math.random() * RAND_MAX) / (RAND_MAX + 1) * (MAX_WEIGHT - MIN_WEIGHT);
        }
        output = 0.0;
        error = 0.0;
    }

    //neuron activation, calculate output
    public double fire(double[] info) {
        double net = 0.0;
        for (int i = 0; i < noOfInputs; i++) {
            net += info[i] * weights[i];
        }
        //linear function
        this.output = net;
        //System.out.println(net);
        //sigmoidal function
        //this.output = (1.0/(1.0+Math.pow(Math.E, -net)));
        //System.out.println(net+ "---" +output);
        return this.output;
    }

    public int getNoOfInputs() {
        return noOfInputs;
    }

    public void setNoOfInputs(int noOfInputs) {
        this.noOfInputs = noOfInputs;
    }

    public Double[] getWeights() {
        return weights;
    }

    public void setWeights(Double[] weights) {
        this.weights = weights;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getWeight(int k) {
        return weights[k];
    }

    public void setWeight(int k, double netWeigth) {
        this.weights[k] = netWeigth;
    }

    public void setErr(double val){
        //linear function
        //this.error = val;
        //sigmoidal function
        this.error = this.output*(1-this.output)*val;
    }

    @Override
    public String toString() {
        return "Neuron{" +
                ", noOfInputs=" + noOfInputs +
                ", weights=" + Arrays.toString(weights) +
                ", output=" + output +
                ", error=" + error +
                '}';
    }
}
