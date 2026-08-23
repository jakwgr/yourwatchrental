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

  watches = signal<WatchCardResponseDTO[]>([]);
  branches = signal<BranchResponseDTO[]>([]);

  private fb = inject(FormBuilder);

  profile = signal<UserResponseDTO | null>(null);
  rentals = signal<RentalResponseDTO[]>([]);

  currentPage = signal(0);
  totalPages = signal(0);

  admin = input(false);
  userId = signal<string | null>(null);
  role = role;
  adminId = input<string|null>(null);

  reloadRental() {
    this.profileService.getMyProfile().subscribe(
      response => {
        this.profile.set(response);

        if (this.profile()?.role == role.ADMIN) {
          this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            this.userId.set(id);
          });
          if (this.userId() != null) {
            const filter: RentalFilterRequestDTO = {
              userId: this.userId()
            }
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
        }
        else {
          this.rentalsService.getMyRentals().subscribe(
            response => {
              this.rentals.set(response.content)
            }
          )
        }

      }
    )

  }

  ngOnInit() {
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

    if (this.profile()?.role != role.ADMIN || this.userId == null) {
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
        this.userId.set(id);
      });

      const filter = this.rentalFilterForm.getRawValue();

      filter.userId = this.userId();

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
