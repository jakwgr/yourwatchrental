
import { Component, inject, input, signal } from '@angular/core';
import { ProfileService } from '../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../core/models/profile/user-response.dto';
import { RentalsService } from '../../core/services/rentals/rentals-service';
import { RentalResponseDTO } from '../../core/models/rentals/rental-response.dto';
import { RentalView } from '../../shared/components/rental-view/rental-view';
import {
  RentalStatus,
  RentalStatusLabel
} from '../../core/models/rentals/rental-status';
import {
  PaymentStatus,
  PaymentStatusLabel
} from '../../core/models/rentals/payment-status';
import {
  PaymentMethod,
  PaymentMethodLabel
} from '../../core/models/rentals/payment-method';
import {
  FormBuilder,
  ReactiveFormsModule
} from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { PaginationButtons } from '../../shared/components/pagination-buttons/pagination-buttons';
import { role } from '../../core/models/profile/enums/role';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import { RentalFilterRequestDTO } from '../../core/models/rentals/rental-filter-request.dto';
import { AdminService } from '../../core/services/admin/admin-service';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';

@Component({
  selector: 'app-rentals',
  imports: [
    RentalView,
    ReactiveFormsModule,
    PaginationButtons
  ],
  templateUrl: './rentals.html',
  styleUrl: './rentals.css',
})
export class Rentals {

