package ru.practicum.utility.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;
import ru.practicum.utility.DateTimeParser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@UtilityClass
public class EndpointMapper {
    private final String APP = "ewm-service";

    public EndpointHitDto toEndpointHitDto(HttpServletRequest request) {
        EndpointHitDto endpointHit = new EndpointHitDto();
        endpointHit.setApp(APP);
        endpointHit.setUri(request.getRequestURL().toString());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(DateTimeParser.toString(LocalDateTime.now()));
        return endpointHit;
    }
}
