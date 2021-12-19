package br.com.alura.school.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    @JsonProperty
    private String email;
    @JsonProperty("quantidade_matriculas")
    private Integer numberOfEnrollments;
}
