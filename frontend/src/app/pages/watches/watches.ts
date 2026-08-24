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

import { WatchGender } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus } from '../../core/models/watches/enums/watch-status';
import { WatchType } from '../../core/models/watches/enums/watch-type';

import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchShortResponseDTO } from '../../core/models/branch/branch-short-response.dto';

import { ActivatedRoute, Router } from '@angular/router';

import { PaginationButtons } from '../../shared/components/pagination-buttons/pagination-buttons';

@Component({
  selector: 'app-watches',
  imports: [
    WatchCard,
    ReactiveFormsModule,
    PaginationButtons
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

  // =========================
  // OPCJE FILTRÓW
  // =========================

  genderOptions = Object.values(WatchGender);
  movementTypeOptions = Object.values(WatchMovementType);
  statusOptions = Object.values(WatchStatus);
  watchTypeOptions = Object.values(WatchType);

  // =========================
  // FORMULARZ
  // =========================

  filterForm = this.fb.group({

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

    branchId: ['']

  });

  // =========================
  // SIGNALS
  // =========================

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

      branchId: ''
    });

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

      // -------------------------
      // ODCZYT FILTRÓW Z URL
      // -------------------------

      this.filterForm.patchValue({

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

        branchId: params['branchId'] ?? ''

      });

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

    });

    this.branchesGenerate();

  }

  // =========================
  // POBIERANIE ZEGARKÓW
  // =========================

  loadWatches() {

    const filter = this.filterForm.getRawValue();

    this.watchesService
      .getWatches(
        this.currentPage(),
        12,
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
  // SZUKANIE
  // =========================

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
        10,
        filter
      )
      .subscribe({

        next: response => {

          this.watches.set(response.content);

          this.totalPages.set(response.totalPages);

          setTimeout(() => {

            this.watchesSection.nativeElement.scrollIntoView({
              behavior: 'smooth',
              block: 'start'
            });

          });

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