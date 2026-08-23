import { Component, inject, input, output, signal } from '@angular/core';
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

  private watchesService = inject(WatchesService);

  watch = input.required<WatchFullInfoResponseDTO>();
  isDatePicker = input.required<boolean>();

  currentDate = signal(new Date());

  days: (number | null)[] = [];

  selectedDateStart = signal<Date | null>(null);
  selectedDateEnd = signal<Date | null>(null);

  datePickerDays = signal<number>(0);
  datePickerMaxEnd = signal<Date | null>(null);

  unavailableDays = signal<number[]>([]);
  unavailablePeriods = signal<RentalPeriodResponseDTO[]>([]);

  datePickerStartFinish = output<Date | null>();
  datePickerEndFinish = output<Date | null>();

  summaryRental = output<boolean>();

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

    const availabilityEndDate = new Date(lastDayOfMonth);

    availabilityEndDate.setDate(
      availabilityEndDate.getDate() + 21
    );

    this.watchesService.getWatchAvailability(
      this.watch().id,
      this.formatDate(firstDayOfMonth),
      this.formatDate(availabilityEndDate)
    ).subscribe(response => {

      this.unavailablePeriods.set(
        response.unavailablePeriods
      );

      const unavailableDays: number[] = [];

      for (let i = 1; i <= amountOfDays; i++) {

        const date = new Date(
          firstDayOfMonth.getFullYear(),
          firstDayOfMonth.getMonth(),
          i
        );

        const dateString = this.formatDate(date);

        if (
          response.unavailablePeriods.some(rental =>
            dateString >= rental.startDate &&
            dateString <= rental.endDate
          )
        ) {
          unavailableDays.push(i);
        }
      }

      this.unavailableDays.set(unavailableDays);
    });
  }

  isPastDay(day: number | null): boolean {

    if (day === null) {
      return false;
    }

    const today = new Date();

    today.setHours(0, 0, 0, 0);

    const date = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    return date <= today;
  }

  isRented(day: number | null): boolean {

    if (day === null) {
      return false;
    }

    const date = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    const dateString = this.formatDate(date);

    return this.unavailablePeriods().some(rental =>
      dateString >= rental.startDate &&
      dateString <= rental.endDate
    );
  }

  isBlocked(day: number | null): boolean {

    return this.isPastDay(day) || this.isRented(day);
  }

  monthBefore() {

    const today = new Date();

    const currentMonth = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      1
    );

    const todayMonth = new Date(
      today.getFullYear(),
      today.getMonth(),
      1
    );

    if (currentMonth <= todayMonth) {
      return;
    }

    const date = new Date(this.currentDate());

    date.setMonth(
      date.getMonth() - 1
    );

    this.currentDate.set(date);

    this.generateDays();
  }

  monthAfter() {

    const date = new Date(this.currentDate());

    date.setMonth(
      date.getMonth() + 1
    );

    this.currentDate.set(date);

    this.generateDays();
  }

  generateDays() {

    this.days = [];

    const date = new Date(this.currentDate());

    const firstDayOfMonth = new Date(
      date.getFullYear(),
      date.getMonth(),
      1
    );

    const lastDayOfMonth = new Date(
      date.getFullYear(),
      date.getMonth() + 1,
      0
    );

    const amountOfDays = lastDayOfMonth.getDate();

    const emptyDays =
      firstDayOfMonth.getDay() === 0
        ? 6
        : firstDayOfMonth.getDay() - 1;

    for (let i = 0; i < emptyDays; i++) {
      this.days.push(null);
    }

    for (let i = 1; i <= amountOfDays; i++) {
      this.days.push(i);
    }

    this.datePickerDays.set(
      amountOfDays
    );

    this.loadUnavailableDays(
      firstDayOfMonth,
      lastDayOfMonth,
      amountOfDays
    );
  }

  datePickerStart(day: number | null) {

    if (!this.isDatePicker()) {
      return;
    }

    if (
      day === null ||
      this.isBlocked(day)
    ) {
      return;
    }

    if (
      this.selectedDateStart() !== null
    ) {
      return;
    }

    const startDate = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    this.selectedDateStart.set(
      startDate
    );

    const maximumDate = new Date(
      startDate
    );

    maximumDate.setDate(
      maximumDate.getDate() + 20
    );

    let firstUnavailableDate: Date | null = null;

    for (
      const period of this.unavailablePeriods()
    ) {

      const rentalStart = new Date(
        period.startDate + 'T00:00:00'
      );

      if (
        rentalStart > startDate &&
        (
          firstUnavailableDate === null ||
          rentalStart < firstUnavailableDate
        )
      ) {
        firstUnavailableDate = rentalStart;
      }
    }

    let maxEndDate = maximumDate;

    if (
      firstUnavailableDate !== null &&
      firstUnavailableDate <= maximumDate
    ) {

      maxEndDate = new Date(
        firstUnavailableDate
      );

      maxEndDate.setDate(
        maxEndDate.getDate() - 1
      );
    }

    this.datePickerMaxEnd.set(
      maxEndDate
    );
  }

  datePickerEnd(day: number | null) {

    if (!this.isDatePicker()) {
      return;
    }

    if (
      day === null ||
      this.isBlocked(day)
    ) {
      return;
    }

    const startDate =
      this.selectedDateStart();

    const maxEndDate =
      this.datePickerMaxEnd();

    if (
      startDate === null ||
      maxEndDate === null ||
      this.selectedDateEnd() !== null
    ) {
      return;
    }

    const endDate = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    if (
      endDate < startDate ||
      endDate > maxEndDate
    ) {
      return;
    }

    this.selectedDateEnd.set(
      endDate
    );

    this.datePickerStartFinish.emit(
      startDate
    );

    this.datePickerEndFinish.emit(
      endDate
    );

    this.summaryRental.emit(
      true
    );
  }

  isDayInAllowedRange(
    day: number | null
  ): boolean {

    if (day === null) {
      return false;
    }

    const startDate =
      this.selectedDateStart();

    const maxEndDate =
      this.datePickerMaxEnd();

    if (
      startDate === null ||
      maxEndDate === null
    ) {
      return false;
    }

    const date = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    return (
      date >= startDate &&
      date <= maxEndDate &&
      !this.isBlocked(day)
    );
  }

  isDaySelected(
    day: number | null
  ): boolean {

    if (day === null) {
      return false;
    }

    const startDate =
      this.selectedDateStart();

    const endDate =
      this.selectedDateEnd();

    if (
      startDate === null ||
      endDate === null
    ) {
      return false;
    }

    const date = new Date(
      this.currentDate().getFullYear(),
      this.currentDate().getMonth(),
      day
    );

    return (
      date >= startDate &&
      date <= endDate
    );
  }

  datePickerClick(
    day: number | null
  ) {

    if (
      this.selectedDateStart() === null
    ) {
      this.datePickerStart(day);
    } else {
      this.datePickerEnd(day);
    }
  }

  datePickerReset() {

    if (!this.isDatePicker()) {
      return;
    }

    this.selectedDateStart.set(null);
    this.selectedDateEnd.set(null);
    this.datePickerMaxEnd.set(null);

    this.datePickerStartFinish.emit(null);
    this.datePickerEndFinish.emit(null);

    this.summaryRental.emit(false);
  }

  ngOnInit() {

    this.generateDays();

    this.summaryRental.emit(false);
  }
}