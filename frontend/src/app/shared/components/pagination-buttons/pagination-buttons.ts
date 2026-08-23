import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination-buttons',
  imports: [],
  templateUrl: './pagination-buttons.html',
  styleUrl: './pagination-buttons.css',
})
export class PaginationButtons {

  currentPage = input.required<number>();
  totalPages = input.required<number>();

  previous = output<void>();
  next = output<void>();

  previousPage() {
    if (this.currentPage() > 0) {
      this.previous.emit();
    }
  }

  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.next.emit();
    }
  }
}