package com.example.cmput301w21t25.activities_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cmput301w21t25.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.AbstractMap;

/**
 * This activity is used to generate a QR code
 */
public class GenerateQRActivity extends AppCompatActivity {

    MultiFormatWriter mfw = new MultiFormatWriter();
    BitMatrix bm;
    BarcodeEncoder be;
    Bitmap bmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r);

        final Button generate_qr_code = findViewById(R.id.generate_qr_button);
        final Button scan_qr_code = findViewById(R.id.scan_button);

        String encode = "Eden is cute";

        try {
            bm = mfw.encode(encode, BarcodeFormat.QR_CODE, 200, 200);
            be = new BarcodeEncoder();
            bmap = be.createBitmap(bm);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        generate_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showQRCode = new Intent(GenerateQRActivity.this, DisplayQRActivity.class);
                showQRCode.putExtra("QR_CODE", bmap);
                startActivity(showQRCode);

            }
        });

        scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scan = new Intent(GenerateQRActivity.this, ScanQRActivity.class);
                startActivity(scan);
            }
        });

    }
}