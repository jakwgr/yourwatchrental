import { BranchCreate } from "../../../../pages/branch-create/branch-create";

export enum BranchStatus{
    DISABLED = "DISABLED",
    ACTIVE = "ACTIVE"
}

export const BranchStatusLabel: Record<BranchStatus, string> = {
    [BranchStatus.DISABLED]: "DISABLED",
    [BranchStatus.ACTIVE]: "ACTIVE"
};