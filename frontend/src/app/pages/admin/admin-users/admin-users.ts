import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { UserFilterCriteriaRequestDTO } from '../../../core/models/admin/users/user-filter-criteria-request.dto';

import { AdminService } from '../../../core/services/admin/admin-service';
import { ProfileService } from '../../../core/services/profile/profile-service';

import { AdminUsersView } from '../../../core/components/admin/admin-users-view/admin-users-view';
import { PaginationButtons } from '../../../shared/components/pagination-buttons/pagination-buttons';


@Component({
  selector: 'app-admin-users',
  imports: [
    ReactiveFormsModule,
    AdminUsersView,
    PaginationButtons
  ],
  templateUrl: './admin-users.html',
  styleUrl: './admin-users.css',
})
export class AdminUsers {

  private adminService = inject(AdminService);
  private fb = inject(FormBuilder);
  private profileService = inject(ProfileService);

  private router = inject(Router);
  private route = inject(ActivatedRoute);


  filterForm = this.fb.group({
    firstName: [''],
    lastName: [''],
    email: [''],
    phoneNumber: ['']
  });


  users = signal<UserResponseDTO[]>([]);

  profile = signal<UserResponseDTO | null>(null);

  currentPage = signal(0);

  totalPages = signal(0);


  ngOnInit() {

    this.route.queryParams.subscribe(params => {

      const page = Number(params['page'] ?? 0);

      this.currentPage.set(page);

      this.filterForm.patchValue({
        firstName: params['firstName'] ?? '',
        lastName: params['lastName'] ?? '',
        email: params['email'] ?? '',
        phoneNumber: params['phoneNumber'] ?? ''
      }, {
        emitEvent: false
      });

      this.loadUsers();

    });

    this.profileService.getMyProfile().subscribe(
      response => {
        this.profile.set(response);
      }
    );

  }

  loadUsers() {

    const value = this.filterForm.getRawValue();

    const filter: UserFilterCriteriaRequestDTO = {
      firstName: value.firstName || null,
      lastName: value.lastName || null,
      email: value.email || null,
      phoneNumber: value.phoneNumber || null
    };


    this.adminService
      .getUsersAdmin(
        this.currentPage(),
        10,
        filter
      )
      .subscribe({

        next: response => {

          this.users.set(response.content);

          this.totalPages.set(response.totalPages);

        },

        error: error => {

          console.error('BŁĄD:', error);

        }

      });

  }

  search() {

    const value = this.filterForm.getRawValue();

    this.router.navigate([], {

      relativeTo: this.route,

      queryParams: {
        firstName: value.firstName || null,
        lastName: value.lastName || null,
        email: value.email || null,
        phoneNumber: value.phoneNumber || null,
        page: 0
      }

    });

  }

  loadProfile() {

    this.loadUsers();

  }

  previousPage() {

    if (this.currentPage() <= 0) {
      return;
    }

    const newPage = this.currentPage() - 1;

    this.currentPage.set(newPage);

    this.updatePageInUrl(newPage);

  }

  nextPage() {

    if (this.currentPage() >= this.totalPages() - 1) {
      return;
    }

    const newPage = this.currentPage() + 1;

    this.currentPage.set(newPage);

    this.updatePageInUrl(newPage);

  }

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