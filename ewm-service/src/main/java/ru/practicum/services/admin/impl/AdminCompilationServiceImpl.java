package ru.practicum.services.admin.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;
import ru.practicum.models.compilation.Compilation;
import ru.practicum.models.event.Event;
import ru.practicum.repositories.compilation.CompilationRepository;
import ru.practicum.repositories.event.EventRepository;
import ru.practicum.services.admin.api.AdminCompilationService;
import ru.practicum.utility.EventShortDtoHandler;
import ru.practicum.utility.mapper.CompilationMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;
    EventShortDtoHandler eventShortDtoHandler;

    @Override
    public CompilationDto postNewCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> compilationEvents = getCompilationEvents(newCompilationDto.getEvents());
        compilation.setEvents(compilationEvents);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.setEvents(eventShortDtoHandler.getEventShortList(newCompilationDto.getEvents()));
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.getCompilationById(compId);
        compilationRepository.deleteById(compilation.getId());
    }

    @Override
    public CompilationDto patchCompilation(Long compId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationRepository.getCompilationById(compId);
        setCompilationFields(updateCompilationDto, compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.setEvents(eventShortDtoHandler.getEventShortList(updateCompilationDto.getEvents()));
        return compilationDto;
    }

    private void setCompilationFields(UpdateCompilationDto updateCompilationDto, Compilation compilation) {
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(getCompilationEvents(updateCompilationDto.getEvents()));
        }
    }

    private Set<Event> getCompilationEvents(Set<Long> eventIds) {
        if (eventIds != null) {
            return new HashSet<>(eventRepository.findAllById(eventIds));
        } else {
            return Collections.emptySet();
        }
    }
}
