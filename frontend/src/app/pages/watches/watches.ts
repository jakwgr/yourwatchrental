import {
  Component,
  inject,
  OnInit,
  signal,
  ElementRef,
  ViewChild
} from '@angular/core';

import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';

import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';

import { WatchGender, WatchGenderLabel } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType, WatchMovementTypeLabel } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus, WatchStatusLabel } from '../../core/models/watches/enums/watch-status';
import { WatchType, WatchTypeLabel } from '../../core/models/watches/enums/watch-type';

import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchShortResponseDTO } from '../../core/models/branch/branch-short-response.dto';

import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { PaginationButtons } from '../../shared/components/pagination-buttons/pagination-buttons';
import { ProfileService } from '../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../core/models/profile/user-response.dto';
import { single } from 'rxjs';

@Component({
  selector: 'app-watches',
  imports: [
    WatchCard,
    ReactiveFormsModule,
    PaginationButtons, RouterLink
  ],
  templateUrl: './watches.html',
  styleUrl: './watches.css',
})
export class Watches implements OnInit {

  @ViewChild('watchesSection')
  watchesSection!: ElementRef;


  private watchesService = inject(WatchesService);
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private profileService = inject(ProfileService);

  // =========================
  // OPCJE FILTRÓW
  // =========================

  genderOptions = Object.values(WatchGender);
  genderLabels = WatchGenderLabel;

  movementTypeOptions = Object.values(WatchMovementType);
  movementTypeLabels = WatchMovementTypeLabel;
  
  statusOptions = Object.values(WatchStatus);
  statusLabels = WatchStatusLabel;

  watchTypeOptions = Object.values(WatchType);
  watchTypeLabels = WatchTypeLabel;


  // =========================
  // FORMULARZ
  // =========================

  filterForm = this.fb.group({

    watchId: [''],
    manufacturer: [''],
    model: [''],

    minPrice: this.fb.control<number | null>(null),
    maxPrice: this.fb.control<number | null>(null),

    minYear: this.fb.control<number | null>(null),
    maxYear: this.fb.control<number | null>(null),

    gender: this.fb.control<WatchGender | null>(null),
    movementType: this.fb.control<WatchMovementType | null>(null),
    status: this.fb.control<WatchStatus | null>(null),
    watchType: this.fb.control<WatchType | null>(null),

    branchId: this.fb.control<string | null>(null),

  });

  // =========================
  // SIGNALS
  // =========================

  watchSerial = signal<string | null>(null);
  editWatchPhotos = signal<boolean>(false);
  watchId = signal<string | null>(null);
  profile = signal<UserResponseDTO | null>(null);
  watches = signal<WatchCardResponseDTO[]>([]);

  branches = signal<BranchShortResponseDTO[]>([]);

  advancedFilters = signal(false);

  currentPage = signal(0);

  totalPages = signal(0);

  // =========================
  // FILTRY ZAAWANSOWANE
  // =========================

  toggleAdvancedFilters() {
    this.advancedFilters.update(value => !value);
  }

  // =========================
  // BRANCHES
  // =========================

  branchesGenerate() {

    this.branchesService.getBranches().subscribe({
      next: response => {
        this.branches.set(response);
      }
    });

  }

  // =========================
  // RESET FILTRÓW
  // =========================

