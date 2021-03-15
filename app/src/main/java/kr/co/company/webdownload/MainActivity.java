package kr.co.company.webdownload;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class MainActivity extends AppCompatActivity {
    private ImageLoader mImageLoader;
    NetworkImageView mNetworkImageView;

    @Override
    protected void onStart() {
        super.onStart();
        // Instantiate the RequestQueue.
        mImageLoader = kr.co.company.webdownload.CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
        //final String url = "https://www.truiton.com/truiton_sq.jpg";
        String url = "http://address.hitouchsoft.com/images/graphicimage.png";

        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                R.mipmap.ic_launcher, android.R.drawable
                        .ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://address.hitouchsoft.com/images/graphicimage.png";

        //NetworkImageView imageView = (NetworkImageView) findViewById(R.id.network_image_view);
        //mNetworkImageView.setImageUrl(url, mImageLoader);

        mNetworkImageView = (NetworkImageView) findViewById(R.id
                .network_image_view);

    }

    public void onClick(View v) {
        if (isNetworkAvailable()) {
            EditText url = (EditText) findViewById(R.id.url);
            //url.setText("http://address.hitouchsoft.com/images/graphicimage.png");
            //url.setText("https://www.cleverfiles.com/howto/wp-content/uploads/2018/03/minion.jpg");=
            //url.setText("https://w.namu.la/s/32fd8cd97bd7aa79f1760ee143a0248005b49c8340af5b198505b3cba7aee0b2b84d16f7237441fc32c74e7aaf08a57ea99c3474e11682f988e048a6d4f2118ebd83610785fe5e67cbf72dd57b73aff8fe0033359fba338a576e86f2580c453c62d22676375f6bc07bb9234114d62f1f");

            //url.setText(url);

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url.getText().toString());

            //String url = "http://address.hitouchsoft.com/images/graphicimage.png";

            mImageLoader.get(url.getText().toString(), ImageLoader.getImageListener(mNetworkImageView,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            mNetworkImageView.setImageUrl(url.getText().toString(), mImageLoader);

        } else {
            Toast.makeText(getBaseContext(),
                    "Network is not Available", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean isNetworkAvailable() {
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
            available = true;

        return available;
    }

    private Bitmap downloadUrl(String strUrl) throws IOException {
        Bitmap bitmap = null;
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(iStream);
        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
        }
        return bitmap;
    }

    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {

        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... url) {

            try {
                bitmap = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView iView = (ImageView) findViewById(R.id.iv_image);
            iView.setImageBitmap(result);
            // Toast.makeText(getBaseContext(), "Image downloaded successfully",
            // Toast.LENGTH_SHORT).show();

            iView.invalidate();

        }
    }
}