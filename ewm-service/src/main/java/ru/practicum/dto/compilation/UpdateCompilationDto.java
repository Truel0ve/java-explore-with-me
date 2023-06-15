package ru.practicum.dto.compilation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @Nullable
    @Size(min = 1, max = 50)
    String title;
    @Nullable
    Boolean pinned;
    @Nullable
    Set<Long> events;
}
