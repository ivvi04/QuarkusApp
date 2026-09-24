package ru.lakeevda.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record OrderParamResponse(Long id, String name, String status) {
}
