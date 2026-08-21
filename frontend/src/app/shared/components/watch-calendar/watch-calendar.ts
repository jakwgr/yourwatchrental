import { Component, inject, input, output, signal } from '@angular/core';
import { first } from 'rxjs';
import { RentalPeriodResponseDTO } from '../../../core/models/watches/rental-peroid-response.dto';
import { WatchFullInfoResponseDTO } from '../../../core/models/watches/watch-full-info-response.dto';
import { WatchesService } from '../../../core/services/watches/watches-service';

@Component({
  selector: 'app-watch-calendar',
  imports: [],
  templateUrl: './watch-calendar.html',
  styleUrl: './watch-calendar.css',
})
export class WatchCalendar {
  currentDate = signal(new Date());
  days: (number | null)[] = [];
  private watchesService = inject(WatchesService);

  selectedDayStart = signal<number | null>(null);
  selectedDayEnd = signal<number | null>(null);

  datePickerDays = signal<number>(0);
  datePickerMaxEnd = signal<number>(0);
  datePickerFinish = signal<number>(-1);

  watch = input.required<WatchFullInfoResponseDTO>();
  isDatePicker = input.required<boolean>();

  unavailableDays = signal<number[]>([]);

  datePickerStartFinish = output<number>();
  datePickerEndFinish = output<number>();

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  loadUnavailableDays(
    firstDayOfMonth: Date,
    lastDayOfMonth: Date,
    amountOfDays: number
  ) {

    this.watchesService.getWatchAvailability(
      this.watch()?.id,
      this.formatDate(firstDayOfMonth),
      this.formatDate(lastDayOfMonth)
    ).subscribe(response => {
      this.unavailableDays.set([]);
      const unavailableDays: number[] = [];
      console.log(this.formatDate(firstDayOfMonth),
        this.formatDate(lastDayOfMonth));
      for (var i: number = 0; i < amountOfDays; i++) {
        const date = new Date(
          lastDayOfMonth.getFullYear(),
          lastDayOfMonth.getMonth(),
          i
        )
        if (
          response.unavailablePeriods.some(rental =>
            this.formatDate(date) >= rental.startDate &&
            this.formatDate(date) <= rental.endDate)
        ) {
          unavailableDays.push(i);
        }
      }
      console.log(this.unavailableDays);
      this.unavailableDays.set(unavailableDays);
    })
  }

  isRented(day: number): boolean {
    return this.unavailableDays().includes(day);
  }

  monthBefore() {
    const date = new Date(this.currentDate());
    date.setMonth(date.getMonth() - 1);
    this.currentDate.set(date);
    this.generateDays();
  }

  monthAfter() {
    const date = new Date(this.currentDate());
    date.setMonth(date.getMonth() + 1);
    this.currentDate.set(date);
    this.generateDays();
  }

  generateDays() {
    this.days = [];

    const date = new Date(this.currentDate());

    const lastDayOfMonth = new Date(
      date.getFullYear(),
      date.getMonth() + 1,
      0
    );

    const amountOfDays = lastDayOfMonth.getDate();

    const firstDayOfMonth = new Date(
      date.getFullYear(),
      date.getMonth(),
      1
    );

    const emptyDays = firstDayOfMonth.getDay() === 0
      ? 6
      : firstDayOfMonth.getDay() - 1;


    for (var i = 0; i < emptyDays; i++) {
      this.days.push(null)
    }
    for (var i = 0; i < amountOfDays; i++) {
      this.days.push(i + 1);
    }
    this.datePickerDays.set(amountOfDays);
    console.log(this.datePickerDays());
    this.loadUnavailableDays(firstDayOfMonth, lastDayOfMonth, amountOfDays);
  }

  datePickerStart(day: number | null) {
    if(this.isDatePicker() == false) return
    const date = new Date(this.currentDate());
    if (!day || this.datePickerDays() === 0) {
      return

    }
    if (this.isRented(day)) {

    } else {

      if (this.selectedDayStart() == null) {

        this.selectedDayStart.set(day);
        for (var i = day; i < this.datePickerDays(); i++) {

          if (this.unavailableDays().includes(i)) {
            console.log("tych dni nie ma " + this.unavailableDays());
            this.datePickerMaxEnd.set(i - 1);
            break;
          }
        }
        if(this.datePickerMaxEnd() == 0) this.datePickerMaxEnd.set(this.datePickerDays());
        console.log(this.datePickerMaxEnd());
        console.log(this.selectedDayStart());
      }
    }
  }

  datePickerEnd(day: number | null) {
    if(this.isDatePicker() == false) return
    if (!day || this.datePickerDays() === 0 || this.datePickerMaxEnd() === 0) {
      return
    }
    if (this.isRented(day)) {

    } else {
      if (this.selectedDayStart() != null && this.selectedDayEnd() == null) {
        if (day <= this.datePickerMaxEnd() && day >= this.selectedDayStart()!) {
          this.selectedDayEnd.set(day);
          this.datePickerFinish.set(day + 1)

          console.log(this.selectedDayEnd());

          this.datePickerStartFinish.emit(this.selectedDayStart()!);
          this.datePickerEndFinish.emit(this.selectedDayEnd()!);
        }
        else {
          return
        }
      };
    }
  }

  datePickerReset()
  {
    if(this.isDatePicker() == false) return
    this.selectedDayStart.set(null);
    this.selectedDayEnd.set(null);
    this.datePickerMaxEnd.set(0);
    this.datePickerFinish.set(-1);

    this.datePickerStartFinish.emit(-1);
    this.datePickerEndFinish.emit(-1);
  }

  ngOnInit() {
    this.days = [];
    const date = new Date(this.currentDate());

    this.generateDays();
  }

}
