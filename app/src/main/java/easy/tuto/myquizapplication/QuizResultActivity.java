package easy.tuto.myquizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class QuizResultActivity extends AppCompatActivity {

    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        resultTextView = findViewById(R.id.result_textview);

        // Get user answers from intent
        String[] userAnswers = getIntent().getStringArrayExtra("userAnswers");

        // Compute result
        DestinationGenerator destinationGenerator = new DestinationGenerator();
        String bestDestination = destinationGenerator.getBestDestination(userAnswers);

        // Display result
        String resultMessage = "Your best destination is: " + bestDestination;
        resultTextView.setText(resultMessage);

        // Call the API to get image URL based on the best destination
        String apiUrl = "https://api.unsplash.com/search/photos?query=" + "USA" + "&per_page=1&client_id=gnhEMq1cHwx0v9hOGFudH3HOCiTsPMrhlP4vDsk4C-k";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject responseJson = new JSONObject(responseData);
                    JSONArray results = responseJson.getJSONArray("results");
                    if (results.length() > 0) {
                        JSONObject result = results.getJSONObject(0);
                        String imageUrl = result.getJSONObject("urls").getString("regular");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView resultImageView = findViewById(R.id.result_imageview);
                                Glide.with(getApplicationContext())
                                        .load(imageUrl)
                                        .into(resultImageView);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set up button to restart quiz
        findViewById(R.id.restart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartQuiz();
            }
        });
    }

    private void restartQuiz() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
