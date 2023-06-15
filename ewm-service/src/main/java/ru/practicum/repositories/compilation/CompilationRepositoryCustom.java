package ru.practicum.repositories.compilation;

import ru.practicum.models.compilation.Compilation;

public interface CompilationRepositoryCustom {
    Compilation getCompilationById(Long compId);
}
