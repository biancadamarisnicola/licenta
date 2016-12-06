package ANN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by bianca on 19.08.2016.
 */
public class Controller {
    private Fraction input[][], output[][];
    private Fraction inputTest[][], outputTest[][];
    private int noOfExample, noOfExampleTest;
    private int noOfOuputs, noOfOuputsTest;
    private int noOfFeatures, noOfFeaturesTest;
    private int noEpoch;
    private Fraction epsilon;

    public Controller(int noEpoch, Fraction epsilon) throws IOException, ParseException {
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
        input = new Fraction[noOfExample+1][noOfFeatures];
        output = new Fraction[noOfExample+1][noOfOuputs];
        FileReader fr1 = new FileReader("dataTest.txt");
        BufferedReader br1 = new BufferedReader(fr1);
        noOfExampleTest = Integer.parseInt(br1.readLine());
        noOfFeaturesTest = Integer.parseInt(br1.readLine());
        noOfOuputsTest = Integer.parseInt(br1.readLine());
        inputTest = new Fraction[noOfExampleTest][noOfFeaturesTest];
        outputTest = new Fraction[noOfExampleTest][noOfOuputsTest];
        for (int i = 0; i < noOfExample; i++) {
            String props[] = br.readLine().split("\t");
            int j;
            input[i][0] = new Fraction(BigInteger.ONE).valueOf(dayOfYear(props[0])).calibreaza();
            output[i][0] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[6])).calibreaza();
            for (j = 1; j < noOfFeatures; j++) {
                input[i][j] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[j])).calibreaza();
            }
        }
        for (int i = 0; i < noOfExampleTest; i++) {
            String props[] = br1.readLine().split("\t");
            int j;
            inputTest[i][0] = new Fraction(BigInteger.ONE).valueOf(dayOfYear(props[0])).calibreaza();
            outputTest[i][0] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[6])).calibreaza();
            for (j = 1; j < noOfFeaturesTest; j++) {
                inputTest[i][j] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[j])).calibreaza();
            }
        }
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
        int noOfNeuronsPerLayer = 3;
        Network network = new Network(noOfFeatures, noOfOuputs, noOfHidden, noOfNeuronsPerLayer, epsilon, noEpoch);
        network.learn(input, output);
        network.test(inputTest, outputTest);
        //pt datele introduse de user
        Fraction userInput[] = new Fraction[noOfFeatures];
        FileReader fr = new FileReader("userData.txt");
        BufferedReader br = new BufferedReader(fr);
        String props[] = br.readLine().split("\t");
        br.close();
        fr.close();
        userInput[0] = new Fraction(BigInteger.ONE).valueOf(dayOfYear(props[0])).calibreaza();
        System.out.println(noOfFeatures);
        for (int i=1; i<noOfFeatures;i++){
            userInput[i] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[i])).calibreaza();
        }
        network.activate(userInput);
        Layer layer = network.getLayer(noOfHidden+1);
        //System.out.println(network.getLayer(1));
        return layer;
    }
}
