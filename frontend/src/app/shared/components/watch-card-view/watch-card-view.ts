import { Component, inject, input, output, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchFullInfoView } from '../watch-full-info-view/watch-full-info-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { WatchesService } from '../../../core/services/watches/watches-service';
import { WatchPhotoRequestDTO } from '../../../core/models/watches/photos/watch-photo-request.dto';
import { WatchPhotoResponseDTO } from '../../../core/models/watches/photos/watch-photo-response.dto';

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
  wathcesService = inject(WatchesService);

  watch = input.required<WatchCardResponseDTO>();

  watchPhoto = signal<WatchPhotoResponseDTO | null>(null);
  photoLoaded = signal(false);

  ngOnInit() {
    this.wathcesService.getThumbnail(this.watch().id).subscribe(
      {
        next: response => {
          this.watchPhoto.set(response);
          this.photoLoaded.set(true);
        },
        error: err => {
          console.log("no photos for watch " + this.watch().id);
          this.photoLoaded.set(true);
        }
      }
    );
  }

  onWatchUpdated() {
    this.watchUpdated.emit();
  }

  showFullInfoModal = signal(false);
  closeFullInfoModal() {
    this.showFullInfoModal.set(false);
  }
  openFullInfoModal() {
    this.showFullInfoModal.set(true);
  }
}
