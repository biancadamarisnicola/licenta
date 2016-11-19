package ANN;

import java.util.Arrays;
import java.util.Random;

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
    private Fraction weights[];
    private Fraction output, error;
    private Fraction bias;

    public Neuron(int n) {
        noOfInputs = n;
        initWeights(n);
        output = new Fraction();
        error = new Fraction();
    }

    private void initWeights(int noOfInputs) {
        weights = new Fraction[noOfInputs];
        Random random = new Random();
        for (int i = 0; i < noOfInputs; i++) {
//            weights[i] = MIN_WEIGHT +
//                    (Math.random() * RAND_MAX) / (RAND_MAX + 1) * (MAX_WEIGHT - MIN_WEIGHT);
            weights[i] = new Fraction().valueOf(random.nextGaussian() * 0.8).normalize();
        }
        bias = new Fraction().valueOf(random.nextGaussian() * 0.8).normalize();
    }

    //neuron activation, calculate output
    public Fraction fireSoftPlus(Fraction[] info) {
        Fraction net = new Fraction();
        for (int i = 0; i < noOfInputs; i++) {
            net = net.add(info[i].mul(weights[i]));
        }
        //Soft plus function
        double fx = Math.log(1+Math.pow(Math.E, net.toDouble()));
        this.output = new Fraction().valueOf(fx);
        return this.output;
    }

    public Fraction fireSigmoidal(Fraction[] info) {
        Fraction net = new Fraction();
        net = net.add(bias);
        for (int i = 0; i < noOfInputs; i++) {
            net = net.add(info[i].mul(weights[i]));
        }
        //Sigmodial function
        double fx =(1.0/(1.0+Math.pow(Math.E, -net.toDouble())));
        this.output = new Fraction().valueOf(fx);
        return new Fraction().valueOf(fx);
    }

    public int getNoOfInputs() {
        return noOfInputs;
    }

    public void setNoOfInputs(int noOfInputs) {
        this.noOfInputs = noOfInputs;
    }

    public Fraction[] getWeights() {
        return weights;
    }

    public void setWeights(Fraction[] weights) {
        this.weights = weights;
    }

    public Fraction getOutput() {
        return output;
    }

    public void setOutput(Fraction output) {
        this.output = output;
    }

    public Fraction getError() {
        return error;
    }

    public void setError(Fraction error) {
        this.error = error;
    }

    public Fraction getWeight(int k) {
        return weights[k];
    }

    public void setWeight(int k, Fraction netWeigth) {
        this.weights[k] = netWeigth;
    }

    public void setErr(Fraction val){
        //linear function
        this.error = val;
        //sigmoidal function
        //this.error = this.output*(1-this.output)*val;
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
