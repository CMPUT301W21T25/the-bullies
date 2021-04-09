package com.example.cmput301w21t25.activities_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cmput301w21t25.R;

public class DisplayQRActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_q_r);

        bitmap = (Bitmap) getIntent().getParcelableExtra("QR_CODE");
        qrCode = findViewById(R.id.generated_qr_code);
        qrCode.setImageBitmap(bitmap);

        final Button back = findViewById(R.id.back_to_menu_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(DisplayQRActivity.this, MenuQRActivity.class);
                startActivity(back);
            }
        });

    }
}