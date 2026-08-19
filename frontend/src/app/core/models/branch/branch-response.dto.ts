import { BranchStatus } from "./enums/branch-status";

export interface BranchResponseDTO{
    id: string,
    city: string,
    name:string,
    address: string,
    phoneNumber: string,
    email: string,
    status: BranchStatus
}