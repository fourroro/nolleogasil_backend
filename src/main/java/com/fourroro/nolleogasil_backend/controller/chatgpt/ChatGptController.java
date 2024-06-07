package com.fourroro.nolleogasil_backend.controller.chatgpt;


import com.fourroro.nolleogasil_backend.dto.chatgpt.ChatGptRequestDto;
import com.fourroro.nolleogasil_backend.dto.chatgpt.ChatGptResponseDto;
import com.fourroro.nolleogasil_backend.dto.travelpath.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bot")
public class ChatGptController {
    @Value("${classpath:openai.model}")
    private String model;
    @Value("${classpath:openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    
    private ResultDto resultDto;

    //chatGPT에게 prompt(질문)에 대한 응답을 요청하고 응답 내용을 클라이언트에게 전달
    @PostMapping("/chat")
    public ResultDto chat(@RequestParam(name = "prompt")String prompt){
        ChatGptRequestDto requestDto = new ChatGptRequestDto(model, prompt);
        ChatGptResponseDto chatGptResponseDto =  template.postForObject(apiURL, requestDto, ChatGptResponseDto.class);

        String responseText = chatGptResponseDto.getChoices().get(0).getMessage().getContent();
        responseText = responseText.replaceAll("\n-", "");

        // 추출한 날짜와 내용을 담을 리스트 생성
        List<String> dates = new ArrayList<>();
        List<String> infos = new ArrayList<>();

        // 날짜와 내용을 각각의 리스트에 add
        String[] lines = responseText.split("\n\n");

        for (String line : lines) {
            try {
                String[] parts = line.split("\n", 2);
                dates.add(parts[0]);
                infos.add(parts[1]);
            }catch(ArrayIndexOutOfBoundsException e){
                break;
            }
        }

        //dto객체에 담아서 클라이언트에 보내기
        resultDto = new ResultDto(dates, infos);

        return resultDto;
    }
}