  private profileService = inject(ProfileService);
  private rentalsService = inject(RentalsService);
  private branchesService = inject(BranchesService);
  private watchesService = inject(WatchesService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private adminService = inject(AdminService);
  private fb = inject(FormBuilder);


  // =========================
  // DANE
  // =========================

  watches = signal<WatchCardResponseDTO[]>([]);

  branches = signal<BranchResponseDTO[]>([]);

  profile = signal<UserResponseDTO | null>(null);

  rentals = signal<RentalResponseDTO[]>([]);

  user = signal<UserResponseDTO | null>(null);

  watch = signal<WatchFullInfoResponseDTO | null>(null);


  // =========================
  // PAGINACJA
  // =========================

  currentPage = signal<number>(0);

  totalPages = signal<number>(0);


  // =========================
  // PARAMETRY
  // =========================

  admin = input(false);

  adminId = input<string | null>(null);

  paramId = signal<string | null>(null);

  isWatchId = signal<boolean>(false);

  isRentalId = signal<boolean>(false);

  rentalId = signal<string | null>(null);

  role = role;


  // =========================
  // WSZYSTKIE WYPOŻYCZENIA
  // =========================

  showAll = signal<boolean>(false);


  // =========================
  // FILTRY ZAAWANSOWANE
  // =========================

  advancedFilters = signal<boolean>(false);


  // =========================
  // SORTOWANIE
  // =========================

  sort = signal<string>('createdAt,desc');


  // =========================
  // FORMULARZ
  // =========================

  rentalFilterForm = this.fb.group({

    rentalStatus:
      [null as RentalStatus | null],

    paymentStatus:
      [null as PaymentStatus | null],

    paymentMethod:
      [null as PaymentMethod | null],

    userId:
      [null as string | null],

    watchId:
      [null as string | null],

    branchId:
      [null as string | null],

    startDateFrom:
      [''],

    startDateTo:
      [''],

    endDateFrom:
      [''],

    endDateTo:
      ['']

  });


  // =========================
  // OPCJE SELECTÓW
  // =========================

  rentalStatusOptions =
    Object.values(RentalStatus);

  statusLabel =
    RentalStatusLabel;

  paymentStatusOptions =
    Object.values(PaymentStatus);

  paymentStatusLabel =
    PaymentStatusLabel;

  paymentMethodOptions =
    Object.values(PaymentMethod);

  paymentMethodLabel =
    PaymentMethodLabel;


  // =========================
  // POKAŻ WSZYSTKIE
  // =========================

  showAllRentals() {

    this.showAll.set(true);

    this.currentPage.set(0);


    this.router.navigate([], {

      relativeTo: this.route,

      queryParams: {
        all: 'true',
        sort: this.sort(),
        page: 0
      },

      queryParamsHandling: 'merge'

    });

  }


  // =========================
  // RELOAD
  // =========================

  reloadRental() {

    this.profileService.getMyProfile().subscribe({

      next: profile => {

        this.profile.set(profile);


        // =========================
        // ID Z URL
        // =========================

        const id =
          this.route.snapshot.paramMap.get('id');

        this.paramId.set(id);


        // =========================
        // SORTOWANIE
        // =========================

        const sortValue =
          this.sort();


        // =========================
        // ADMIN
        // =========================

        if (profile.role === role.ADMIN) {


          // =========================
          // KONKRETNE WYPOŻYCZENIE
          // =========================

          if (this.isRentalId()) {

            const rentalId =
              this.rentalId();

            if (rentalId) {

              this.rentalsService
                .getRentalById(rentalId)
                .subscribe({

                  next: response => {

                    this.rentals.set([
                      response
                    ]);

                    this.totalPages.set(1);

                  },

                  error: err => {

                    console.error(
                      'Błąd podczas pobierania wypożyczenia',
                      err
                    );

                    this.rentals.set([]);

                  }

                });

            }

            return;
          }


          // =========================
          // WYPOŻYCZENIA ZEGARKA
          // =========================

          if (this.isWatchId()) {

            if (id) {

              const filter:
                RentalFilterRequestDTO = {
                  watchId: id
                };


              this.watchesService
                .getWatch(id)
                .subscribe({

                  next: response => {

                    this.watch.set(
                      response
                    );

                  },

                  error: err => {

                    console.error(
                      'Błąd podczas pobierania zegarka',
                      err
                    );

                  }

                });


              this.rentalsService
                .getRentalsAdmin(
                  this.currentPage(),
                  15,
                  filter,
                  sortValue
                )
                .subscribe({

                  next: response => {

                    this.rentals.set(
                      response.content
                    );

                    this.totalPages.set(
                      response.totalPages
                    );

                  },

                  error: err => {

                    console.error(
                      'Błąd podczas pobierania wypożyczeń zegarka',
                      err
                    );

                    this.rentals.set([]);

                  }

                });

            }

            return;
          }


          // =========================
          // WYPOŻYCZENIA UŻYTKOWNIKA
          // =========================

          if (id) {

            const filter:
              RentalFilterRequestDTO = {
                userId: id
              };


            this.adminService
              .getUserAdmin(id)
              .subscribe({

                next: response => {

                  this.user.set(
                    response
                  );

                },

                error: err => {

                  console.error(
                    'Błąd podczas pobierania użytkownika',
                    err
                  );

                }

              });


            this.rentalsService
              .getRentalsAdmin(
                this.currentPage(),
                15,
                filter,
                sortValue
              )
              .subscribe({

                next: response => {

                  this.rentals.set(
                    response.content
                  );

                  this.totalPages.set(
                    response.totalPages
                  );

                },

                error: err => {

                  console.error(
                    'Błąd podczas pobierania wypożyczeń użytkownika',
                    err
                  );

                  this.rentals.set([]);

                }

              });

            return;
          }


          // =========================
          // ADMIN
          // =========================
          // all=true → wszystkie
          // all=false → moje
          // =========================

          if (this.showAll()) {

            const filter =
              this.getFilter();


            this.rentalsService
              .getRentalsAdmin(
                this.currentPage(),
                15,
                filter,
                sortValue
              )
              .subscribe({

                next: response => {

                  this.rentals.set(
                    response.content
                  );

                  this.totalPages.set(
                    response.totalPages
                  );

                },

                error: err => {

                  console.error(
                    'Błąd podczas pobierania wszystkich wypożyczeń',
                    err
                  );

                  this.rentals.set([]);

                }

              });

          } else {

            const filter =
              this.getFilter();


            // Admin domyślnie pobiera
            // swoje wypożyczenia

            filter.userId =
              profile.id;


            this.rentalsService
              .getRentalsAdmin(
                this.currentPage(),
                15,
                filter,
                sortValue
              )
              .subscribe({

                next: response => {

                  this.rentals.set(
                    response.content
                  );

                  this.totalPages.set(
                    response.totalPages
                  );

                },

                error: err => {

                  console.error(
                    'Błąd podczas pobierania moich wypożyczeń',
                    err
                  );

                  this.rentals.set([]);

                }

              });

          }

          return;
        }


        // =========================
        // ZWYKŁY UŻYTKOWNIK
        // =========================

        if (this.isRentalId()) {

          const rentalId =
            this.rentalId();

          if (rentalId) {

            this.rentalsService
              .getRentalById(rentalId)
              .subscribe({

                next: response => {

                  this.rentals.set([
                    response
                  ]);

                  this.totalPages.set(1);

                },

                error: err => {

                  console.error(
                    'Błąd podczas pobierania wypożyczenia',
                    err
                  );

                  this.rentals.set([]);

                }

              });

          }

          return;
        }


        // =========================
        // ZWYKŁY UŻYTKOWNIK
        // =========================

        const filter =
          this.getFilter();


        filter.userId = null;


        this.rentalsService
          .getMyRentals(
            this.currentPage(),
            15,
            filter,
            sortValue
          )
          .subscribe({

            next: response => {

              this.rentals.set(
                response.content
              );

              this.totalPages.set(
                response.totalPages
              );

            },

            error: err => {

              console.error(
                'Błąd podczas pobierania moich wypożyczeń',
                err
              );

              this.rentals.set([]);

            }

          });

      },

      error: err => {

        console.error(
          'Błąd podczas pobierania profilu',
          err
        );

      }

    });

  }


  // =========================
  // POBRANIE FILTRA
  // =========================

  private getFilter():
    RentalFilterRequestDTO {

    const value =
      this.rentalFilterForm.getRawValue();


    return {

      rentalStatus:
        value.rentalStatus,

      paymentStatus:
        value.paymentStatus,

      paymentMethod:
        value.paymentMethod,

      userId:
        value.userId,

      watchId:
        value.watchId,

      branchId:
        value.branchId,

      startDateFrom:
        value.startDateFrom || null,

      startDateTo:
        value.startDateTo || null,

      endDateFrom:
        value.endDateFrom || null,

      endDateTo:
        value.endDateTo || null

    };

  }


  // =========================
  // INIT
  // =========================

  ngOnInit() {

    this.route.queryParamMap.subscribe(params => {


      // =========================
      // WATCH
      // =========================

      this.isWatchId.set(
        params.get('watch') === 'true'
      );


      // =========================
      // RENTAL ID
      // =========================

      this.isRentalId.set(
        params.get('isRentalId') === 'true'
      );


      this.rentalId.set(
        params.get('rentalId')
      );


      // =========================
      // ALL
      // =========================

      this.showAll.set(
        params.get('all') === 'true'
      );


      // =========================
      // SORTOWANIE
      // =========================

      this.sort.set(
        params.get('sort')
        ?? 'createdAt,desc'
      );


      // =========================
      // FILTRY
      // =========================

      this.rentalFilterForm.patchValue({

        rentalStatus:
          this.getRentalStatus(
            params.get('rentalStatus')
          ),

        paymentStatus:
          this.getPaymentStatus(
            params.get('paymentStatus')
          ),

        paymentMethod:
          this.getPaymentMethod(
            params.get('paymentMethod')
          ),

        userId:
          params.get('userId'),

        watchId:
          params.get('watchId'),

        branchId:
          params.get('branchId'),

        startDateFrom:
          params.get('startDateFrom')
          ?? '',

        startDateTo:
          params.get('startDateTo')
          ?? '',

        endDateFrom:
          params.get('endDateFrom')
          ?? '',

        endDateTo:
          params.get('endDateTo')
          ?? ''

      });


      // =========================
      // OTWARCIE FILTRÓW
      // =========================

      if (

        params.get('branchId') ||

        params.get('startDateFrom') ||

        params.get('startDateTo') ||

        params.get('endDateFrom') ||

        params.get('endDateTo')

      ) {

        this.advancedFilters.set(
          true
        );

      }


      // =========================
      // POBRANIE DANYCH
      // =========================

      this.reloadRental();

    });


    // =========================
    // ZEGARKI
    // =========================

    this.watchesService
      .getWatches()
      .subscribe({

        next: response => {

          this.watches.set(
            response.content
          );

        },

        error: err => {

          console.error(
            'Błąd podczas pobierania zegarków',
            err
          );

        }

      });


    // =========================
    // ODDZIAŁY
    // =========================

    this.branchesService
      .getBranches()
      .subscribe({

        next: response => {

          this.branches.set(
            response
          );

        },

        error: err => {

          console.error(
            'Błąd podczas pobierania oddziałów',
            err
          );

        }

      });

  }


  // =========================
  // ENUMY Z URL
  // =========================

  private getRentalStatus(
    value: string | null
  ): RentalStatus | null {

    if (!value) {

      return null;

    }

    return Object.values(RentalStatus)
      .includes(value as RentalStatus)

      ? value as RentalStatus

      : null;

  }


  private getPaymentStatus(
    value: string | null
  ): PaymentStatus | null {

    if (!value) {

      return null;

    }

    return Object.values(PaymentStatus)
      .includes(value as PaymentStatus)

      ? value as PaymentStatus

      : null;

  }


  private getPaymentMethod(
    value: string | null
  ): PaymentMethod | null {

    if (!value) {

      return null;

    }

    return Object.values(PaymentMethod)
      .includes(value as PaymentMethod)

      ? value as PaymentMethod

      : null;

  }


  // =========================
  // FILTRY ZAAWANSOWANE
  // =========================

  toggleAdvancedFilters() {

    this.advancedFilters.update(
      value => !value
    );

  }


  // =========================
  // SORTOWANIE
  // =========================

  changeSort(
    event: Event
  ) {

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


  // =========================
  // SZUKANIE
  // =========================

  searchRentals() {

    const filter =
      this.rentalFilterForm.getRawValue();


    const queryParams:
      Record<string, string> = {};


    Object.entries(filter).forEach(
      ([key, value]) => {

        if (
          value !== null &&
          value !== ''
        ) {

          queryParams[key] =
            String(value);

        }

      }
    );


    // =========================
    // WATCH
    // =========================

    if (this.isWatchId()) {

      queryParams['watch'] =
        'true';

    }


    // =========================
    // RENTAL ID
    // =========================

    if (this.isRentalId()) {

      queryParams['isRentalId'] =
        'true';


      const rentalId =
        this.rentalId();

      if (rentalId) {

        queryParams['rentalId'] =
          rentalId;

      }

    }


    // =========================
    // ALL
    // =========================

    if (this.showAll()) {

      queryParams['all'] =
        'true';

    }


    // =========================
    // SORT
    // =========================

    queryParams['sort'] =
      this.sort();


    // =========================
    // PIERWSZA STRONA
    // =========================

    queryParams['page'] =
      '0';


    this.currentPage.set(0);


    // =========================
    // URL
    // =========================

    this.router.navigate([], {

      relativeTo: this.route,

      queryParams

    });

  }


  // =========================
  // RESET FILTRÓW
  // =========================

  resetFilters() {

  // =========================
  // RESET FORMULARZA
  // =========================

  this.rentalFilterForm.reset({

    rentalStatus: null,

    paymentStatus: null,

    paymentMethod: null,

    userId: null,

    watchId: null,

    branchId: null,

    startDateFrom: '',

    startDateTo: '',

    endDateFrom: '',

    endDateTo: ''

  });


  // =========================
  // RESET STANU
  // =========================

  this.advancedFilters.set(false);

  this.currentPage.set(0);


  // =========================
  // WYŁĄCZ "WSZYSTKIE"
  // =========================

  this.showAll.set(false);


  // =========================
  // DOMYŚLNE SORTOWANIE
  // =========================

  this.sort.set('createdAt,desc');


  // =========================
  // POWRÓT DO MOICH WYPOŻYCZEŃ
  // =========================

  this.router.navigate(
    ['/rentals'],
    {
      queryParams: {
        sort: 'createdAt,desc',
        page: 0
      }
    }
  );

}


  // =========================
  // PAGINACJA
  // =========================

  previousPage() {

    if (
      this.currentPage() > 0
    ) {

      this.currentPage.update(
        page => page - 1
      );


      this.router.navigate([], {

        relativeTo: this.route,

        queryParams: {

          page: this.currentPage()

        },

        queryParamsHandling: 'merge'

      });

    }

  }


  nextPage() {

    if (
      this.currentPage() <
      this.totalPages() - 1
    ) {

      this.currentPage.update(
        page => page + 1
      );


      this.router.navigate([], {

        relativeTo: this.route,

        queryParams: {

          page: this.currentPage()

        },

        queryParamsHandling: 'merge'

      });

    }

  }

}
