export enum role{
    USER = "USER",
    ADMIN = "ADMIN"
}

export const RoleLabel: Record<role, string> = {
    [role.USER]: "USER",
    [role.ADMIN]: "ADMIN"
};