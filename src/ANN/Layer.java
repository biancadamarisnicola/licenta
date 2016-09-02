package ANN;

import java.io.IOException;

/**
 * Created by bianca on 19.08.2016.
 */
public class Layer {
    private int noOfNeurons;
    private Neuron neurons[];

    public Layer(int noOfNeurons, int noOfInputs) {
        neurons = new Neuron[noOfNeurons];
        this.noOfNeurons = noOfNeurons;
        for (int i = 0; i < noOfNeurons; i++)
            neurons[i] = new Neuron(noOfInputs);
    }

    public int getNoOfNeurons() {
        return noOfNeurons;
    }

    public void setNoOfNeurons(int noOfNeurons) {
        this.noOfNeurons = noOfNeurons;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void setNeurons(Neuron[] neurons) {
        this.neurons = neurons;
    }

    public Neuron getNeuron(int i){
        return neurons[i];
    }
}
