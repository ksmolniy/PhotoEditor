package by.ksmolniy.photoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v4.content.FileProvider;
import android.provider.MediaStore;
import java.io.*;
import java.text.SimpleDateFormat;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnPhoto;
    Button btnGalleria;


    static final int RESULT_LOAD_IMAGE = 2;
    static final int RESULT_PHOTO_IMAGE = 1;

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

    static final int REQUEST_TAKE_PHOTO = 1;


    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Toast.makeText(this,"cant make directory",Toast.LENGTH_LONG).show();
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    Uri fileUri;

    //photoListener onClick
    public void photoClick(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);


        startActivityForResult(intent,RESULT_PHOTO_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_PHOTO_IMAGE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Intent editorActivity = new Intent(this, EditorActivity.class);
//
//            editorActivity.putExtra("photo", imageBitmap);
//            startActivity(editorActivity);

            Intent editorActivity = new Intent(this,EditorActivity.class);

            editorActivity.putExtra("photoUri", fileUri);
            startActivity(editorActivity);


        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

            Intent editorActivity = new Intent(this, EditorActivity.class);

            editorActivity.putExtra("photoUri", selectedImage);
            startActivity(editorActivity);

            // String picturePath contains the path of selected Image
        }
    }

}


