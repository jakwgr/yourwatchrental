import { Component, inject, input, signal } from '@angular/core';
import { AuthService } from '../../core/services/auth/auth-service';
import { ProfileService } from '../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../core/models/profile/user-response.dto';
import { RentalsService } from '../../core/services/rentals/rentals-service';
import { RentalResponseDTO } from '../../core/models/rentals/rental-response.dto';
import { RentalView } from '../../shared/components/rental-view/rental-view';
import { RentalStatus } from '../../core/models/rentals/rental-status';
import { PaymentStatus } from '../../core/models/rentals/payment-status';
import { PaymentMethod } from '../../core/models/rentals/payment-method';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchCardResponseDTO } from '../../core/models/watches/watch-card-response.dto';
import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { PaginationButtons } from '../../shared/components/pagination-buttons/pagination-buttons';
import { role } from '../../core/models/profile/enums/role';
import { ActivatedRoute, Router } from '@angular/router';
import { RentalFilterRequestDTO } from '../../core/models/rentals/rental-filter-request.dto';
import { AdminService } from '../../core/services/admin/admin-service';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';

@Component({
  selector: 'app-rentals',
  imports: [RentalView, ReactiveFormsModule, PaginationButtons],
  templateUrl: './rentals.html',
  styleUrl: './rentals.css',
})
export class Rentals {

