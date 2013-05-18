/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Testing;

import com.mapdigit.network.Connector;
import com.mapdigit.network.HttpConnection;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author James
 */
public class Main {
 public static void main(String[] args){
        try {
            HttpConnection conn = Connector.open("http://mt2.google.com/vt/lyrs=m@121&hl=en&x=1&y=1&z=1&s=Gali");

            conn.setRequestProperty("Accept", "image/png");
             int status=conn.getResponseCode();
             InputStream is=conn.openInputStream();
             int len=is.available();
             byte [] buffer=new byte[len];
             is.read(buffer);
             conn.close();

            System.out.println(status);
        } catch (IOException ex) {

        }
    }

}
