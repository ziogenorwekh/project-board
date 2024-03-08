package com.example.projectboard.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "예외 사항 응답")
public class ExceptionResponse {

    @Schema(description = "예외 사항 메세지", defaultValue = "~이(가) 없습니다.")
    private String message;

    @Schema(description = "예외 사항 발생 시간", defaultValue = "0000-00-00")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date causedBy;
}
