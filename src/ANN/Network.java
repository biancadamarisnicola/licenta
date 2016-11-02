package ANN;

import java.io.IOException;

/**
 * Created by bianca on 19.08.2016.
 */
public class Network {
    private int noOfInputs;
    private int noOfOutput;
    private int noOfHiddenLayers;
    private Layer layers[];
    private double epsilon;
    private int noEpoch;
    private double learningRate = 0.001;


    public Network(int noInput, int noOutput, int noHidden, int noNeurons, double epsilon, int noEpoch) throws IOException {
        noOfInputs = noInput;
        noOfOutput = noOutput;
        noOfHiddenLayers = noHidden;
        this.epsilon = epsilon;
        this.noEpoch = noEpoch;
        int noOfNeuronLayer = noNeurons;
        layers = new Layer[noOfNeuronLayer + 2];
        layers[0] = new Layer(noOfInputs, 0);
        layers[1] = new Layer(noOfNeuronLayer, noOfInputs);
        for (int i = 2; i < noOfHiddenLayers + 1; i++) {
            layers[i] = new Layer(noOfNeuronLayer, noOfNeuronLayer);
        }
        layers[noOfHiddenLayers + 1] = new Layer(noOfOutput, noOfNeuronLayer);
    }

    public void activate(double input[]) throws IOException {
        //pt primul layer, datele sunt inputul problemei
        for (int i = 0; i < noOfInputs; i++) {
            layers[0].getNeuron(i).setOutput(input[i]);
        }
        //pentru restul layerelor(ascunse+output)
        for (int l = 1; l <= noOfHiddenLayers + 1; l++) {
            // Pt fiecare neuron de pe layer
            for (int j = 0; j < layers[l].getNeurons().length; j++) {
                double[] info = new double[layers[l].getNeuron(j).getNoOfInputs()];
                // primeste ca informatii ptr fire toate outputurile
                // neuronilor de pe layerul precedent
                for (int i = 0; i < layers[l].getNeuron(j).getNoOfInputs(); i++) {
                    info[i] = layers[l - 1].getNeuron(i).getOutput();
                }
                //System.out.println("Layer"+l+"neuron"+j);
                layers[l].getNeuron(j).fire(info);
            }
        }
    }

    public void errorBackprop(double[] error) {
        //incepem de la final
        for (int l = noOfHiddenLayers + 1; l >= 1; l--) {
            //pt fiecare layer parcurg neuronii
            for (int i = 0; i < layers[l].getNeurons().length; i++) {
                if (l == noOfHiddenLayers + 1) {
                    //suntem pe layerul de output
                    layers[l].getNeuron(i).setError(error[i]);
                    //System.out.println("Layer "+l+" neuron "+i+" err "+error[i]);
                } else {
                    //layere intermediare, se calculeaza eroarea
                    double errorSum = 0.0;
                    for (int k = 0; k < layers[l + 1].getNeurons().length; k++) {
                        errorSum += layers[l + 1].getNeuron(k).getError() * layers[l + 1].getNeuron(k).getWeight(k);
                    }
                    //setam eroarea pe layer
                    layers[l].getNeuron(i).setError(errorSum);
                    //System.out.println(layers[l].getNeuron(i).toString());
                    //System.out.println(errorSum);
                    //System.out.print("Layer "+l+" neuron "+i+" err "+error[i]);
                }
                //se seteaza ponderile
                double netWeigth = 0.0;
                for (int j = 0; j < layers[l].getNeuron(i).getNoOfInputs(); j++) {
                    double error2 = layers[l].getNeuron(i).getError();
                    learningRate = getNewLearningRate(error2);
                    netWeigth = layers[l].getNeuron(i).getWeight(j) + learningRate * layers[l].getNeuron(i).getError() * layers[l - 1].getNeuron(j).getOutput();
                    //netWeigth = normalize(netWeigth);
                    layers[l].getNeuron(i).setWeight(j, netWeigth);
                    //System.out.println(netWeigth);
                }
                normalize(layers[l].getNeuron(i));
                //System.out.println();
            }
        }
    }

