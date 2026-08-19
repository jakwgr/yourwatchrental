import { PhotoType } from "./photo-type";

export interface WatchPhotoShortResponseDTO{
    id: string,
    photoUrl: string,
    photoType: PhotoType,
    description: string
}