  private authService = inject(AuthService);
  private profileService = inject(ProfileService);
  private rentalsService = inject(RentalsService);
  private branchesService = inject(BranchesService);
  private watchesService = inject(WatchesService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private adminService = inject(AdminService);

  watches = signal<WatchCardResponseDTO[]>([]);
  branches = signal<BranchResponseDTO[]>([]);

  private fb = inject(FormBuilder);

  profile = signal<UserResponseDTO | null>(null);
  rentals = signal<RentalResponseDTO[]>([]);

  currentPage = signal(0);
  totalPages = signal(0);

  admin = input(false);

  paramId = signal<string | null>(null);

  role = role;

  adminId = input<string | null>(null);

  user = signal<UserResponseDTO | null>(null);

  watch = signal<WatchFullInfoResponseDTO | null>(null);

  isWatchId = signal<boolean>(false);


  rentalFilterForm = this.fb.group({
    rentalStatus: [null as RentalStatus | null],
    paymentStatus: [null as PaymentStatus | null],
    paymentMethod: [null as PaymentMethod | null],
    userId: [null as string | null],
    watchId: [null as string | null],
    branchId: [null as string | null],
    startDateFrom: [''],
    startDateTo: [''],
    endDateFrom: [''],
    endDateTo: ['']
  });


  rentalStatusOptions = Object.values(RentalStatus);
  paymentStatusOptions = Object.values(PaymentStatus);
  paymentMethodOptions = Object.values(PaymentMethod);


  /*
   * GŁÓWNE POBIERANIE WYPOŻYCZEŃ
   */
  reloadRental() {

    this.profileService.getMyProfile().subscribe({

      next: response => {

        this.profile.set(response);

        /*
         * Pobieramy ID bezpośrednio z aktualnego URL.
         *
         * Dzięki temu nie musimy robić kolejnego
         * route.paramMap.subscribe() za każdym razem.
         */
        const id = this.route.snapshot.paramMap.get('id');

        this.paramId.set(id);


        /*
         * ADMIN
         */
        if (this.profile()?.role === role.ADMIN) {

          /*
           * Wypożyczenia konkretnego zegarka
           */
          if (this.isWatchId()) {

            if (id != null) {

              const filter: RentalFilterRequestDTO = {
                watchId: id
              };

              /*
               * Pobierz informacje o zegarku
               */
              this.watchesService.getWatch(id).subscribe({
                next: response => {
                  this.watch.set(response);
                },
                error: err => {
                  console.error(
                    "Błąd podczas pobierania zegarka",
                    err
                  );
                }
              });


              /*
               * Pobierz wypożyczenia tego zegarka
               */
              this.rentalsService.getRentalsAdmin(
                this.currentPage(),
                15,
                filter
              ).subscribe({

                next: response => {

                  this.rentals.set(response.content);

                  this.totalPages.set(response.totalPages);

                },

                error: err => {
                  console.error(
                    "Błąd podczas pobierania wypożyczeń",
                    err
                  );
                }

              });

            }

          }

          /*
           * Wypożyczenia konkretnego użytkownika
           */
          else if (id != null) {

            const filter: RentalFilterRequestDTO = {
              userId: id
            };


            /*
             * Pobierz użytkownika
             */
            this.adminService.getUserAdmin(id).subscribe({
              next: response => {
                this.user.set(response);
              },
              error: err => {
                console.error(
                  "Błąd podczas pobierania użytkownika",
                  err
                );
              }
            });


            /*
             * Pobierz wypożyczenia użytkownika
             */
            this.rentalsService.getRentalsAdmin(
              this.currentPage(),
              15,
              filter
            ).subscribe({

              next: response => {

                this.rentals.set(response.content);

                this.totalPages.set(response.totalPages);

              },

              error: err => {
                console.error(
                  "Błąd podczas pobierania wypożyczeń",
                  err
                );
              }

            });

          }

          /*
           * ADMIN BEZ ID
           */
          else {

            this.rentalsService.getMyRentals(
              this.currentPage(),
              15
            ).subscribe({

              next: response => {

                this.rentals.set(response.content);

                this.totalPages.set(response.totalPages);

              },

              error: err => {
                console.error(
                  "Błąd podczas pobierania wypożyczeń",
                  err
                );
              }

            });

          }

        }

        /*
         * ZWYKŁY UŻYTKOWNIK
         */
        else {

          const filter = this.rentalFilterForm.getRawValue();

          /*
           * Zwykły użytkownik nie powinien filtrować
           * po userId z URL.
           */
          filter.userId = null;

          this.rentalsService.getMyRentals(
            this.currentPage(),
            15,
            filter
          ).subscribe({

            next: response => {

              this.rentals.set(response.content);

              this.totalPages.set(response.totalPages);

            },

            error: err => {
              console.error(
                "Błąd podczas pobierania wypożyczeń",
                err
              );
            }

          });

        }

      },

      error: err => {
        console.error(
          "Błąd podczas pobierania profilu",
          err
        );
      }

    });

  }


  ngOnInit() {

    /*
     * Najpierw odczytujemy parametry z URL.
     *
     * Dopiero po ich ustawieniu odpalamy reloadRental().
     */
    this.route.queryParamMap.subscribe(params => {

      const watchParam = params.get('watch');

      this.isWatchId.set(watchParam === 'true');


      /*
       * WCZYTANIE FILTRÓW Z URL
       */
      this.rentalFilterForm.patchValue({

        rentalStatus:
          params.get('rentalStatus') as RentalStatus | null,

        paymentStatus:
          params.get('paymentStatus') as PaymentStatus | null,

        paymentMethod:
          params.get('paymentMethod') as PaymentMethod | null,

        userId:
          params.get('userId'),

        watchId:
          params.get('watchId'),

        branchId:
          params.get('branchId'),

        startDateFrom:
          params.get('startDateFrom') ?? '',

        startDateTo:
          params.get('startDateTo') ?? '',

        endDateFrom:
          params.get('endDateFrom') ?? '',

        endDateTo:
          params.get('endDateTo') ?? ''

      });


      /*
       * TERAZ dopiero pobieramy wypożyczenia.
       *
       * Dzięki temu isWatchId() ma już prawidłową wartość.
       */
      this.reloadRental();

    });


    /*
     * Lista zegarków do filtrów
     */
    this.watchesService.getWatches().subscribe(response => {

      this.watches.set(response.content);

    });


    /*
     * Lista oddziałów do filtrów
     */
    this.branchesService.getBranches().subscribe(response => {

      this.branches.set(response);

    });

  }


  async searchRentals() {

    const filter = this.rentalFilterForm.getRawValue();


    /*
     * ZAPIS FILTRÓW DO URL
     */
    const queryParams: any = {};


    Object.entries(filter).forEach(([key, value]) => {

      if (value !== null && value !== '') {

        queryParams[key] = value;

      }

    });


    /*
     * Zachowanie watch=true
     */
    if (this.isWatchId()) {

      queryParams['watch'] = 'true';

    }


    /*
     * Zmieniamy URL.
     *
     * UWAGA:
     * Po zmianie URL queryParamMap odpali się ponownie
     * i automatycznie wykona reloadRental().
     */
    this.router.navigate([], {

      relativeTo: this.route,

      queryParams: queryParams

    });

  }


  previousPage() {

    if (this.currentPage() > 0) {

      this.currentPage.update(
        page => page - 1
      );

      this.reloadRental();

    }

  }


  nextPage() {

    if (this.currentPage() < this.totalPages() - 1) {

      this.currentPage.update(
        page => page + 1
      );

      this.reloadRental();

    }

  }

}