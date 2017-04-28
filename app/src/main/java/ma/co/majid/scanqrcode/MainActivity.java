package ma.co.majid.scanqrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editAdresse;
    private ImageView imgResult;
    private TextView tvScanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editAdresse     = (EditText) findViewById(R.id.edit_adresse);
        imgResult       = (ImageView) findViewById(R.id.img_result);
        tvScanned       = (TextView) findViewById(R.id.txt_scanned);

        ((Button) findViewById(R.id.btn_generate)).setOnClickListener(this);
        ((Button) findViewById(R.id.btn_scan)).setOnClickListener(this);

        imgResult.setOnClickListener(this);

        imgResult.setVisibility(View.GONE);
        tvScanned.setVisibility(View.GONE);

    }

    public static Bitmap encodeToQrCode(String text){

        int width = 500;
        int height = 500;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if(result != null) {

            if(result.getContents() == null) {
                //cancel
            } else {
                editAdresse.setText("");
                imgResult.setVisibility(View.GONE);
                tvScanned.setVisibility(View.VISIBLE);
                //Scanned successfully
                tvScanned.setText(result.getContents());
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }
    public void generateCode() {
        tvScanned.setVisibility(View.GONE);
        imgResult.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(editAdresse.getText().toString()))
            imgResult.setImageBitmap(encodeToQrCode(editAdresse.getText().toString()));
        else
            Toast.makeText(this, "Please enter your text", Toast.LENGTH_SHORT).show();
    }
    public void scanTextQR() {
        
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate      : generateCode();break;
            case R.id.img_result        : scanTextQR();break;
            case R.id.btn_scan          : scanTextQR();break;
        }
    }
}
