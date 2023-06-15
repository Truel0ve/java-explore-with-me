package ru.practicum.services.admin.api;

import ru.practicum.dto.compilation.*;

public interface AdminCompilationService {

    CompilationDto postNewCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(Long compId, UpdateCompilationDto compilation);
}