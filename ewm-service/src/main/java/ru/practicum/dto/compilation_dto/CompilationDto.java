package ru.practicum.dto.compilation_dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import ru.practicum.dto.event_dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    @Null
    Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    String title;
    @Value("false")
    Boolean pinned;
    @Nullable
    List<EventShortDto> events;
}
