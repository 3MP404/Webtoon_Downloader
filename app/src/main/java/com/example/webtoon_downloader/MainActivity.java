package com.example.webtoon_downloader;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    public linkControl linkControl = new linkControl();
    public Context mcontext;
    String mainURL = "https://comic.naver.com/webtoon/weekday.nhn";
    Intent foregroundServiceIntent;

    Point displaySize = new Point();
    Handler handler = new Handler(msg -> {//타일 설정
        String[] names = new String[]{"MontabContent", "TuetabContent", "WedtabContent", "ThutabContent", "FritabContent", "SattabContent", "SuntabContent"};
        int nameindex = 0;
        int index = 1;
        String day = "";
        itemList list;

        while (index < linkControl.getElementList().size() && nameindex < 7) {
            int id = getResources().getIdentifier(names[nameindex], "id", getPackageName());
            nameindex++;
            LinearLayout layout = findViewById(id);
            list = linkControl.getElementList().get(index);
            day = list.getDay();
            while (day.equals(list.getDay())) {
                WebtoonTiles tile = new WebtoonTiles(this, null, 0);
                tile.setThumbnail(list.getTitle(), list.getImagesrc(), list.getComiclist());
                layout.addView(tile);
                tile.getLayoutParams().width = displaySize.x;

                index++;
                if (index >= linkControl.getElementList().size())
                    break;
                list = linkControl.getElementList().get(index);
            }
        }

        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makePermission();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(displaySize);
        createNotificationChannel();

        mcontext = this;
        setContentView(R.layout.activity_main);


        /*if (updateCheck.serviceIntent == null) {
            foregroundServiceIntent = new Intent(this, updateCheck.class);
            startService(foregroundServiceIntent);
        } else {
            foregroundServiceIntent = updateCheck.serviceIntent;
        }*/


        ////tabhost 세팅
        TabHost host = findViewById(R.id.host);
        host.setup();
        TabHost.TabSpec monspec = host.newTabSpec("tab1");
        monspec.setContent(R.id.MontabContent);
        monspec.setIndicator("월");
        host.addTab(monspec);

        TabHost.TabSpec tuespec = host.newTabSpec("tab2");
        tuespec.setContent(R.id.TuetabContent);
        tuespec.setIndicator("화");
        host.addTab(tuespec);

        TabHost.TabSpec wedspec = host.newTabSpec("tab3");
        wedspec.setContent(R.id.WedtabContent);
        wedspec.setIndicator("수");
        host.addTab(wedspec);

        TabHost.TabSpec thuspec = host.newTabSpec("tab4");
        thuspec.setContent(R.id.ThutabContent);
        thuspec.setIndicator("목");
        host.addTab(thuspec);

        TabHost.TabSpec frispec = host.newTabSpec("tab5");
        frispec.setContent(R.id.FritabContent);
        frispec.setIndicator("금");
        host.addTab(frispec);

        TabHost.TabSpec satspec = host.newTabSpec("tab6");
        satspec.setContent(R.id.SattabContent);
        satspec.setIndicator("토");
        host.addTab(satspec);

        TabHost.TabSpec sonspec = host.newTabSpec("tab7");
        sonspec.setContent(R.id.SuntabContent);
        sonspec.setIndicator("일");

        host.addTab(sonspec);

        new Thread() {
            public void run() {
                try {
                    Document doc = Jsoup.connect(mainURL).get();
                    Elements elements = doc.select("div.col_inner");
                    linkControl.sethtml(elements.toString());
                } catch (Exception ex) {
                    Log.d("mydebug", ex.toString());
                }
                handler.sendMessage(handler.obtainMessage());
            }
        }.start();

        updateCheck a = new updateCheck();
        a.makeAlarm(this);

    }

    private void makePermission() {
        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1004", "yee", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
