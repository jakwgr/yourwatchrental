import { WatchCondition } from "./enums/watch-condition";
import { WatchGender } from "./enums/watch-gender";
import { WatchMovementType } from "./enums/watch-movement-type";
import { WatchType } from "./enums/watch-type";

export interface WatchUpdateRequestDTO
{
    manufacturer: string,
    model: string,
    referenceNumber: string,
    movement: string,
    description: string,
    yearOfProduction: number,
    pricePerDay: number,
    condition: WatchCondition,
    gender: WatchGender,
    movementType: WatchMovementType,
    watchType: WatchType
}