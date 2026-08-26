import { Component, inject, input, output, signal } from '@angular/core';
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
import { ActivatedRoute } from '@angular/router';
import { RentalFilterRequestDTO } from '../../core/models/rentals/rental-filter-request.dto';
import { AdminService } from '../../core/services/admin/admin-service';
import { UserFilterCriteriaRequestDTO } from '../../core/models/admin/users/user-filter-criteria-request.dto';
import { single } from 'rxjs';
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

  reloadRental() {
    this.profileService.getMyProfile().subscribe(
      response => {
        this.profile.set(response);

        if (this.profile()?.role == role.ADMIN) {

          this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            this.paramId.set(id);
          });

          if (!this.isWatchId()) {
            if (this.paramId() != null) {
              const filter: RentalFilterRequestDTO = {
                userId: this.paramId()
              }
              this.adminService.getUserAdmin(this.paramId()!).subscribe(
                response => {
                  this.user.set(response);
                }

              );
              this.rentalsService.getRentalsAdmin(this.currentPage(), 15, filter).subscribe(
                response => {
                  this.rentals.set(response.content)
                }
              )
            }
            else {
              this.rentalsService.getMyRentals().subscribe(
                response => {
                  this.rentals.set(response.content)
                }
              )
            }
          } else {
            if (this.paramId() != null) {
              const filter: RentalFilterRequestDTO = {
                watchId: this.paramId()
              }
              this.watchesService.getWatch(this.paramId()!).subscribe(
                response => {
                  this.watch.set(response);
                }

              );
              this.rentalsService.getRentalsAdmin(this.currentPage(), 15, filter).subscribe(
                response => {
                  this.rentals.set(response.content)
                }
              )
            }
          }
        }
      }
    )

  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(
      params => {
        const watchParam = params.get('watch');

        if (watchParam === 'true') {
          this.isWatchId.set(true);
        }
      }
    )

    console.log(this.isWatchId());
    console.log(this.paramId());
    this.reloadRental();

    this.watchesService.getWatches().subscribe(response => {
      this.watches.set(response.content);
    });

    this.branchesService.getBranches().subscribe(response => {
      this.branches.set(response);
    });
  }


  rentalStatusOptions = Object.values(RentalStatus);
  paymentStatusOptions = Object.values(PaymentStatus);
  paymentMethodOptions = Object.values(PaymentMethod);

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

  async searchRentals() {
    const filter = this.rentalFilterForm.getRawValue();

    if (this.profile()?.role != role.ADMIN || this.paramId() == null) {
      filter.userId = null;
      console.log("nie admin");
      this.rentalsService.getMyRentals(this.currentPage(), 15, filter).subscribe(
        response => {
          this.rentals.set(response.content)
        }
      )
    }
    else {

      this.route.paramMap.subscribe(params => {
        const id = params.get('id');
        this.paramId.set(id);
      });

      const filter = this.rentalFilterForm.getRawValue();

      if (this.isWatchId()) {
        filter.watchId = this.paramId();
      }
      else {
        filter.userId = this.paramId();
      }

      this.rentalsService.getRentalsAdmin(this.currentPage(), 15, filter).subscribe(
        response => {
          this.rentals.set(response.content)

          console.log(this.rentals);
        }
      )
    }

  }


  previousPage() {
    if (this.currentPage() > 0) {
      this.currentPage.update(page => page - 1);
      this.searchRentals();
    }
  }

  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.currentPage.update(page => page + 1);
      this.searchRentals();
    }
  }

}
