import { Component, inject, input, output, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchFullInfoView } from '../watch-full-info-view/watch-full-info-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
output
@Component({
  selector: 'app-watch-card',
  imports: [WatchFullInfoView,
    ReactiveFormsModule
  ],
  templateUrl: './watch-card-view.html',
  styleUrl: './watch-card-view.css',  
})
export class WatchCard {
  watchUpdated = output<void>();

  watch = input.required<WatchCardResponseDTO>();

  onWatchUpdated() {
    this.watchUpdated.emit();
}

  showFullInfoModal = signal(false);
  closeFullInfoModal()
  {
    this.showFullInfoModal.set(false);
  }
  openFullInfoModal()
  {
    this.showFullInfoModal.set(true);
  }
}
