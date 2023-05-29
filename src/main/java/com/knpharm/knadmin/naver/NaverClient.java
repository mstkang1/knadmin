package com.knpharm.knadmin.naver;

import com.knpharm.knadmin.dto.StoreDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

@Component
public class NaverClient {
    // yaml 파일 사용하는데 @Value 어노테이션을 사용하며
    // 내부에 "${}"형태로 yaml에 설정한 대로 기입
    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    @Value("${naver.url.geocoding}")
    private String naverGeocodingUrl;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;



    public StoreDto searchGeocoding(String address) {
        var map = new LinkedMultiValueMap<String, String>();
        map.add("query", address);

        var uri = UriComponentsBuilder
                .fromUriString(naverGeocodingUrl)
                .queryParams(map)
                .build()
                .encode()
                .toUri();

        var headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);
       var responseType = new ParameterizedTypeReference<StoreDto>(){};


        var responseEntity = new RestTemplate()
                .exchange(
                        uri,
                        HttpMethod.GET,
                        httpEntity,
                        responseType
                );

        return responseEntity.getBody();
    }
}
