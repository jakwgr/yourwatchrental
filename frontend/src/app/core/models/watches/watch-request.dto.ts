import { WatchCondition } from "./enums/watch-condition";
import { WatchGender } from "./enums/watch-gender";
import { WatchMovementType } from "./enums/watch-movement-type";
import { WatchStatus } from "./enums/watch-status";
import { WatchType } from "./enums/watch-type";

export interface WatchRequestDTO {
  manufacturer: string;
  model: string;
  referenceNumber: string;
  serialNumber: string;
  movement: string;
  description: string;
  yearOfProduction: number;
  pricePerDay: number;
  condition: WatchCondition;
  gender: WatchGender;
  movementType: WatchMovementType;
  status: WatchStatus;
  watchType: WatchType;
  branchId: string;
}