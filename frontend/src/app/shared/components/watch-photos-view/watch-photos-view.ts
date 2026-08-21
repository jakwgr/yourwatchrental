import { Component, computed, inject, input, output, signal } from '@angular/core';
import { WatchesService } from '../../../core/services/watches/watches-service';
import { WatchPhotoResponseDTO } from '../../../core/models/watches/photos/watch-photo-response.dto';
import { PhotoType } from '../../../core/models/watches/photos/photo-type';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-watch-photos-view',
  imports: [ReactiveFormsModule],
  templateUrl: './watch-photos-view.html',
  styleUrl: './watch-photos-view.css',
})
export class WatchPhotosView {
  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);

  watchId = input.required<string>();

  PhotoType = PhotoType;

  photos = signal<WatchPhotoResponseDTO[]>([]);
  photoTypes = signal<PhotoType[]>([PhotoType.FRONT, PhotoType.BACK, PhotoType.FULL])
  reload = output<void>();

  addPhoto = this.fb.group({
    file: [null as File | null],
    description: ['']
  })

  photosReload() {
    this.watchesService.getPhotos(this.watchId()).subscribe(response => {
      this.photos.set(response);
      console.log(response);

      this.addPhoto.reset({
      file: null,
      description: ''
    });
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    this.addPhoto.patchValue({
      file: file
    });
  }
  add(photoType: PhotoType) {
    const value = this.addPhoto.getRawValue();

    const file = value.file;
    const description = value.description;

    if (file == null) {
      return;
    }

    if (description == null) {
      return;
    }

    console.log("file:", file);
    console.log("photoType:", photoType);
    console.log("description:", description);

    this.watchesService
      .uploadPhoto(
        this.watchId(),
        file,
        photoType,
        description
      )
      .subscribe(response => {
        this.photos.update(photos => [...photos, response]);
        this.photosReload();

        this.reload.emit();
      });
  }

  delete(id: string) {
    this.watchesService.deletePhoto(id).subscribe(() => {
      this.photosReload();
      this.reload.emit();
    }
    )
  }

  ngOnInit() {
    this.photosReload();
  }
}
