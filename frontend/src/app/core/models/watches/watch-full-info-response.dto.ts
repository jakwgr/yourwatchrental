import { WatchCondition } from './enums/watch-condition';
import { WatchGender } from './enums/watch-gender';
import { WatchMovementType } from './enums/watch-movement-type';
import { WatchStatus } from './enums/watch-status';
import { WatchType } from './enums/watch-type';
import { BranchShortResponseDTO } from '../branch/branch-short-response.dto';
import { WatchPhotoShortResponseDTO } from './photos/watch-photo-short-response.dto';

export interface WatchFullInfoResponseDTO {
    id: string;
    manufacturer: string;
    model: string;
    movement: string;
    referenceNumber: string;
    serialNumber: string;
    description: string;
    yearOfProduction: number;
    pricePerDay: number;
    condition: WatchCondition;
    gender: WatchGender;
    movementType: WatchMovementType;
    status: WatchStatus;
    watchType: WatchType;
    branch: BranchShortResponseDTO;
    photos: WatchPhotoShortResponseDTO[]
}