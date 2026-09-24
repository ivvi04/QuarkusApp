package ru.lakeevda.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record OrderParamRequest(String name, String status) {
}
