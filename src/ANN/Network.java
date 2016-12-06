package ANN;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by bianca on 19.08.2016.
 */
public class Network {
    private int noOfInputs;
    private int noOfOutput;
    private int noOfHiddenLayers;
    private Layer layers[];
    private Fraction epsilon;
    private int noEpoch;
    private Fraction learningRate = new Fraction().valueOf(0.01);


    public Network(int noInput, int noOutput, int noHidden, int noNeurons, Fraction epsilon, int noEpoch) throws IOException {
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

    public void activate(Fraction input[]) throws IOException {
        //pt primul layer, datele sunt inputul problemei
        for (int i = 0; i < noOfInputs; i++) {
            layers[0].getNeuron(i).setOutput(input[i]);
        }
        int l=1;
        //pentru restul layerelor(ascunse)
        for (l = 1; l < noOfHiddenLayers + 1; l++) {
            // Pt fiecare neuron de pe layer
            for (int j = 0; j < layers[l].getNeurons().length; j++) {
                Fraction[] info = new Fraction[layers[l].getNeuron(j).getNoOfInputs()];
                // primeste ca informatii ptr fire toate outputurile
                // neuronilor de pe layerul precedent
                for (int i = 0; i < layers[l].getNeuron(j).getNoOfInputs(); i++) {
                    info[i] = layers[l - 1].getNeuron(i).getOutput();
                }
                layers[l].getNeuron(j).fireSigmoidal(info);
                //System.out.println("Layer "+l+" neuron "+j+" -> "+layers[l].getNeuron(j).toString());
            }
        }
        if (l == noOfHiddenLayers+1){
            for (int j = 0; j < layers[l].getNeurons().length; j++) {
                Fraction[] info = new Fraction[layers[l].getNeuron(j).getNoOfInputs()];
                // primeste ca informatii ptr fire toate outputurile
                // neuronilor de pe layerul precedent
                for (int i = 0; i < layers[l].getNeuron(j).getNoOfInputs(); i++) {
                    info[i] = layers[l - 1].getNeuron(i).getOutput();
                }
                layers[l].getNeuron(j).fireSoftPlus(info);
                //System.out.println("Layer "+l+" neuron "+j+" -> "+layers[l].getNeuron(j).toString());
            }
        }
    }

    public void errorBackpropagation(Fraction[] error) {
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
                    Fraction errorSum = new Fraction();
                    for (int k = 0; k < layers[l + 1].getNeurons().length; k++) {
                        //err[h] = output[h]*(1-output[h])*suma(wkh*err[k])
                        errorSum =
                                errorSum.add(layers[l + 1].getNeuron(k).getError().mul(layers[l + 1].getNeuron(k).getWeight(k)));
                    }
                    //errorSum = errorSum.mul(layers[l].getNeuron(i).getOutput().mul(new Fraction().valueOf(1).sub(layers[l].getNeuron(i).getOutput())));
                    //setam eroarea pe layer
                    layers[l].getNeuron(i).setError(errorSum);
                    //System.out.println(layers[l].getNeuron(i).toString());
                    //System.out.println(errorSum);
                    //System.out.print("Layer "+l+" neuron "+i+" err "+error[i]);
                }

                //se seteaza ponderile
                Fraction netWeigth = new Fraction();
                Fraction sigmai = new Fraction();
                Fraction xij = new Fraction();
                for (int j = 0; j < layers[l].getNeuron(i).getNoOfInputs(); j++) {
                    Fraction error2 = layers[l].getNeuron(i).getError();
                    learningRate = getNewLearningRate(error2);
                    //wji=wji+learningRate*error[i]*outout[j,i]
                    sigmai = layers[l].getNeuron(i).getError();
                    xij = layers[l - 1].getNeuron(j).getOutput();
                    netWeigth = layers[l].getNeuron(i).getWeight(j).add(learningRate.mul(sigmai.mul(xij)));
                    //netWeigth = normalize(netWeigth);
                    layers[l].getNeuron(i).setWeight(j, netWeigth);
                    //System.out.println(netWeigth);
                }
                normalize(layers[l].getNeuron(i));
                //System.out.println();
            }
        }
    }

    private Fraction getNewLearningRate(Fraction error2) {
        if (error2.greaterThan(new Fraction(BigInteger.valueOf(300)))) {
            return new Fraction(BigInteger.ONE, BigInteger.TEN);
        }
        if (error2.greaterThan(new Fraction(BigInteger.valueOf(300)))) {
            return new Fraction(BigInteger.ONE, BigInteger.valueOf(100));
        }
        return new Fraction(BigInteger.ONE, BigInteger.valueOf(1000));
    }

    private void normalize(Neuron neuron) {
        double maxWeigth = 0.0;
        double minWeigth = 0.0;
        for (int j = 0; j < neuron.getWeights().length; j++) {
//            if (neuron.getWeight(j) > maxWeigth) {
//                maxWeigth = neuron.getWeight(j);
//            }
//            if (neuron.getWeight(j) < 0.0) {
//                neuron.setWeight(j, 0.05);
//            }
        }
        if (maxWeigth < Math.abs(minWeigth))
            maxWeigth = minWeigth;
        while (maxWeigth > 0.2 || maxWeigth < -0.2) {
            for (int j = 0; j < neuron.getWeights().length; j++) {
                //double n = neuron.getWeight(j);
                //neuron.setWeight(j, n / 10);
            }
            maxWeigth = maxWeigth / 10;
        }
    }

    public Fraction errorCompRegression(Fraction error[], Fraction target[]) {
        Fraction totalErr = new Fraction();
        for (int i = 0; i < layers[noOfHiddenLayers + 1].getNeurons().length; i++) {
            //tkd-okd
            //System.out.println("Target "+target[i]+" output "+layers[noOfHiddenLayers + 1].getNeuron(i).getOutput());
            error[i] = target[i].sub(layers[noOfHiddenLayers + 1].getNeuron(i).getOutput());
            //1/2*(tkd-okd)^2
            totalErr = totalErr.add(error[i].mul(error[i]));
            //System.out.println(target[i]+"  "+layers[noOfHiddenLayers + 1].getNeuron(i).getOutput()+"  "+totalErr);
        }
        totalErr = totalErr.div(new Fraction().valueOf(2));
        return totalErr;
    }

    public boolean checkError(Fraction error[]) {
        Fraction err = new Fraction(BigInteger.ZERO);
        for (int i = 0; i < error.length; i++) {
            err.add(error[i]);
        }
        Fraction zPo = new Fraction(BigInteger.ONE, BigInteger.TEN);
        //eroarea in modul < epsilon
        if (!err.sub(zPo).greaterThan(epsilon) && !(new Fraction(BigInteger.ZERO).sub(zPo)).greaterThan(epsilon)) {
            return true;
        }
        return false;
    }

    public void learn(Fraction input[][], Fraction output[][]) throws IOException {
        boolean stop = false;
        int epoch = 0;
        Fraction globalErr[];
        while (!stop && epoch < noEpoch) {
            //training
            int noTrainingExamples = 2*input.length/3;
            globalErr = new Fraction[noTrainingExamples];
            for (int i = 0; i < noTrainingExamples; i++) {
                //System.out.println("@@@@@ouput "+i+ "values"+output[i][0]+" "+output[i][1]+" "+output[i][2]+"@@@@@@@");
                activate(input[i]);
                //activam neuronii si propagam semnalul in retea
                Fraction error[] = new Fraction[noOfOutput];
                globalErr[i] = errorCompRegression(error, output[i]);
                //System.out.println("Output "+ output[i].toString()+ "error"+ error.toString());
                //System.out.println("Global err i="+i+" "+globalErr[i].toDouble());
                errorBackpropagation(error);
            }
            //validation
            for (int i = noTrainingExamples; i < input.length-1; i++) {
                //System.out.println("@@@@@ouput "+i+ "values"+output[i][0]+" "+output[i][1]+" "+output[i][2]+"@@@@@@@");
                activate(input[i]);
            }
            stop = checkError(globalErr) || validateWeights(input, output, noTrainingExamples, input.length-1);
            epoch++;
            // System.out.println(epoch);
        }
    }

    private boolean validateWeights(Fraction[][] input, Fraction[][] output, int begin, int end) throws IOException {
        for (int i = begin; i < end; i++) {
            activate(input[i]);
            if (!input[i][noOfHiddenLayers].sub(output[i][noOfHiddenLayers]).greaterThan(new Fraction(BigInteger.ONE).valueOf(10))){
                System.out.println("!!!!!");
                return true;
            }
            if (!output[i][noOfHiddenLayers].sub(input[i][noOfHiddenLayers]).greaterThan(new Fraction(BigInteger.ONE).valueOf(10))){
                System.out.println("??????");
                return true;
            }
        }
        return false;
    }

    public Layer getLayer(int i) {
        return this.layers[i];
    }

    public void test(Fraction input[][], Fraction output[][]) throws IOException {
        System.out.println("@@@@@@@@@@@@@TESTING@@@@@@@@@@@@@@@@@@@");
        for (int d = 0; d < input.length; d++) {
            int noOfHidden = 1;
            int noOfNeuronsPerLayer = 3;
            Network networkTest = new Network(noOfInputs, noOfOutput, noOfHidden, noOfNeuronsPerLayer, new Fraction(BigInteger.ZERO).valueOf(0.001), 100);
            networkTest.activate(input[d]);
            Layer actualOutput = networkTest.getLayer(2);
            System.out.println("Expected output: "+ String.valueOf(output[d][0].mul(new Fraction().valueOf(20000))));
            System.out.println("Actual output: "+ String.valueOf(actualOutput.getNeuron(0).getOutput().mul(new Fraction().valueOf(20000))));
            System.out.println("__________________________________________________");
        }
    }

    private int size() {
        return noOfHiddenLayers+2;
    }
}
