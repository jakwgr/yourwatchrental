import { PaymentMethod } from './payment-method';
import { PaymentStatus } from './payment-status';
import { RentalStatus } from './rental-status';

export interface RentalFilterRequestDTO {
  rentalStatus?: RentalStatus | null;
  paymentStatus?: PaymentStatus | null;
  paymentMethod?: PaymentMethod | null;
  userId?: string | null;
  watchId?: string | null;
  branchId?: string | null;
  startDateFrom?: string | null;
  startDateTo?: string | null;
  endDateFrom?: string | null;
  endDateTo?: string | null;
}