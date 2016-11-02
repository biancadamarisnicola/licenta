package ANN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by bianca on 19.08.2016.
 */
public class Controller {
    private double input[][], output[][];
    private double inputTest[][], outputTest[][];
    private int noOfExample, noOfExampleTest;
    private int noOfOuputs, noOfOuputsTest;
    private int noOfFeatures, noOfFeaturesTest;
    private int noEpoch;
    private double epsilon;

    public Controller(int noEpoch, double epsilon) throws IOException, ParseException {
        readInputData();
        this.noEpoch = noEpoch;
        this.epsilon = epsilon;
    }

    private void readInputData() throws IOException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);
        noOfExample = Integer.parseInt(br.readLine());
        noOfFeatures = Integer.parseInt(br.readLine());
        noOfOuputs = Integer.parseInt(br.readLine());
        input = new double[noOfExample+1][noOfFeatures];
        output = new double[noOfExample+1][noOfOuputs];
        FileReader fr1 = new FileReader("dataTest.txt");
        BufferedReader br1 = new BufferedReader(fr1);
        noOfExampleTest = Integer.parseInt(br1.readLine());
        noOfFeaturesTest = Integer.parseInt(br1.readLine());
        noOfOuputsTest = Integer.parseInt(br1.readLine());
        inputTest = new double[noOfExampleTest][noOfFeaturesTest];
        outputTest = new double[noOfExampleTest][noOfOuputsTest];
        for (int i = 0; i < noOfExample-1; i++) {
            String props[] = br.readLine().split("\t");
            int j;
            input[i][0] = dayOfYear(props[0]);
            if (i>0) {
                output[i - 1][0] = Double.parseDouble(props[1]);
            }
            for (j = 1; j < noOfFeatures; j++) {
                input[i][j] = Double.parseDouble(props[j]);
            }
        }
//        for (int i = 0; i < noOfExampleTest; i++) {
//            String props[] = br1.readLine().split("\t");
//            int j;
//            inputTest[i][0] = dayOfYear(props[0]);
//            for (j = 1; j < noOfFeaturesTest; j++) {
//                inputTest[i][j] = Double.parseDouble(props[j]);
//            }
//            for (int k = 0; k < noOfOuputsTest; k++) {
//                outputTest[i][k] = Double.parseDouble(props[k+noOfFeaturesTest]);
//            }
//        }
        br.close();
        fr.close();
        br1.close();
        fr1.close();
    }

    private double dayOfYear(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(date);
        return greg.get(greg.DAY_OF_YEAR);
    }

    public Layer getOutput() throws IOException, ParseException {
        int noOfHidden = 2;
        int noOfNeuronsPerLayer = 7;
        Network network = new Network(noOfFeatures, noOfOuputs, noOfHidden, noOfNeuronsPerLayer, epsilon, noEpoch);
        network.learn(input, output);
        //network.test(inputTest, outputTest);
        //pt datele introduse de user
        double userInput[] = new double[noOfFeatures];
        FileReader fr = new FileReader("userData.txt");
        BufferedReader br = new BufferedReader(fr);
        String props[] = br.readLine().split("\t");
        br.close();
        fr.close();
        userInput[0] = dayOfYear(props[0]);
        System.out.println(noOfFeatures);
        for (int i=1; i<noOfFeatures;i++){
            userInput[i] = Double.parseDouble(props[i]);
        }
        network.activate(userInput);
        Layer layer = network.getLayer(noOfHidden+1);
        System.out.println(network.getLayer(1));
        return layer;
    }
}
