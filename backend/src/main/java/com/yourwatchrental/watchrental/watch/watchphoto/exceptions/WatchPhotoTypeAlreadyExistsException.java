package com.yourwatchrental.watchrental.watch.watchphoto.exceptions;

import com.yourwatchrental.watchrental.common.exceptions.ResourceAlreadyUsedException;
import com.yourwatchrental.watchrental.common.handlers.ErrorMessages;

public class WatchPhotoTypeAlreadyExistsException extends ResourceAlreadyUsedException {
    public WatchPhotoTypeAlreadyExistsException() {
        super(ErrorMessages.WATCH_PHOTO_SAME_TYPE.getMessage());
    }
}
