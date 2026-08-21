import { PhotoType } from "./photo-type";

export interface WatchPhotoResponseDTO{
        id: string,
        photoUrl: string,
        photoType: PhotoType,
        description: string,
        watchId: string
}