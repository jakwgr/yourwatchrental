export enum WatchType{
    DRESS = "DRESS",
    DIVER = "DIVER",
    FIELD = "FIELD",
    PILOT = "PILOT",
    CHRONOGRAPH = "CHRONOGRAPH"
}

export const WatchTypeLabel: Record<WatchType, string> = {
    [WatchType.DRESS]: "DRESS",
    [WatchType.DIVER]: "DIVER",
    [WatchType.FIELD]: "FIELD",
    [WatchType.PILOT]: "PILOT",
    [WatchType.CHRONOGRAPH]: "CHRONOGRAPH"
};