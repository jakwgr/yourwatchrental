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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchPhotoService {

    private final WatchPhotoRepository watchPhotoRepository;
    private final WatchRepository watchRepository;
    private final WatchPhotoMapper watchPhotoMapper;

    @Transactional
    public WatchPhotoResponseDTO createPhoto(
            UUID watchId,
            MultipartFile file,
            WatchPhotoRequestDTO request
    ) {
        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new WatchNotFoundException(watchId));

        if (watchPhotoRepository.existsByWatchAndPhotoType(watch, request.photoType())) {
            throw new WatchPhotoTypeAlreadyExistsException();
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("Wrong file");
        }

        String extension = originalFileName.substring(
                originalFileName.lastIndexOf(".")
        );

        String fileName = UUID.randomUUID()
                + "_" + request.photoType().name().toLowerCase()
                + extension;

        Path uploadPath = Paths.get("uploads/watches");

        try {
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath);

            String photoUrl = "/uploads/watches/" + fileName;

            WatchPhoto photo = new WatchPhoto(
                    photoUrl,
                    request.photoType(),
                    request.description(),
                    watch
            );

            WatchPhoto savedPhoto = watchPhotoRepository.save(photo);

            return watchPhotoMapper.toResponseDTO(savedPhoto);

        } catch (IOException e) {
            throw new RuntimeException("Photo cannot be saved", e);
        }
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
                .orElseThrow(() -> new WatchPhotoNotFoundException(id));

        try {
            Path filePath = Paths.get("." + photo.getPhotoUrl());

            Files.deleteIfExists(filePath);

            watchPhotoRepository.delete(photo);

        } catch (IOException e) {
            throw new RuntimeException("Nie udało się usunąć zdjęcia", e);
        }
    }
}
