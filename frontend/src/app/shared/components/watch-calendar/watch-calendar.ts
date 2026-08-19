import { Component, inject, input, signal } from '@angular/core';
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

  // rental = input.required<RentalPeriodResponseDTO[]>();
  watch = input.required<WatchFullInfoResponseDTO>();

  unavailableDays = signal<number[]>([]);

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

        // console.log(this.formatDate(date));

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
    this.loadUnavailableDays(firstDayOfMonth, lastDayOfMonth, amountOfDays);
  }

  ngOnInit() {
    this.days = [];
    const date = new Date(this.currentDate());

    this.generateDays();
  }

}
