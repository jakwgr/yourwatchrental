import { Component, inject, OnInit, signal } from '@angular/core';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { WatchGender } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus } from '../../core/models/watches/enums/watch-status';
import { WatchType } from '../../core/models/watches/enums/watch-type';
@Component({
  selector: 'app-watches',
  imports: [WatchCard, ReactiveFormsModule],
  templateUrl: './watches.html',
  styleUrl: './watches.css',
})
export class Watches implements OnInit {

  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);

  genderOptions = Object.values(WatchGender);
  movementTypeOptions = Object.values(WatchMovementType);
  statusOptions = Object.values(WatchStatus);
  watchTypeOptions = Object.values(WatchType);

  filterForm = this.fb.group({
    manufacturer: [''],
    model: [''],
    minPrice: [null],
    maxPrice: [null],
    minYear: [null],
    maxYear: [null],
    gender: [null],
    movementType: [null],
    status: [null],
    watchType: [null]
});

  watches = signal<WatchCardResponseDTO[]>([]);

    ngOnInit() {
      this.watchesService.getWatches().subscribe(response => {

        this.watches.set(response.content);

        console.log("test" + this.watches.length)
      }
    );
  }

  search() {
    const filter = this.filterForm.getRawValue();

    this.watchesService.getWatches(0, 10, filter).subscribe({
        next: response => {
            console.log('NOWA ODPOWIEDŹ:', response.content);
            this.watches.set(response.content);
        },
        error: error => {
            console.error('BŁĄD:', error);
        }
    });
    }
  }
  

