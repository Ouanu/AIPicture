package com.moment.aipicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class ModelActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvModelItems;
    private Uri imageUri;
    private static final String TAG = "MainActivity";
    private ImageView ivImage;

    private Bitmap imageBit;
    private Button changeBtn;
    private int[][] pixelsArray;
    private String resultPixels;
    private TextView tvPixels;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        initView();
        imageUri = getIntent().getData();
        ivImage.setImageURI(imageUri);

    }


    private void initView() {
        ivImage = (ImageView) findViewById(R.id.iv_image);
        changeBtn = (Button) findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(this);
        tvPixels = (TextView) findViewById(R.id.tv_pixels);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_btn) {
            Log.d(TAG, "onClick: -------------test");
            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageBit = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                bitmap = toGrayScale(bitmap);
                Bitmap bitmap = toHighPixel(4);
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onClick: " + e.getMessage());
            }
        }
    }

    public Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(filter);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public Bitmap toHighPixel(int b) {
        int width = imageBit.getWidth();
        int high = imageBit.getHeight();
        int amountToExpand;
        int[][] finalImage = new int[width*b][high*b];
        Bitmap bitmap = Bitmap.createBitmap(width*b, high*b, Bitmap.Config.ARGB_8888);
        if(finalImage.length < finalImage[0].length) {
            amountToExpand = (finalImage.length + 1)/2;
        } else {
            amountToExpand = (finalImage[0].length + 1)/2;
        }
        for(int i = 0; i<finalImage.length; i++){
            for(int j = 0; j<finalImage[0].length; j++){
//                finalImage[i][j]=imageBit.getPixel(i / amountToExpand,j / amountToExpand);
                bitmap.setPixel(i,j,imageBit.getPixel(i / amountToExpand,j / amountToExpand));
            }
        }
        return bitmap;
    }

}
