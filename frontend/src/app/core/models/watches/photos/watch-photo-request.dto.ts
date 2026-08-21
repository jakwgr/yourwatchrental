import { PhotoType } from "./photo-type";

export interface WatchPhotoRequestDTO{
    photoType: PhotoType,
    description: string
}