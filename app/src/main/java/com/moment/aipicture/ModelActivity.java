package com.moment.aipicture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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

import com.moment.aipicture.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
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
    private Button btnSave;
    private Bitmap imageBitmap;


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
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.change_btn) {
            Log.d(TAG, "onClick: -------------test");
            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageBit = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                bitmap = toGrayScale(bitmap);
                imageBitmap = toHighPixel(4);
                ivImage.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onClick: " + e.getMessage());
            }
        } else if(v.getId() == R.id.btn_save) {
            imageBitmap = ((BitmapDrawable)ivImage.getDrawable()).getBitmap();
            String targetPath = this.getFilesDir() + "/images/";
            if (!FileUtils.fileIsExist(targetPath)) {
                Log.d(TAG, "onClick: " + "TargetPath isn't exists");
            } else {
                Log.d(TAG, "onClick: " + "TargetPath is exists");
                String fileName = String.valueOf(System.currentTimeMillis());
                File saveFile = new File(targetPath, fileName);
                try {
                    FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                    // compress - 压缩的意思
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, saveImgOut);
                    //存储完成后需要清除相关的进程
                    saveImgOut.flush();
                    saveImgOut.close();
                    Log.d("Save Bitmap", "The picture is save to your phone!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        System.out.println("width:" + width + " high:" + high);
        int amountToExpand;
        int[][] finalImage = new int[width * b][high * b];

        Bitmap bitmap = Bitmap.createBitmap(width * b, high * b, Bitmap.Config.ARGB_8888);
        if (finalImage.length < finalImage[0].length) {
            amountToExpand = (finalImage.length + 1) / 2;
        } else {
            amountToExpand = (finalImage[0].length + 1) / 2;
        }
        System.out.println("finalImage:" + finalImage.length);
        System.out.println("amountToExpand:" + amountToExpand);
        for (int i = 0; i < finalImage.length; i++) {
            for (int j = 0; j < finalImage[0].length; j++) {
//                finalImage[i][j]=imageBit.getPixel(i / amountToExpand,j / amountToExpand);
                int originColor = imageBit.getPixel(i / b, j / b);

                int red = (Color.red(originColor));
                int alpha = Color.alpha(originColor);
                int green = Color.green(originColor);
                int blue = Color.blue(originColor);


                System.out.print(originColor + "\t");
                bitmap.setPixel(i, j, Color.argb(alpha, red, green, blue));
//                System.out.print();
//                bitmap.setPixel(i,j,imageBit.getPixel(i / amountToExpand,j / amountToExpand));
            }
            System.out.println();
        }
        return bitmap;
    }


}
