import { Component, inject, input, output, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchFullInfoView } from '../watch-full-info-view/watch-full-info-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-watch-card',
  imports: [WatchFullInfoView,
    ReactiveFormsModule
  ],
  templateUrl: './watch-card-view.html',
  styleUrl: './watch-card-view.css',  
})
export class WatchCard {
  watch = input.required<WatchCardResponseDTO>();


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
