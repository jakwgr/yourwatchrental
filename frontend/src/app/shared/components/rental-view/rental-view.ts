import { Component, inject, input, output, signal } from '@angular/core';
import { RentalResponseDTO } from '../../../core/models/rentals/rental-response.dto';
import { PaymentStatus } from '../../../core/models/rentals/payment-status';
import { RentalStatus } from '../../../core/models/rentals/rental-status';
import { ReactiveFormsModule } from '@angular/forms';
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

  rentalViewModal = signal(false);


  cancelRental() {

    this.rentalsService.cancelRental(this.rental().id).subscribe({
      next: response => {

        console.log("Anulowano wypożyczenie", response);

        // Informacja do rodzica, żeby ponownie pobrał wypożyczenia
        this.reloadRental.emit();

      },
      error: err => {
        console.error("Błąd podczas anulowania wypożyczenia", err);
      }
    });

  }


  payRental() {

    this.rentalsService.changePaymentStatus(
      this.rental().id,
      PaymentStatus.SUCCESSFUL
    ).subscribe({
      next: response => {

        console.log("Opłacono wypożyczenie", response);

        // Informacja do rodzica, żeby ponownie pobrał wypożyczenia
        this.reloadRental.emit();

      },
      error: err => {
        console.error("Błąd podczas opłacania wypożyczenia", err);
      }
    });

  }


  openRentalModal() {
    this.rentalViewModal.set(true);
  }


  closeRentalModal() {
    this.rentalViewModal.set(false);
  }

}