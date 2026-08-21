import { Component, inject, OnInit, signal } from '@angular/core';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { WatchGender } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus } from '../../core/models/watches/enums/watch-status';
import { WatchType } from '../../core/models/watches/enums/watch-type';
import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchShortResponseDTO } from '../../core/models/branch/branch-short-response.dto';
import { ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs';
import { WatchFilterRequestDTO } from '../../core/models/watches/watch-filter-request.dto';

@Component({
  selector: 'app-watches',
  imports: [WatchCard, ReactiveFormsModule],
  templateUrl: './watches.html',
  styleUrl: './watches.css',
})
export class Watches implements OnInit {

  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private route = inject(ActivatedRoute);

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
    watchType: [null],
    branchId: ['']
  });

  watches = signal<WatchCardResponseDTO[]>([]);
  branches = signal<BranchShortResponseDTO[]>([]);

  branch = signal<string | null>(null);

  branchesGenerate() {
    this.branchesService.getBranches().subscribe(
      response => {
        this.branches.set(response);
      }
    )
  }

  ngOnInit() {
    const branchId = this.route.snapshot.queryParams['branchId'];

    const filter: WatchFilterRequestDTO = {};

    if (branchId) {
      filter.branchId = branchId;
      this.filterForm.patchValue({
        branchId: branchId ?? null
    });
    }
    this.watchesService.getWatches(0, 10, filter).subscribe(response => {

      this.watches.set(response.content);
    }
    );
    this.branchesGenerate();
  }

  loadWatches()
  {
    const filter = this.filterForm.getRawValue();

    this.watchesService.getWatches(0, 10, filter).subscribe(response => {

      this.watches.set(response.content);
    }
  )
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


