package com.example.yamuna.hindunewsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    public static final String Server_Url = "http://www.thehindu.com/news/?service=rss";
    ArrayList<String> headlines, links;
    String[] linkstr;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        headlines = new ArrayList<String>();
        links = new ArrayList<String>();

        DownloadXMLData downloadXMLData = new DownloadXMLData();
        downloadXMLData.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0;j<linkstr.length;j++){
                    if(i==j){
                        url = linkstr[j];
                        Intent intent = new Intent(MainActivity.this,WebViewUrl.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    public class DownloadXMLData extends AsyncTask<Void, Void, Void>{

        String s, s1;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL theUrl = new URL(Server_Url);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(theUrl.openConnection().getInputStream(), "UTF_8");

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG){
                        if(xpp.getName().equalsIgnoreCase("title")){
                            s = xpp.nextText();
                            headlines.add(s);
                        }
                        if(xpp.getName().equalsIgnoreCase("link")){
                            s1 = xpp.nextText();
                            links.add(s1);
                        }
                    }
                eventType = xpp.next();
            }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            linkstr = new String[links.size()];
            linkstr = links.toArray(linkstr);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,headlines);
            listView.setAdapter(adapter);
        }
    }

}
