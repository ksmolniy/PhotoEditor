package by.ksmolniy.photoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v4.content.FileProvider;
import android.provider.MediaStore;
import java.io.*;
import java.util.Date;

import android.net.Uri;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.os.Environment;
import android.icu.text.SimpleDateFormat;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnPhoto;
    Button btnGalleria;



    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide this ugly shit
        getSupportActionBar().hide();

        //make Btns global
        btnPhoto = (Button) findViewById(R.id.button);
        btnGalleria = (Button) findViewById(R.id.button2);
        setContentView(R.layout.activity_main);

    }

    //galleria button listener


    public void galleryPickClick(View v) {

        //Toast.makeText(getApplicationContext(),"FuckYou",Toast.LENGTH_LONG).show();

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);



        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //photoListener onClick
    public void photoClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent editorActivity = new Intent(this,EditorActivity.class);

            editorActivity.putExtra("photo",imageBitmap);
            startActivity(editorActivity);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

            Intent editorActivity = new Intent(this,EditorActivity.class);

            editorActivity.putExtra("photoUri",selectedImage);
            startActivity(editorActivity);

            // String picturePath contains the path of selected Image
        }
    }
}


