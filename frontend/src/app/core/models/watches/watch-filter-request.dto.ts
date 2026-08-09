import { WatchCondition } from './enums/watch-condition';
import { WatchGender } from './enums/watch-gender';
import { WatchMovementType } from './enums/watch-movement-type';
import { WatchStatus } from './enums/watch-status';
import { WatchType } from './enums/watch-type';

export interface WatchFilterRequestDTO {
    manufacturer?: string | null;
    model?: string | null;
    referenceNumber?: string | null;
    serialNumber?: string | null;
    movement?: string | null;
    condition?: WatchCondition | null;
    gender?: WatchGender | null;
    movementType?: WatchMovementType | null;
    status?: WatchStatus | null;
    watchType?: WatchType | null;
    branch?: string | null;
    minPrice?: number | null;
    maxPrice?: number | null;
    minYear?: number | null;
    maxYear?: number | null;
}