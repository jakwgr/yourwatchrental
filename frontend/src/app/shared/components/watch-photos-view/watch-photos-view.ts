import { Component, inject, input, output, signal } from '@angular/core';

import { WatchesService } from '../../../core/services/watches/watches-service';

import { WatchPhotoResponseDTO } from '../../../core/models/watches/photos/watch-photo-response.dto';

import { PhotoType } from '../../../core/models/watches/photos/photo-type';

import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { SmallErrorView } from '../small-error-view/small-error-view';
import { FormError } from '../form-error/form-error';

@Component({
  selector: 'app-watch-photos-view',
  imports: [
    ReactiveFormsModule,
    SmallErrorView,
    FormError
  ],
  templateUrl: './watch-photos-view.html',
  styleUrl: './watch-photos-view.css',
})
export class WatchPhotosView {

  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);

  watchId = input.required<string>();

  PhotoType = PhotoType;

  photos = signal<WatchPhotoResponseDTO[]>([]);

  photoTypes = signal<PhotoType[]>([
    PhotoType.FRONT,
    PhotoType.BACK,
    PhotoType.FULL
  ]);

  watchesError = signal<string | null>(null);

  reload = output<void>();

  closePhotos = output<boolean>();

addPhotos = {

    [PhotoType.FRONT]: this.fb.group({
        file: [null as File | null, Validators.required],
        description: [PhotoType.FRONT as string, Validators.required]
    }),

    [PhotoType.BACK]: this.fb.group({
        file: [null as File | null, Validators.required],
        description: [PhotoType.BACK as string, Validators.required]
    }),

    [PhotoType.FULL]: this.fb.group({
        file: [null as File | null, Validators.required],
        description: [PhotoType.FULL as string, Validators.required]
    })

};

  photosReload() {
  this.watchesError.set(null);

  this.watchesService.getPhotos(this.watchId()).subscribe({
    next: response => {
      this.photos.set(response);
      console.log(response);
    },
    error: err => {
      this.watchesError.set(err.error?.message ?? err.message);
    }
  });
}
  onFileSelected(event: Event, photoType: PhotoType) {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];

  if (!file) {
    return;
  }

  this.addPhotos[photoType].patchValue({
    file: file
  });
}

  add(photoType: PhotoType) {
  const form = this.addPhotos[photoType];
  const value = form.getRawValue();

  const file = value.file;
  const description = value.description;

  if (file == null || description == null) {
    return;
  }

  this.watchesError.set(null);

  this.watchesService
    .uploadPhoto(
      this.watchId(),
      file,
      photoType,
      description
    )
    .subscribe({
      next: response => {
        this.photos.update(photos => [...photos, response]);

        form.reset({
          file: null,
          description: ''
        });

        this.photosReload();
        this.reload.emit();
      },
      error: err => {
        this.watchesError.set(err.error?.message ?? err.message);
      }
    });
}

  delete(id: string) {
    this.watchesError.set(null);

    this.watchesService.deletePhoto(id).subscribe({
      next: () => {
        this.photosReload();

        this.reload.emit();
      },
      error: err => {
        this.watchesError.set(err.error?.message ?? err.message);
      }
    });
  }

  ngOnInit() {
    this.photosReload();
  }

  close() {
    this.closePhotos.emit(true);
  }
}