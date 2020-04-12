package com.myapp.webtoon_viewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.webtoon_downloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class imageViewer extends AppCompatActivity {
    String path="";
    Context mcontext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_image_viewer);
        Intent intent=getIntent();
        path=intent.getStringExtra("path")+"/";
        makefield();
    }
    private void  makefield() {
        File fileList = new File(path);
        File[] files = fileList.listFiles();
        ArrayList<imageItems> filelist=new ArrayList<>();
        if (files != null) {
            for (File i:files) {
                String t = i.toString();
                if ('.' != t.charAt(t.lastIndexOf("/") + 1)) {
                    imageItems item;
                    if (i.getPath().contains("jpg"))
                        item = new imageItems(i.getPath(), true);
                    else
                        item = new imageItems(i.getPath(), false);
                    filelist.add(item);
                }
            }
        }
        Collections.sort(filelist);
        LinearLayout field=findViewById(R.id.imageField);
        for(imageItems i:filelist)
        {
            ImageView image = new ImageView(this);
            File file = new File(i.getPath());
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bm);
            field.addView(image);
        }
    }
}
