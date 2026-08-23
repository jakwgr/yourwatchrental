import { PaymentMethod } from "./payment-method";

export interface RentalRequestDTO{
    startDate: string,
    endDate: string,
    paymentMethod: PaymentMethod,
    watchId: string
}