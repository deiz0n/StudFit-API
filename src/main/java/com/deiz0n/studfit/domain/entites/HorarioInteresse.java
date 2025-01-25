package com.deiz0n.studfit.domain.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_horario_interesse")
public class HorarioInteresse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}
