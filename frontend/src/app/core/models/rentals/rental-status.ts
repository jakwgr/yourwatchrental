export enum RentalStatus {
    PENDING = "PENDING",
    CONFIRMED = "CONFIRMED",
    IN_PROGRESS = "IN_PROGRESS",
    COMPLETED = "COMPLETED",
    CANCELLED = "CANCELLED"
}

export const RentalStatusLabel: Record<RentalStatus, string> = {
    [RentalStatus.PENDING]: "PENDING",
    [RentalStatus.CONFIRMED]: "CONFIRMED",
    [RentalStatus.IN_PROGRESS]: "IN PROGRESS",
    [RentalStatus.COMPLETED]: "COMPLETED",
    [RentalStatus.CANCELLED]: "CANCELLED"
};