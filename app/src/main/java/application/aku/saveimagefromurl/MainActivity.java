package application.aku.saveimagefromurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView ivgambar;
    Button btnsave;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivgambar = findViewById(R.id.ivgambar);
        btnsave = findViewById(R.id.btnsave);

        Glide.with(getApplicationContext())
                .load("https://kgo.googleusercontent.com/profile_vrt_raw_bytes_1587515303_10059.png")
                .into(ivgambar);

        btnsave.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else{
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }else {
                    bitmap = ((BitmapDrawable)ivgambar.getDrawable()).getBitmap();

                    String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File dir = new File(path.getAbsolutePath()+"/Images");
                    dir.mkdirs();

                    String imagename = time+".png";
                    File file = new File(dir, imagename);
                    OutputStream out;

                    try {
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Toast.makeText(MainActivity.this, "Image Saved", Toast.LENGTH_LONG).show();
                        new SingleMediaScanner(MainActivity.this, file);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
