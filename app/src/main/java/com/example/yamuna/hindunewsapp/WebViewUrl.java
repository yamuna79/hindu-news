package com.example.yamuna.hindunewsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;


public class WebViewUrl extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    String url;
    String s;
    String s1,s2,img,im;
    Bitmap bm = null;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_url);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        url = getIntent().getStringExtra("url");
        DownloadText downloadText = new DownloadText();
        downloadText.execute();

    }

    public class DownloadText extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url).get();

                Elements paragraphs = doc.getElementsByClass("body");
                for (Element p : paragraphs)
                    s += p.text();

                s1 = s.substring(4);

                    Elements images = doc.getElementsByClass("main-image");

                if(images.select("img").first() != null) {
                    Element image = images.select("img").first();
                    s2 = image.attr("src");
                    System.out.println(s2);
                }else if(doc.getElementById("pic") != null){
                    Element divPic = doc.getElementById("pic");

                        Element image = divPic.getAllElements().select("img").first();
                        s2 = image.attr("src");
                        System.out.println(s2);
                }else{
                    s2 = null;
                }

                try {
                    if (s2 != null) {
                        InputStream is = new java.net.URL(s2).openStream();
                        bm = BitmapFactory.decodeStream(is);
                    }
                    else{
                        bm = null;
                        i = R.mipmap.ic_launcher;
                    }
                }catch(Exception e){
                        e.printStackTrace();
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText(s1);
            if(bm!=null) {
                imageView.setImageBitmap(bm);
            }else{
                imageView.setImageResource(i);
            }

        }
    }
}
