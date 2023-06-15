package ru.practicum.repositories.compilation;

import org.springframework.context.annotation.Lazy;
import ru.practicum.exceptions.ArgumentNotFoundException;
import ru.practicum.models.compilation.Compilation;

@SuppressWarnings("all")
public class CompilationRepositoryImpl implements CompilationRepositoryCustom {
    private final CompilationRepository compilationRepository;

    public CompilationRepositoryImpl(@Lazy CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new ArgumentNotFoundException("The specified compilation id=" + compId + " does not exist",
                        new NullPointerException()));
    }
}
