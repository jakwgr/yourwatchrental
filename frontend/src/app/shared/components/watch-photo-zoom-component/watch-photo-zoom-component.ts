import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-watch-photo-zoom-component',
  standalone: true,
  imports: [],
  templateUrl: './watch-photo-zoom-component.html',
  styleUrl: './watch-photo-zoom-component.css'
})
export class WatchPhotoZoomComponent {

  photoUrl = input.required<string>();
  alt = input<string>('');

  close = output<void>();
}