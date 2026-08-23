import { Component, inject, output, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';
import { WatchCalendar } from '../../shared/components/watch-calendar/watch-calendar';
import { ReactiveFormsModule } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { PaymentMethod } from '../../core/models/rentals/payment-method';
import { date } from '@primeuix/themes/aura/datepicker';
import { DatePipe } from '@angular/common';
import { RentalsService } from '../../core/services/rentals/rentals-service';
import { RentalRequestDTO } from '../../core/models/rentals/rental-request.dto';

// import {}

@Component({
  selector: 'app-rental-create',
  imports: [WatchCalendar,
    ReactiveFormsModule,
    DatePipe],
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

  getWatchInfo() {
    const watch = this.route.snapshot.paramMap.get('id');
    if (watch != null) {
      console.log(watch);
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
    this.showRentalModal.set(true);
  }

  closeRentalModal() {
    this.showRentalModal.set(false);
  }

  rentalForm = this.fb.group({
    paymentMethod: [PaymentMethod.CASH]
  })

  ngOnInit() {
    this.getWatchInfo();
  }

  createRental()
  {
    const paymentMethodForm = this.rentalForm.getRawValue();
    if(paymentMethodForm == null) return;
    const rentalRequest: RentalRequestDTO = {
      startDate: this.datePipe.transform(this.datePickerStartDate(), 'yyyy-MM-dd')!.toString(),
      endDate: this.datePipe.transform(this.datePickerEndDate(), 'yyyy-MM-dd')!.toString(),
      paymentMethod: paymentMethodForm.paymentMethod!,
      watchId: this.watchInfo()!.id
    }

    this.rentalsService.createRental(rentalRequest).subscribe(
      response => {
        console.log(response);
      }
    )
  }
}
