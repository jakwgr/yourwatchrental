package com.yourwatchrental.watchrental.watch.watchphoto;

import com.yourwatchrental.watchrental.watch.Watch;
import com.yourwatchrental.watchrental.watch.WatchRepository;
import com.yourwatchrental.watchrental.watch.exceptions.WatchNotFoundException;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoRequestDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.dto.WatchPhotoResponseDTO;
import com.yourwatchrental.watchrental.watch.watchphoto.exceptions.WatchPhotoNotFoundException;
import com.yourwatchrental.watchrental.watch.watchphoto.exceptions.WatchPhotoTypeAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchPhotoService {

    private final WatchPhotoRepository watchPhotoRepository;
    private final WatchRepository watchRepository;
    private final WatchPhotoMapper watchPhotoMapper;

    @Transactional
    public WatchPhotoResponseDTO createPhoto(UUID watchId, WatchPhotoRequestDTO request) {

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));


        if (watchPhotoRepository.existsByWatchAndPhotoType(watch, request.photoType())) {
            throw new WatchPhotoTypeAlreadyExistsException();
        }

        WatchPhoto photo = watchPhotoMapper.toEntity(request, watch);

        WatchPhoto savedPhoto = watchPhotoRepository.save(photo);

        return watchPhotoMapper.toResponseDTO(savedPhoto);
    }

    @Transactional
    public List<WatchPhotoResponseDTO> getPhotosByWatch(UUID watchId) {

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));


        return watchPhotoRepository.findAllByWatch(watch)
                .stream()
                .map(watchPhotoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public WatchPhotoResponseDTO getThumbnail(UUID watchId) {

        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));


        WatchPhoto photo = watchPhotoRepository.findByWatchAndPhotoType(watch, PhotoType.FRONT)
                .orElseGet(() -> watchPhotoRepository.findByWatchAndPhotoType(watch, PhotoType.FULL)
                        .orElseGet(() -> watchPhotoRepository.findByWatchAndPhotoType(watch, PhotoType.BACK)
                                .orElseThrow(() -> new WatchPhotoNotFoundException(watchId)
                                )
                        )
                );


        return watchPhotoMapper.toResponseDTO(photo);
    }

    @Transactional
    public void deletePhoto(UUID id) {

        WatchPhoto photo = watchPhotoRepository.findById(id)
                .orElseThrow(() -> new WatchPhotoNotFoundException(null));


        watchPhotoRepository.delete(photo);
    }
}
