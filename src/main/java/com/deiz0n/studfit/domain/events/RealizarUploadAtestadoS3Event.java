package com.deiz0n.studfit.domain.events;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record RealizarUploadAtestadoS3Event(MultipartFile atestado, UUID alunoId) {
}
