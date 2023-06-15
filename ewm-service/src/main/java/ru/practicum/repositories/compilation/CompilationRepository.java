package ru.practicum.repositories.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {

    @Query("SELECT c " +
            "FROM Compilation c " +
            "WHERE c.pinned IN ?1 OR ?1 IS NULL")
    Page<Compilation> findAll(Boolean pinned, Pageable pageable);
}
