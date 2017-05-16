package by.ksmolniy.photoeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.provider.MediaStore;
import android.view.View.OnClickListener;


import java.io.IOException;


public class EditorActivity extends AppCompatActivity implements OnClickListener {

    ImageView editorView;

    private LruCache<String, Bitmap> mMemoryCache;


//Текущий фильтр или нет фильтра "nofilter"
    String currentFilter;
    //Текущая рамка или нет рамки "noframe"
    String currentFrame;

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.btnWithoutfilters:
                if(currentFilter != "nofilter"){



                    editorView.setImageBitmap(photoBitmap);

                    processedBitmap = photoBitmap;

                    currentFilter = "nofilter";
                }
            break;
            case R.id.btnSepia:
                if(currentFilter != "sepia"){

                    if(getBitmapFromMemCache("sepia") == null) {

                        processedBitmap = BitmapProcessing.sepia(photoBitmap);
                        addBitmapToMemoryCache("sepia",processedBitmap);
                    } else{

                        processedBitmap = getBitmapFromMemCache("sepia");

                    }
                    editorView.setImageBitmap(processedBitmap);
                    currentFilter = "sepia";
                } else{



                    editorView.setImageBitmap(photoBitmap);
                    currentFilter = "nofilter";
                }
                break;
            case R.id.btnNegative:
                if(currentFilter != "negative") {

                    if(getBitmapFromMemCache("negative") == null) {

                        processedBitmap = BitmapProcessing.invert(photoBitmap);
                        addBitmapToMemoryCache("negative",processedBitmap);
                    } else{

                        processedBitmap = getBitmapFromMemCache("negative");

                    }

                    editorView.setImageBitmap(processedBitmap);
                    currentFilter = "negative";
                } else {



                    editorView.setImageBitmap(photoBitmap);
                    currentFilter = "nofilter";
                }
            break;
            case R.id.btnGoToFinal:
                Intent intent = new Intent(this, FinishActivity.class);

                intent.putExtra("photo", photoBitmap);

                intent.putExtra("miniImage", processedBitmap);

                intent.putExtra("stringResult", currentFilter+"-"+currentFrame);

                startActivity(intent);
                break;
            case R.id.btnCancelFinal:
                th.setCurrentTabByTag("tag1");
                break;
            case R.id.btnFrame1:

                currentFrame = "frame1";

                Bitmap frame = BitmapFactory.decodeResource(getResources(),R.drawable.frame1);
                frame = Bitmap.createScaledBitmap(frame,photoBitmap.getWidth(),photoBitmap.getHeight(),true);
                processedBitmap = BitmapProcessing.overlay(processedBitmap,frame);

                editorView.setImageBitmap(processedBitmap);

                break;
        }
    }

    TabHost th;

    Bitmap photoBitmap;

    Bitmap fullPhoto;

    Bitmap processedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //hide this ugly shit
        getSupportActionBar().hide();

        th = (TabHost) findViewById(R.id.tabHost);

        th.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = th.newTabSpec("tag1");

        tabSpec.setIndicator("Фильтры");

        tabSpec.setContent(R.id.tab1);

        th.addTab(tabSpec);

        tabSpec = th.newTabSpec("tag2");

        tabSpec.setIndicator("Рамки");

        tabSpec.setContent(R.id.tab2);

        th.addTab(tabSpec);

        tabSpec = th.newTabSpec("tag3");

        tabSpec.setIndicator("Завершить");

        tabSpec.setContent(R.id.tab3);

        th.addTab(tabSpec);

        editorView = (ImageView) findViewById(R.id.editorView);

        editorView.setDrawingCacheEnabled(true);

        currentFilter = "nofilter";

        findViewById(R.id.btnWithoutfilters).setOnClickListener(this);
        findViewById(R.id.btnSepia).setOnClickListener(this);
        findViewById(R.id.btnNegative).setOnClickListener(this);
        findViewById(R.id.btnFrame1).setOnClickListener(this);
        findViewById(R.id.btnCancelFinal).setOnClickListener(this);
        findViewById(R.id.btnGoToFinal).setOnClickListener(this);


        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


        Bitmap photoBM = (Bitmap) getIntent().getExtras().get("photo");

        if(photoBM != null){

            photoBitmap = photoBM.copy(photoBM.getConfig(),true);


        } else {
            Uri urlToPhoto = (Uri) getIntent().getExtras().get("photoUri");
            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), urlToPhoto);
                photoBitmap = photoBitmap.copy(photoBitmap.getConfig(),true);
            } catch (IOException e){
                Toast.makeText(getApplicationContext(),"Не могу найти изображение",Toast.LENGTH_LONG).show();
                finish();
            }
        }

        if(photoBitmap != null){

            //Processing bitmap to small size

            fullPhoto = photoBitmap;

            final int maxSize = 600;
            int outWidth;
            int outHeight;
            int inWidth = fullPhoto.getWidth();
            int inHeight = fullPhoto.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            photoBitmap = Bitmap.createScaledBitmap(fullPhoto, outWidth, outHeight, false);

            addBitmapToMemoryCache("nofilter",photoBitmap);
            editorView.setImageBitmap(photoBitmap.copy(photoBitmap.getConfig(),true));
            processedBitmap = photoBitmap;
        } else{
            Toast.makeText(getApplicationContext(),"Не могу найти изображение", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
