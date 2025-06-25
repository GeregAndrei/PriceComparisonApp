package com.example.price_analysis_app.OpenAI;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTService {

        @Headers({
                "Content-Type: application/json",
                "Authorization: "
        })
        @POST("v1/chat/completions")
        Call<ChatResponse> getChatResponse(@Body ChatRequest request);

}
