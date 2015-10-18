package yulongproductions.com.matchme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by Alan on 10/18/2015.
 */
public class Dictionary {

    public String define(String word) throws IOException {
        // Make a URL to the web page
        URL url = new URL("http://dictionary.reference.com/browse/" + word);
        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();
        // Once you have the Input Stream, it's just plain old Java IO stuff.
        // For this case, since you are interested in getting plain-text web page
        // I'll use a reader and output the text content to System.out.
        // For binary content, it's better to directly read the bytes from stream and write
        // to the target file.
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        //Not sure how to use array here
        ArrayList<String> definitions = new ArrayList<String>();
        // read each line and write to System.out
        while (((line = br.readLine()) != null)) {
            String builder = "";
            if (line.contains("def-content")) {
                String next = br.readLine();
                if (next.charAt(0) != '(') {
                    if (next.charAt(0) != '<') {
                        for (int i = 0; i < next.length() - 1; i++) {
                            if (next.charAt(i) == '<') {
                                while (next.charAt(i) != '>') {
                                    i++;
                                }
                            }
                            if (next.charAt(i) != '>')
                                builder+=next.charAt(i);
                        }
                        definitions.add(builder);
                        builder = "";
                        //System.out.println();
                    }
                }
            }
        }
        int ran = (int) (Math.random() * definitions.size());
        return definitions.get(ran);
    }
}
