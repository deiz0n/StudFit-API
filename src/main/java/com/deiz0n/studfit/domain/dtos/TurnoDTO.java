package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurnoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    private String nome;
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

}
