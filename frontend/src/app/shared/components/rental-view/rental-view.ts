import { Component, inject, input, output, signal } from '@angular/core';
import { RentalResponseDTO } from '../../../core/models/rentals/rental-response.dto';
import { PaymentStatus } from '../../../core/models/rentals/payment-status';
import { RentalStatus } from '../../../core/models/rentals/rental-status';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { value } from '@primeuix/themes/aura/knob';
import { PaymentMethod } from '../../../core/models/rentals/payment-method';
import { RentalsService } from '../../../core/services/rentals/rentals-service';

@Component({
  selector: 'app-rental-view',
  imports: [ReactiveFormsModule],
  templateUrl: './rental-view.html',
  styleUrl: './rental-view.css',
})
export class RentalView {
  rental = input.required<RentalResponseDTO>();
  admin = input.required<boolean>();  
  seeUser = input<string | null>();
  user = input<string | null>();

  private rentalsService = inject(RentalsService);

  reloadRental = output<void>();

  paymentStatus = PaymentStatus;
  rentalStatus = RentalStatus;

  
  cancelRental()
  {
    this.rentalsService.cancelRental(this.rental().id).subscribe(
      response => {
        this.reloadRental.emit();
        console.log("dziala " + response);
      }
    )
  }

  payRental()
  {
    this.rentalsService.changePaymentStatus(this.rental().id, PaymentStatus.SUCCESSFUL).subscribe(
    response => {
        this.reloadRental.emit();
        console.log("dziala " + response);
      }
    )
  }

  rentalViewModal = signal(false);

  openRentalModal() {
    this.rentalViewModal.set(true);
  }

  closeRentalModal() {
    this.rentalViewModal.set(false);
  }
}
