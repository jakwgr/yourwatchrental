import {
  Component,
  inject,
  OnInit,
  signal
} from '@angular/core';

import { WatchesService } from '../../core/services/watches/watches-service';

import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';

import { WatchCard } from '../../shared/components/watch-card-view/watch-card-view';

import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';

import {
  WatchGender,
  WatchGenderLabel
} from '../../core/models/watches/enums/watch-gender';

import {
  WatchMovementType,
  WatchMovementTypeLabel
} from '../../core/models/watches/enums/watch-movement-type';

import {
  WatchStatus,
  WatchStatusLabel
} from '../../core/models/watches/enums/watch-status';

import {
  WatchType,
  WatchTypeLabel
} from '../../core/models/watches/enums/watch-type';

import { BranchesService } from '../../core/services/branches/branches-service';

import { BranchShortResponseDTO } from '../../core/models/branch/branch-short-response.dto';

import {
  ActivatedRoute,
  Router,
  RouterLink
} from '@angular/router';

import { PaginationButtons } from '../../shared/components/pagination-buttons/pagination-buttons';

import { ProfileService } from '../../core/services/profile/profile-service';

import { UserResponseDTO } from '../../core/models/profile/user-response.dto';


@Component({
  selector: 'app-watches',
  imports: [
    WatchCard,
    ReactiveFormsModule,
    PaginationButtons,
    RouterLink
  ],
  templateUrl: './watches.html',
  styleUrl: './watches.css',
})
export class Watches implements OnInit {

  private watchesService = inject(WatchesService);

  private fb = inject(FormBuilder);

  private branchesService = inject(BranchesService);

  private route = inject(ActivatedRoute);

  private router = inject(Router);

  private profileService = inject(ProfileService);

  genderOptions =
    Object.values(WatchGender);

  genderLabels =
    WatchGenderLabel;

  movementTypeOptions =
    Object.values(WatchMovementType);

  movementTypeLabels =
    WatchMovementTypeLabel;

  statusOptions =
    Object.values(WatchStatus);

  statusLabels =
    WatchStatusLabel;

  watchTypeOptions =
    Object.values(WatchType);

  watchTypeLabels =
    WatchTypeLabel;


  filterForm = this.fb.group({

    watchId: [''],

    manufacturer: [''],

    model: [''],

    minPrice:
      this.fb.control<number | null>(null),

    maxPrice:
      this.fb.control<number | null>(null),

    minYear:
      this.fb.control<number | null>(null),

    maxYear:
      this.fb.control<number | null>(null),

    gender:
      this.fb.control<WatchGender | null>(null),

    movementType:
      this.fb.control<WatchMovementType | null>(null),

    status:
      this.fb.control<WatchStatus | null>(null),

    watchType:
      this.fb.control<WatchType | null>(null),

    branchId:
      this.fb.control<string | null>(null)

  });

  watchSerial =
    signal<string | null>(null);

  editWatchPhotos =
    signal<boolean>(false);

  watchId =
    signal<string | null>(null);

  profile =
    signal<UserResponseDTO | null>(null);

  watches =
    signal<WatchCardResponseDTO[]>([]);

  branches =
    signal<BranchShortResponseDTO[]>([]);

  advancedFilters =
    signal<boolean>(false);

  currentPage =
    signal<number>(0);

  totalPages =
    signal<number>(0);


  sort =
    signal<string>('yearOfProduction,desc');

  toggleAdvancedFilters() {

    this.advancedFilters.update(
      value => !value
    );

  }

  changeSort(event: Event) {

    const select =
      event.target as HTMLSelectElement;


    this.sort.set(
      select.value
    );

    this.currentPage.set(0);


    this.router.navigate([], {

      relativeTo: this.route,

      queryParams: {
        sort: this.sort(),
        page: 0
      },

      queryParamsHandling: 'merge'

    });

  }

  branchesGenerate() {

    this.branchesService
      .getBranches()
      .subscribe({

        next: response => {

          this.branches.set(
            response
          );

        },

        error: error => {

          console.error(
            'Błąd podczas pobierania oddziałów',
            error
          );

        }

      });

  }

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

