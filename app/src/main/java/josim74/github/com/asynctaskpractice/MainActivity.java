package josim74.github.com.asynctaskpractice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button btn;
    Handler h = new Handler();
    final String imageLink = "https://cdn.pixabay.com/photo/2017/02/21/04/57/mirror-2084665_640.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        btn = findViewById(R.id.btn_showToast);

    }

    public void btnShowToast(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler h1 = new Handler(Looper.getMainLooper()); //or h = new Handler(); for main handler declared globally..
                try {
                    Thread.sleep(5000);
                    h1.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Hello Inside!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Toast.makeText(MainActivity.this, "Hello From OutSide!!", Toast.LENGTH_SHORT).show();
    }

    public void showProgress(View view) {

        new ShowProgressTask().execute();
    }

    public void showImage(View view) {
        new ShowImge().execute(imageLink);

    }

    // AsyncTask Example to show progress bar....
    private  class ShowProgressTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            // it's parameter void comes from 1st parameter of the AsyncTask class...

            for(int i = 1; i<=1000; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // it's parameter Integer comes from 2nd parameter of the AsyncTask class...
            super.onProgressUpdate(values);
            // runs on main thread

            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /* it's parameter Integer comes from 3rd parameter of the AsyncTask class... according to that parameter doInBackground method is returning
            void and this method getting the parameter as void...
             */
            super.onPostExecute(aVoid);
            //is called after doInbackground method word finished..
            // runs in main thread...

            Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_SHORT).show();
        }
    }


    //Another Asynctask example to download and show Image to the image view...
    private class ShowImge extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL imageUrl = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
        }
    }
}

