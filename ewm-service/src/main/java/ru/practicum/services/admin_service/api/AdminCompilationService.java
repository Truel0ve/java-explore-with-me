package ru.practicum.services.admin_service.api;

import ru.practicum.dto.compilation_dto.*;

public interface AdminCompilationService {

    CompilationDto postNewCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(Long compId, UpdateCompilationDto compilation);
}