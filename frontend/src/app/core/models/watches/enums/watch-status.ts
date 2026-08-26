export enum WatchStatus {
    AVAILABLE = "AVAILABLE",
    RENTED = "RENTED",
    IN_SERVICE = "IN_SERVICE",
    UNAVAILABLE = "UNAVAILABLE",
    DISABLED = "DISABLED"
}

export const WatchStatusLabel: Record<WatchStatus, string> = {
    [WatchStatus.AVAILABLE]: "AVAILABLE",
    [WatchStatus.RENTED]: "RENTED",
    [WatchStatus.IN_SERVICE]: "IN SERVICE",
    [WatchStatus.UNAVAILABLE]: "UNAVAILABLE",
    [WatchStatus.DISABLED]: "DISABLED"
};