export enum WatchMovementType {
    QUARTZ = "QUARTZ",
    MANUAL = "MANUAL",
    AUTOMATIC = "AUTOMATIC",
    SOLAR = "SOLAR"
}

export const WatchMovementTypeLabel: Record<WatchMovementType, string> = {
    [WatchMovementType.QUARTZ]: "QUARTZ",
    [WatchMovementType.MANUAL]: "MANUAL",
    [WatchMovementType.AUTOMATIC]: "AUTOMATIC",
    [WatchMovementType.SOLAR]: "SOLAR"
};