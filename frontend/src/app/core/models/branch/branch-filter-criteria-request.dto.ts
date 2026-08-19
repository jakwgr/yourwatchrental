import { BranchStatus } from "./enums/branch-status";

export interface BranchFilterCriteriaRequest {
  city?: string | null;
  name?: string | null;
  phoneNumber?: string | null;
  address?: string | null;
  email?: string | null;
  status?: BranchStatus | null;
}