import { Component, inject, output, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';
import { WatchCalendar } from '../../shared/components/watch-calendar/watch-calendar';
import { ReactiveFormsModule, Validators } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { PaymentMethod } from '../../core/models/rentals/payment-method';
import { date } from '@primeuix/themes/aura/datepicker';
import { DatePipe, ɵnormalizeQueryParams } from '@angular/common';
import { RentalsService } from '../../core/services/rentals/rentals-service';
import { RentalRequestDTO } from '../../core/models/rentals/rental-request.dto';
import { SmallErrorView } from '../../shared/components/small-error-view/small-error-view';
import { PortfolioProjectAlert1 } from '../../shared/components/portfolio-project-alert-1/portfolio-project-alert-1';

// import {}

@Component({
  selector: 'app-rental-create',
  imports: [WatchCalendar,
    ReactiveFormsModule,
    DatePipe, SmallErrorView, PortfolioProjectAlert1],
  providers: [DatePipe],
  templateUrl: './rental-create.html',
  styleUrl: './rental-create.css',
})
export class RentalCreate {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);
  private rentalsService = inject(RentalsService);
  private datePipe = inject(DatePipe);
  rentalError = signal<string | null>(null);
  fullPrice = signal<number>(-1);

  paymentMethodOptions = Object.values(PaymentMethod);

  watchInfo = signal<WatchFullInfoResponseDTO | null>(null);
  isDatePicker: any;

  datePickerStartDate = signal<Date | null>(null);
  datePickerEndDate = signal<Date | null>(null);

  amoutOfDays = signal<number>(0);

  datePickerStartFinish(date: Date | null) {
    this.datePickerStartDate.set(date);
    this.amoutOfDays.set(0);
  }

  datePickerEndFinish(date: Date | null) {
    this.datePickerEndDate.set(date);

    if (date !== null) {
      this.calculatePrice();
    }
  }

  reloadSummary(type: boolean) {
    if (type === false) { this.closeRentalModal(); }
  }
  closeRentalError() {
    this.rentalError.set(null);
  }
  getWatchInfo() {
    const watch = this.route.snapshot.paramMap.get('id');
    if (watch != null) {
      this.watchesService.getWatch(watch).subscribe(
        response => {
          this.watchInfo.set(response);
        }
      )
    }
    else {
      this.router.navigate(['/']);
    }
  }

  calculatePrice() {
    const startDate = this.datePickerStartDate();
    const endDate = this.datePickerEndDate();
    const watch = this.watchInfo();

    if (startDate === null || endDate === null || watch === null) {
      return;
    }

    if (endDate < startDate) {
      this.amoutOfDays.set(0);
      this.fullPrice.set(-1);
      return;
    }

    const millisecondsPerDay = 1000 * 60 * 60 * 24;

    const days =
      Math.round(
        (endDate.getTime() - startDate.getTime()) / millisecondsPerDay
      ) + 1;

    this.amoutOfDays.set(days);
    this.fullPrice.set(watch.pricePerDay * days);
  }

  showRentalModal = signal(false);

  openRentalModal() {
    if (
      this.datePickerStartDate() === null ||
      this.datePickerEndDate() === null ||
      this.amoutOfDays() <= 0
    ) {
      return;
    }

    this.showRentalModal.set(true);
  }

  closeRentalModal() {
    this.showRentalModal.set(false);
  }

  rentalForm = this.fb.group({
    paymentMethod: [PaymentMethod.CASH, Validators.required]
  })

  ngOnInit() {
    this.getWatchInfo();
  }

  createRental() {

    if (
      this.rentalForm.invalid ||
      this.datePickerStartDate() === null ||
      this.datePickerEndDate() === null ||
      this.watchInfo() === null
    ) {
      return;
    }

    const value = this.rentalForm.getRawValue();

    const rentalRequest: RentalRequestDTO = {
      startDate: this.datePipe.transform(
        this.datePickerStartDate(),
        'yyyy-MM-dd'
      )!,
      endDate: this.datePipe.transform(
        this.datePickerEndDate(),
        'yyyy-MM-dd'
      )!,
      paymentMethod: value.paymentMethod!,
      watchId: this.watchInfo()!.id
    };

    this.rentalsService.createRental(rentalRequest).subscribe({
      next: response => {
        this.router.navigate(['/rentals'], {
          queryParams: {
            isRentalId: 'true',
            rentalId: response.id
          }
        }
        );
      },

      error: err => {
        this.closeRentalModal();

        let message = 'Nie udało się utworzyć wypożyczenia.';

        if (err.error?.message) {
          message = err.error.message;
        } else if (typeof err.error === 'string') {
          try {
            const error = JSON.parse(err.error);
            message = error.message ?? message;
          } catch {
            message = err.error;
          }
        }

        this.rentalForm.reset({
          paymentMethod: PaymentMethod.CASH
        });

        this.datePickerStartDate.set(null);
        this.datePickerEndDate.set(null);
        this.amoutOfDays.set(0);
        this.fullPrice.set(-1);
        
        this.rentalError.set(message);
      }
    });
  }
}
