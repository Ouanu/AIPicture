package com.moment.aipicture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnGetPicture;
    private String TAG = "MainActivity";
    private ImageView mIvPhoto;
    private TextView mTvTitle;
    private Button mBtnSetModel;
    private ImageButton mIbSetPic;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnGetPicture = findViewById(R.id.btn_get_picture);
        mIvPhoto = findViewById(R.id.iv_photo);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnSetModel = findViewById(R.id.btn_set_model);
        mIbSetPic = findViewById(R.id.ib_set_pic);
        mIvPhoto.setVisibility(View.INVISIBLE);
        mTvTitle.setVisibility(View.INVISIBLE);
        mBtnSetModel.setVisibility(View.INVISIBLE);
        mIbSetPic.setVisibility(View.INVISIBLE);

    }

    public void getPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setVisibility();
            imageUri = data.getData();
            mIvPhoto.setImageURI(data.getData());
        }
    }


    protected void setVisibility() {
        if (mBtnGetPicture.getVisibility() == View.VISIBLE) {
            mBtnGetPicture.setVisibility(View.INVISIBLE);
            mIvPhoto.setVisibility(View.VISIBLE);
            mTvTitle.setVisibility(View.VISIBLE);
            mBtnSetModel.setVisibility(View.VISIBLE);
            mIbSetPic.setVisibility(View.VISIBLE);
        }
    }

    public void setModel(View view) {
        Intent intent= new Intent(this, ModelActivity.class);
        intent.setData(imageUri);
        startActivity(intent);
    }
}