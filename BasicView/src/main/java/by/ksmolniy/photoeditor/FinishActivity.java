package by.ksmolniy.photoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;

public class FinishActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap resultBitmap;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                startSaving();
            break;
            case R.id.shareButton:

                startShare();;

            break;
        }
    }

    public void startShare(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"ImageDemo.jpg");
        try{
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e){
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///sdcard/ImageDemo.jpg"));
        startActivity(Intent.createChooser(shareIntent,"Поделиться изображением"));
    }

    public void startSaving(){
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if(!file.exists() && !file.mkdirs()){
            Toast.makeText(this,"Cant create directory to Image",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "Img"+date+".jpg";
        String fileName = file.getAbsolutePath()+"/"+name;
        File newFile = new File(fileName);
        try{
            fileOutputStream = new FileOutputStream(newFile);
            resultBitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);
            Toast.makeText(this,"Save OK",Toast.LENGTH_LONG).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        refreshGalery(newFile);
    }

    void refreshGalery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(file,"PhotoEditor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        //hide this ugly shit
        getSupportActionBar().hide();

        Bitmap previeImage = (Bitmap) getIntent().getExtras().get("miniImage");

        ImageView iv = (ImageView) findViewById(R.id.finishImageView);

        Bitmap photo = (Bitmap) getIntent().getExtras().get("photo");

        String options = (String) getIntent().getExtras().get("stringResult");

        String currentFilter = options.split("-")[0];

        String currentFrame = options.split("-")[1];

        switch (currentFilter){
            case "sepia":
                resultBitmap = BitmapProcessing.sepia(photo);
                break;
            case "negative":
                resultBitmap = BitmapProcessing.invert(photo);
                break;
        }

        switch (currentFrame){
            case "frame1":

                Bitmap frame = BitmapFactory.decodeResource(getResources(),R.drawable.frame1);
                frame = Bitmap.createScaledBitmap(frame,photo.getWidth(),photo.getHeight(),true);
                resultBitmap = BitmapProcessing.overlay(resultBitmap,frame);

                break;
        }

        iv.setImageBitmap(previeImage);


        findViewById(R.id.saveButton).setOnClickListener(this);
        findViewById(R.id.shareButton).setOnClickListener(this);
    }
}