  resetFilters() {

    this.filterForm.reset({
      watchId: '',
      manufacturer: '',
      model: '',

      minPrice: null,
      maxPrice: null,

      minYear: null,
      maxYear: null,

      gender: null,
      movementType: null,
      status: null,
      watchType: null,

      branchId: null
    });

    this.editWatchPhotos.set(false);
          this.watchId.set(null);
      this.watchSerial.set(null);

      history.replaceState(
  {
    ...history.state,
    editWatchPhotos: undefined,
    watchId: undefined,
    watchSerial: undefined
  },
  ''
);

    this.currentPage.set(0);

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {}
    });

    this.loadWatches();

  }

  // =========================
  // INIT
  // =========================

  ngOnInit() {

    this.route.queryParams.subscribe(params => {

      const editWatchPhotos = history.state.editWatchPhotos;
      const watchId = history.state.watchId;
      const watchSerial = history.state.watchSerial;

      this.editWatchPhotos.set(editWatchPhotos);
      this.watchId.set(watchId);
      this.watchSerial.set(watchSerial);
      // -------------------------
      // ODCZYT FILTRÓW Z URL
      // -------------------------

      this.filterForm.patchValue({
        watchId: params['watchId'] ?? null,

        manufacturer: params['manufacturer'] ?? '',
        model: params['model'] ?? '',

        minPrice: params['minPrice']
          ? Number(params['minPrice'])
          : null,

        maxPrice: params['maxPrice']
          ? Number(params['maxPrice'])
          : null,

        minYear: params['minYear']
          ? Number(params['minYear'])
          : null,

        maxYear: params['maxYear']
          ? Number(params['maxYear'])
          : null,

        gender: params['gender'] ?? null,

        movementType: params['movementType'] ?? null,

        status: params['status'] ?? null,

        watchType: params['watchType'] ?? null,

        branchId: params['branchId'] ?? null

      });
        console.log("test b1")

      if(this.editWatchPhotos() != undefined && this.watchId() != undefined)
      {
        console.log("test b2")
        this.filterForm.patchValue(
          {
            watchId: this.watchId()
          }
        )
      }

      // -------------------------
      // ODCZYT STRONY Z URL
      // -------------------------

      const page = params['page']
        ? Number(params['page'])
        : 0;

      this.currentPage.set(page);

      // -------------------------
      // POBRANIE ZEGARKÓW
      // -------------------------

      this.loadWatches();

            this.profileService.getMyProfile().subscribe(
        response => {
          console.log("jest konto");
          this.profile.set(response);

        }
      )

    });

    this.branchesGenerate();

  }

  // =========================
  // POBIERANIE ZEGARKÓW
  // =========================

  loadWatches() {

    const filter = this.filterForm.getRawValue();

    console.log(filter);
    this.watchesService
      .getWatches(
        this.currentPage(),
        9,
        filter
      )
      .subscribe({

        next: response => {

          this.watches.set(response.content);

          this.totalPages.set(response.totalPages);

        },

        error: error => {
          console.error('BŁĄD:', error);
        }

      });

  }

  search() {

    const filter = this.filterForm.getRawValue();

    // Po nowym wyszukiwaniu zawsze zaczynamy od strony 0
    this.currentPage.set(0);

    const queryParams: any = {};

    Object.entries(filter).forEach(([key, value]) => {

      if (value !== null && value !== '') {
        queryParams[key] = value;
      }

    });

    // Dodajemy stronę tylko jeśli jest potrzebna
    queryParams.page = 0;

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: queryParams
    });

    this.watchesService
      .getWatches(
        0,
        9,
        filter
      )
      .subscribe({

        next: response => {

          this.watches.set(response.content);

          this.totalPages.set(response.totalPages);

        },

        error: error => {
          console.error('BŁĄD:', error);
        }

      });

  }

  // =========================
  // POPRZEDNIA STRONA
  // =========================

  previousPage() {

    if (this.currentPage() <= 0) {
      return;
    }

    const newPage = this.currentPage() - 1;

    this.currentPage.set(newPage);

    this.updatePageInUrl(newPage);

    this.loadWatches();

  }

  // =========================
  // NASTĘPNA STRONA
  // =========================

  nextPage() {

    if (this.currentPage() >= this.totalPages() - 1) {
      return;
    }

    const newPage = this.currentPage() + 1;

    this.currentPage.set(newPage);

    this.updatePageInUrl(newPage);

    this.loadWatches();

  }

  // =========================
  // ZMIANA STRONY W URL
  // =========================

  private updatePageInUrl(page: number) {

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page
      },
      queryParamsHandling: 'merge'
    });

  }



}