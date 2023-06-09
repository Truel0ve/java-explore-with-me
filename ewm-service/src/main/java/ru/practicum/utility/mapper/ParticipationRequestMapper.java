package ru.practicum.utility.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.partrequest.ParticipationRequestDto;
import ru.practicum.models.partrequest.ParticipationRequest;
import ru.practicum.utility.DateTimeParser;

@UtilityClass
public class ParticipationRequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());
        participationRequestDto.setEvent(participationRequest.getEvent().getId());
        participationRequestDto.setRequester(participationRequest.getRequester().getId());
        participationRequestDto.setCreated(DateTimeParser.toString(participationRequest.getCreated()));
        participationRequestDto.setStatus(participationRequest.getStatus().toString());
        return participationRequestDto;
    }
}