    private double getNewLearningRate(double error2) {
        if (error2 > 300) {
            return 0.1;
        }
        if (error2 > 100) {
            return 0.01;
        }
        return 0.001;
    }

    private void normalize(Neuron neuron) {
        double maxWeigth = 0.0;
        double minWeigth = 0.0;
        for (int j = 0; j < neuron.getWeights().length; j++) {
            if (neuron.getWeight(j) > maxWeigth) {
                maxWeigth = neuron.getWeight(j);
            }
            if (neuron.getWeight(j) < 0.0) {
                neuron.setWeight(j, 0.05);
            }
        }
        if (maxWeigth < Math.abs(minWeigth))
            maxWeigth = minWeigth;
        while (maxWeigth > 0.2 || maxWeigth < -0.2) {
            for (int j = 0; j < neuron.getWeights().length; j++) {
                double n = neuron.getWeight(j);
                neuron.setWeight(j, n / 10);
            }
            maxWeigth = maxWeigth / 10;
        }
    }

    public double errorCompRegression(double error[], double target[]) {
        double totalErr = 0.0;
        for (int i = 0; i < layers[noOfHiddenLayers + 1].getNeurons().length; i++) {
            error[i] = target[i] - layers[noOfHiddenLayers + 1].getNeuron(i).getOutput();
            totalErr += error[i] * error[i];
            //System.out.println(target[i]+"  "+layers[noOfHiddenLayers + 1].getNeuron(i).getOutput()+"  "+totalErr);
        }
        return totalErr;
    }

    public boolean checkError(double error[]) {
        double err = 0;
        for (int i = 0; i < error.length; i++) {
            err += error[i];
        }
        if (Math.abs(err - 0.1) < epsilon) {
            return true;
        }
        return false;
    }

    public void learn(double input[][], double output[][]) throws IOException {
        boolean stop = false;
        int epoch = 0;
        double globalErr[];
        while (!stop && epoch < noEpoch) {
            globalErr = new double[input.length];
            for (int i = 0; i < input.length; i++) {
                //System.out.println("@@@@@ouput "+i+ "values"+output[i][0]+" "+output[i][1]+" "+output[i][2]+"@@@@@@@");
                activate(input[i]);
                //activam neuronii si propagam semnalul in retea
                double error[] = new double[noOfOutput];
                globalErr[i] = errorCompRegression(error, output[i]);
                //System.out.println("Output "+ output[i].toString()+ "error"+ error.toString());
                //System.out.println("Global err "+ globalErr[i]);
                errorBackprop(error);
            }
            stop = checkError(globalErr);
            epoch++;
            // System.out.println(epoch);
        }
    }

    public Layer getLayer(int i) {
        return this.layers[i];
    }

    public void test(double input[][], double output[][]) throws IOException {
        System.out.println("@@@@@@@@@@@@@TESTING@@@@@@@@@@@@@@@@@@@@@");
        for (int d = 0; d < input.length; d++) {
            int noOfHidden = 1;
            int noOfNeuronsPerLayer = 3;
            Network networkTest = new Network(7, 3, noOfHidden, noOfNeuronsPerLayer, 0.7, 100);
            networkTest.activate(input[d]);
            Layer actualOutput = networkTest.getLayer(2);
            System.out.println("Expected output: SLUMP(cm): " + output[d][0] + "  FLOW(cm): " + output[d][1] + "  Compressive Strength (28-day): " + output[d][2]);
            System.out.println("Actual output:   SLUMP(cm): " + actualOutput.getNeuron(0).getOutput() + "  FLOW(cm): " + actualOutput.getNeuron(1).getOutput() + "  Compressive Strength (28-day): " + actualOutput.getNeuron(2).getOutput());
        }
    }
}
