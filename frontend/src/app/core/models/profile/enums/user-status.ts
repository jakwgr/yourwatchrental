export enum userStatus
{
    ACTIVE = "ACTIVE",
    DISABLED = "DISABLED"
}

export const UserStatusLabel: Record<userStatus, string> = {
    [userStatus.ACTIVE]: "ACTIVE",
    [userStatus.DISABLED]: "DISABLED"
};