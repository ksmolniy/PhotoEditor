package by.ksmolniy.photoeditor;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    String currentFilter;


    /*
    if(!(((BitmapDrawable)editorView.getDrawable()).getBitmap().isRecycled())){
                        ((BitmapDrawable)editorView.getDrawable()).getBitmap().recycle();
                        processedBitmap = BitmapProcessing.sepia(photoBitmap);
                    }

    */

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.btnWithoutfilters:
                if(currentFilter != "nofilter"){

                    processedBitmap = photoBitmap;


                    editorView.setImageBitmap(photoBitmap.copy(photoBitmap.getConfig(),true));
                    currentFilter = "nofilter";
                }
            break;
            case R.id.btnSepia:
                if(currentFilter != "sepia"){

                    processedBitmap = BitmapProcessing.sepia(photoBitmap);

                    editorView.setImageBitmap(processedBitmap.copy(processedBitmap.getConfig(),true));
                    currentFilter = "sepia";
                } else{
                    editorView.setImageBitmap(photoBitmap.copy(photoBitmap.getConfig(),true));
                    currentFilter = "nofilter";
                }
                break;
            case R.id.btnNegative:
                if(currentFilter != "negative") {

                    processedBitmap = BitmapProcessing.invert(photoBitmap);
                    editorView.setImageBitmap(processedBitmap.copy(processedBitmap.getConfig(),true));
                    currentFilter = "negative";
                } else {

                    editorView.setImageBitmap(photoBitmap.copy(photoBitmap.getConfig(),true));
                    currentFilter = "nofilter";
                }
            break;
        }
    }

    Bitmap photoBitmap;

    Bitmap processedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //hide this ugly shit
        getSupportActionBar().hide();

        editorView = (ImageView) findViewById(R.id.editorView);

        editorView.setDrawingCacheEnabled(true);

        currentFilter = "nofilter";

        findViewById(R.id.btnWithoutfilters).setOnClickListener(this);
        findViewById(R.id.btnSepia).setOnClickListener(this);
        findViewById(R.id.btnNegative).setOnClickListener(this);


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
            editorView.setImageBitmap(photoBitmap.copy(photoBitmap.getConfig(),true));
        } else{
            Toast.makeText(getApplicationContext(),"Не могу найти изображение", Toast.LENGTH_LONG).show();
            finish();
        }

    }




}
