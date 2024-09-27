package com.project.shopapp.utils;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.response.IResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BindingResultUtils {
    public static ResponseEntity<BaseResponse> getResponseEntity(LocalizationUtils localizationUtils,org.springframework.validation.BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errors= new HashMap<>();

            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );

            String errorMsg= "";

            for(String key: errors.keySet()){
                errorMsg+= errors.get(key) + "\n";
            }

            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(
                           MessageKeys.ERROR_OCCURRED, errorMsg
                    )).build());

        }
        return null;
    }

}
