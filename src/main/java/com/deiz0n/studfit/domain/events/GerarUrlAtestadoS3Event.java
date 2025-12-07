package com.deiz0n.studfit.domain.events;

import java.util.UUID;

public record GerarUrlAtestadoS3Event(String url, UUID alunoId) {
}
