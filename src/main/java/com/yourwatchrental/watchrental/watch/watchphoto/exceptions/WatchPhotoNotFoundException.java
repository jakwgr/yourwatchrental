package com.yourwatchrental.watchrental.watch.watchphoto.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceNotFoundException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

import java.util.UUID;

public class WatchPhotoNotFoundException extends ResourceNotFoundException {
    public WatchPhotoNotFoundException(UUID id) {
        super(ErrorMessages.WATCH_PHOTO_NOT_FOUND.getMessage(), id);
    }
}
