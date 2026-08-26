export enum WatchCondition{
    LIKE_NEW = "LIKE_NEW",
    EXCELLENT = "EXCELLENT",
    GOOD = "GOOD",
    FAIR = "FAIR"
}
export const WatchConditionLabel: Record<WatchCondition, string> = {
    [WatchCondition.LIKE_NEW]: "LIKE NEW",
    [WatchCondition.EXCELLENT]: "EXCELLENT",
    [WatchCondition.GOOD]: "GOOD",
    [WatchCondition.FAIR]: "FAIR"
};