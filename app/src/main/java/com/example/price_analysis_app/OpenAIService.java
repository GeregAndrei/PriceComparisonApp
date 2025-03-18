package com.example.price_analysis_app;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {

        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer YOUR_API_KEY"
        })
        @POST("v1/chat/completions")
    Call<ChatResponse> getChatResponse(@Body ChatRequest request);

}
