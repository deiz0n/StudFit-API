package com.deiz0n.studfit.domain.utils;

import java.time.Instant;
import java.util.UUID;

public class GerarNomeAtestado {

    public static String gerarNome(UUID alunoId) {
        var nomeFinal = new StringBuilder();

        var parteIdUsuario = alunoId.toString()
                .replace("-", "")
                .substring(0, 8);


        var idAleatorio = UUID.randomUUID()
                .toString()
                .replace("-", "");
        var parteIdAleatorio = idAleatorio
                .substring(idAleatorio.length() - 8);

        var instante = Instant.now().toEpochMilli();
        var instanteBase32 = Long.toString(instante, 32);

        nomeFinal.append(parteIdUsuario)
                .append("_")
                .append(parteIdAleatorio)
                .append("_")
                .append(instanteBase32)
                .append(".pdf");

        return nomeFinal.toString();
    }

}
