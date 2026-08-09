import { Component, input } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';

@Component({
  selector: 'app-watch-card',
  imports: [],
  templateUrl: './watch-card.html',
  styleUrl: './watch-card.css',
})
export class WatchCard {
  watch = input.required<WatchCardResponseDTO>();
}