      queryParams: {

        sort: this.sort(),

        page: 0

      }

    });

  }
  ngOnInit() {

    this.route.queryParams.subscribe(params => {

      const editWatchPhotos =
        history.state.editWatchPhotos;

      const watchId =
        history.state.watchId;

      const watchSerial =
        history.state.watchSerial;


      this.editWatchPhotos.set(
        editWatchPhotos
      );

      this.watchId.set(
        watchId
      );

      this.watchSerial.set(
        watchSerial
      );

      this.filterForm.patchValue({

        watchId:
          params['watchId'] ?? null,

        manufacturer:
          params['manufacturer'] ?? '',

        model:
          params['model'] ?? '',

        minPrice:
          params['minPrice']
            ? Number(params['minPrice'])
            : null,

        maxPrice:
          params['maxPrice']
            ? Number(params['maxPrice'])
            : null,

        minYear:
          params['minYear']
            ? Number(params['minYear'])
            : null,

        maxYear:
          params['maxYear']
            ? Number(params['maxYear'])
            : null,

        gender:
          params['gender'] ?? null,

        movementType:
          params['movementType'] ?? null,

        status:
          params['status'] ?? null,

        watchType:
          params['watchType'] ?? null,

        branchId:
          params['branchId'] ?? null

      });


      if (
        this.editWatchPhotos() !== undefined &&
        this.watchId() !== undefined
      ) {

        this.filterForm.patchValue({

          watchId:
            this.watchId()

        });

      }

      const page =
        params['page']
          ? Number(params['page'])
          : 0;


      this.currentPage.set(
        page
      );

      if (

        params['minYear'] ||

        params['maxYear'] ||

        params['gender'] ||

        params['movementType'] ||

        params['status'] ||

        params['watchType'] ||

        params['branchId']

      ) {

        this.advancedFilters.set(true);

      }

      this.loadWatches();

      this.profileService
        .getMyProfile()
        .subscribe({

          next: response => {

            this.profile.set(
              response
            );

          },

          error: error => {

            console.error(
              'Błąd podczas pobierania profilu',
              error
            );

          }

        });

    });


    this.branchesGenerate();

  }


  loadWatches() {

    const filter =
      this.filterForm.getRawValue();


    this.watchesService
      .getWatches(

        this.currentPage(),

        9,

        filter,

        this.sort()

      )
      .subscribe({

        next: response => {

          this.watches.set(
            response.content
          );

          this.totalPages.set(
            response.totalPages
          );

        },

        error: error => {

          console.error(
            'Błąd podczas pobierania zegarków:',
            error
          );

        }

      });

  }

  search() {

    const filter =
      this.filterForm.getRawValue();

    this.currentPage.set(0);


    const queryParams:
      Record<string, string | number> = {};

    Object.entries(filter).forEach(
      ([key, value]) => {

        if (
          value !== null &&
          value !== ''
        ) {

          queryParams[key] =
            value as string | number;

        }

      }
    );

    queryParams['sort'] =
      this.sort();

    queryParams['page'] = 0;

    this.router.navigate([], {

      relativeTo: this.route,

      queryParams

    });

  }

  previousPage() {

    if (
      this.currentPage() <= 0
    ) {

      return;

    }


    const newPage =
      this.currentPage() - 1;


    this.currentPage.set(
      newPage
    );


    this.updatePageInUrl(
      newPage
    );


    this.loadWatches();

  }

  nextPage() {

    if (
      this.currentPage() >=
      this.totalPages() - 1
    ) {

      return;

    }


    const newPage =
      this.currentPage() + 1;


    this.currentPage.set(
      newPage
    );


    this.updatePageInUrl(
      newPage
    );


    this.loadWatches();

  }

  private updatePageInUrl(
    page: number
  ) {

    this.router.navigate([], {

      relativeTo: this.route,

      queryParams: {

        page: page

      },

      queryParamsHandling: 'merge'

    });

  }

}

