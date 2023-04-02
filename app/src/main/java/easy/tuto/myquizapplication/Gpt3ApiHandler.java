package easy.tuto.myquizapplication;

import android.os.AsyncTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

public class Gpt3ApiHandler {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    public static void generatedCode(String input, Gpt3ApiHandlerListener listener) {
        new Gpt3Task(input, listener).execute();
    }

    public interface Gpt3ApiHandlerListener {
        void onCodeGenerated(String code);
        void onError(String errorMessage);
    }

    private static class Gpt3Task extends AsyncTask<Void, Void, String> {
        private final String input;
        private final Gpt3ApiHandlerListener listener;

        public Gpt3Task(String input, Gpt3ApiHandlerListener listener) {
            this.input = input;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return generateCode(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                listener.onCodeGenerated(response);
            } else {
                listener.onError("Failed to generate code");
            }
        }
    }

    static String generateCode(String input) throws Exception {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        json.put("prompt", input);
        json.put("max_tokens", 1024);
        json.put("temperature", 0.5);
        json.put("stop", "");

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new Exception("Unexpected code " + response);
        }

        JSONObject jsonResponse = new JSONObject(response.body().string());
        System.out.println("API Response: " + jsonResponse.toString());

        return jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
