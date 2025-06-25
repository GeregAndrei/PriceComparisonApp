package com.example.price_analysis_app.OpenAI;
import android.os.Handler;
import android.os.Looper;

import java.util.Arrays;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatGPTClient {
    private static ChatGPTClient instance;
    private final ChatGPTService api;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ChatGPTClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                // you can add logging, timeouts, etc. here
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ChatGPTService.class);
    }

    public static ChatGPTClient getInstance() {
        if (instance == null) {
            synchronized (ChatGPTClient.class) {
                if (instance == null) instance = new ChatGPTClient();
            }
        }
        return instance;
    }

    /**
     * Sends a compare prompt to ChatGPT and delivers the assistant's text to onResult
     */
    public void compare(String prompt, Consumer<String> onResult) {
        // build the ChatRequest:
        // here we assume you want to use the “gpt-3.5-turbo” model
        ChatMessage userMsg = new ChatMessage("user", prompt);
        ChatRequest req = new ChatRequest(
                "gpt-3.5-turbo",
                Arrays.asList(userMsg)
        );

        api.getChatResponse(req).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ChatResponse> call,
                                   Response<ChatResponse> response) {
                String text = "No response";
                if (response.body() != null
                        && !response.body().getChoices().isEmpty()) {
                    text = response.body()
                            .getChoices()
                            .get(0)
                            .getMessage()
                            .getContent();
                }
                final String result = text;
                mainHandler.post(() -> onResult.accept(result));
            }

            @Override
            public void onFailure(retrofit2.Call<ChatResponse> call, Throwable t) {
                mainHandler.post(() ->
                        onResult.accept("Error calling OpenAI: " + t.getMessage())
                );
            }
        });
    }
}
