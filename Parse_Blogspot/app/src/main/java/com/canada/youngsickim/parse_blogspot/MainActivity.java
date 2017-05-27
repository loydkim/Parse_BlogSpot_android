package com.canada.youngsickim.parse_blogspot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    String title[];
    String content[];
    String contentHTML[];
    String thumbimage[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // You have to change your blogspot address here
        // for example : http://YOURBLOGADDRESS.blogspot.com/feeds/posts/default
        new StartParsing().execute("http://devloydkimparsing.blogspot.com/feeds/posts/default");
    }

    private class StartParsing extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String getURL = params[0];

            try {
                URL url = new URL(getURL);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("title");

                title = new String[nodeList.getLength()-1];
                content = new String[nodeList.getLength()-1];
                contentHTML = new String[nodeList.getLength()-1];
                thumbimage = new String[nodeList.getLength()-1];

                for (int i = 0; i < nodeList.getLength()-1; i++) {
                    Node node = nodeList.item(i);

                    title[i] = new String();
                    content[i] = new String();
                    contentHTML[i] = new String();
                    thumbimage[i] = new String();

                    //Get blogspot title
                    NodeList titleList = doc.getElementsByTagName("title");
                    Element titleElement = (Element)titleList.item(i+1);
                    titleList = titleElement.getChildNodes();
                    title[i] = ((Node)titleList.item(0)).getNodeValue();

                    //Get blog spot content as HTML
                    NodeList contentHTMLList = doc.getElementsByTagName("content");
                    Element contentHTMLElement = (Element)contentHTMLList.item(i+1);
                    contentHTMLList = contentHTMLElement.getChildNodes();
                    contentHTML[i] = ((Node)contentHTMLList.item(0)).getNodeValue();

                    // Make plain text from HTML
                    content[i] = String.valueOf(Html.fromHtml(Html.fromHtml(((Node)contentHTMLList.item(0)).getNodeValue()).toString()));

                    // Get Post content thumbnail
                    int start = contentHTML[i].indexOf("src=\"") + 5;
                    int end = contentHTML[i].indexOf("\"",start);

                    thumbimage[i] = contentHTML[i].substring(start,end);
                }
            }catch(Exception e) {
                Log.d("Log","XML parsing exception = "+e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Check the data using log
            Log.d("Log","Title is");
            for(int i=0; i<title.length; i++) {
                Log.d("Log",title[i]);
            }
            Log.d("Log","content is");
            for(int i=0; i<content.length; i++) {
                Log.d("Log",content[i]);
            }
            Log.d("Log","thumbimage is");
            for(int i=0; i<thumbimage.length; i++) {
                Log.d("Log",thumbimage[i]);
            }
            Log.d("Log","contentHTML is");
            for(int i=0; i<contentHTML.length; i++) {
                Log.d("Log",contentHTML[i]);
            }
        }
    }
}
