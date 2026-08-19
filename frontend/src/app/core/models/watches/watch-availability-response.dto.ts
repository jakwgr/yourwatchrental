import { RentalPeriodResponseDTO } from "./rental-peroid-response.dto";

export interface WatchAvailabilityResponseDTO {
    id: string;
    unavailablePeriods: RentalPeriodResponseDTO[];
}