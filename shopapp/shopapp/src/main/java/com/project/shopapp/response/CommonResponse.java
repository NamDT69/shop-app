package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class CommonResponse {
    @JsonProperty("created_at")
    private LocalDateTime createAt;
    @JsonProperty(value = "updated_at")
    private LocalDateTime updateAt;
}
