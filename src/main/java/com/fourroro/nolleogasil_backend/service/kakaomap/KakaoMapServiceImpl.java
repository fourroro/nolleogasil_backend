package com.fourroro.nolleogasil_backend.service.kakaomap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMapServiceImpl implements KakaoMapService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Override
    public String searchPlaceByLocationId(Integer locationId) {
        String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = apiUrl + "?query=" + locationId;

        RestTemplate restTemplate = new RestTemplate();  //HTTP 요청
        ResponseEntity<String> responseEntity = restTemplate.exchange(  //exchange()로 API 요청 수행
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        //responseEntity: API 응답
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();  //JSON형태의 문자열 반환
        } else {
            return "API 호출에 실패하였습니다. 상태 코드: " + responseEntity.getStatusCode().value();  //오류메세지 반환
        }
    }
}
