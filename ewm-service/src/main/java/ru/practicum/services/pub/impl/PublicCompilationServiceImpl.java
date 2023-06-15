package ru.practicum.services.pub.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.models.compilation.Compilation;
import ru.practicum.models.event.Event;
import ru.practicum.repositories.compilation.CompilationRepository;
import ru.practicum.services.pub.api.PublicCompilationService;
import ru.practicum.utility.EventShortDtoHandler;
import ru.practicum.utility.PageableBuilder;
import ru.practicum.utility.mapper.CompilationMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {
    CompilationRepository compilationRepository;
    EventShortDtoHandler eventShortDtoHandler;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return mapCompilationPage(compilationRepository.findAll(pinned, PageableBuilder.getPageable(from, size)));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.getCompilationById(compId);
        return getCompilationDto(compilation);
    }

    private List<CompilationDto> mapCompilationPage(Page<Compilation> compilations) {
        return compilations
                .getContent()
                .stream()
                .map(this::getCompilationDto)
                .collect(Collectors.toList());
    }

    private CompilationDto getCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        Set<Long> eventIds = compilation.getEvents()
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        compilationDto.setEvents(eventShortDtoHandler.getEventShortList(eventIds));
        return compilationDto;
    }
}
