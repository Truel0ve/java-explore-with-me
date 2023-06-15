package ru.practicum.dto.partrequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    @NotNull
    @NotEmpty
    List<Long> requestIds;
    @NotBlank
    String status;
}
