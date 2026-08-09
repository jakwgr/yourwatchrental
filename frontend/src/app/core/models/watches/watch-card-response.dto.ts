import { WatchStatus } from './enums/watch-status';

export interface WatchCardResponseDTO {
    id: string;
    manufacturer: string;
    model: string;
    pricePerDay: number;
    status: WatchStatus;
    branchName: string;
    thumbnailUrl: string;
}