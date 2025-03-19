package com.example.price_analysis_app.OpenAI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTService {

        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer "
        })
        @POST("v1/chat/completions")
        Call<ChatResponse> getChatResponse(@Body ChatRequest request);

}
