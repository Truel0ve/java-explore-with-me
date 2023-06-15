package ru.practicum.services.pub.api;

import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}