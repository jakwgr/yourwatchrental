import { BranchResponseDTO } from "../branch/branch-response.dto"
import { UserResponseDTO } from "../profile/user-response.dto"
import { WatchFullInfoResponseDTO } from "../watches/watch-full-info-response.dto"
import { PaymentMethod } from "./payment-method"
import { PaymentStatus } from "./payment-status"
import { RentalStatus } from "./rental-status"

export interface RentalResponseDTO{
        id: string;
        startDate: string;
        endDate: string;
        totalPrice: string;
        paymentMethod: PaymentMethod;
        rentalStatus: RentalStatus;
        paymentStatus: PaymentStatus;
        branch: BranchResponseDTO;
        user: UserResponseDTO;
        watch: WatchFullInfoResponseDTO;
        createdAt: string
}