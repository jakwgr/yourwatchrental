import { role } from "./enums/role";
import { userStatus } from "./enums/user-status";

export interface UserResponseDTO{
    id: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    email: string
    phoneNumber: string;
    createdAt: string;
    role: role;
    status: userStatus;
}