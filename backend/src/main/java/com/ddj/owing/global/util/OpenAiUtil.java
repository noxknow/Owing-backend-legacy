package com.ddj.owing.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class OpenAiUtil {

    private final OpenAiImageApi openAiImageApi;

    public String createImage(String prompt) {

        OpenAiImageApi.OpenAiImageRequest request = new OpenAiImageApi.OpenAiImageRequest(
                prompt,
                OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
                1,
                "standard",
                "url",
                "1024x1024",
                "natural",
                null
        );

        ResponseEntity<OpenAiImageApi.OpenAiImageResponse> response = openAiImageApi.createImage(request);

        if (response.getBody() != null && !response.getBody().data().isEmpty()) {
            return response.getBody().data().getFirst().url();
        } else {
            throw new RuntimeException("Failed to generate image");
        }
    }
}
