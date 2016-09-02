import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by bianca on 19.08.2016.
 */
public class UtilMain {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);
        Integer.parseInt(br.readLine());
        Integer.parseInt(br.readLine());
        Integer.parseInt(br.readLine());
        for (int i = 0; i < 20; i++) {
            String props[] = br.readLine().split("\t");
            System.out.println(props[0]+" "+props[6]);
        }
    }
}
