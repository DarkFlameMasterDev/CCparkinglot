package com.czb.ccparkinglot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.yzq.zxinglibrary.encode.CodeCreator;

public class QrCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        Bitmap bitmap = CodeCreator.createQRCode("省+市+县+区+停车场+随机6位数字编号", 400, 400, null);
        ImageView qrcode = findViewById(R.id.activity_qr_code_imageview_qrcode);
        qrcode.setImageBitmap(bitmap);
    }
}
