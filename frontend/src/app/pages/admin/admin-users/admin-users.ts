import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { UserFilterCriteriaRequestDTO } from '../../../core/models/admin/users/user-filter-criteria-request.dto';
import { AdminService } from '../../../core/services/admin/admin-service';
import { ProfileView } from '../../../core/components/profile/profile-view/profile-view';
import { AdminUsersView } from '../../../core/components/admin/admin-users-view/admin-users-view';
import { ProfileService } from '../../../core/services/profile/profile-service';


@Component({
  selector: 'app-admin-users',
  imports: [ReactiveFormsModule,
    AdminUsersView
   ],
  templateUrl: './admin-users.html',
  styleUrl: './admin-users.css',
})
export class AdminUsers {
  private adminService = inject(AdminService);
  private fb = inject(FormBuilder);
  private profileService = inject(ProfileService)
  filterForm = this.fb.group({
    firstName: [''],
    lastName: [''],
    email: [''],
    phoneNumber: ['']
  })

  users = signal<UserResponseDTO[]>([]);
profile = signal<UserResponseDTO | null>(null);

  ngOnInit() {
      this.adminService.getUsersAdmin().subscribe(response => {

        this.users.set(response.content);

      });

      this.profileService.getMyProfile().subscribe(
      response => {
        this.profile.set(response);
      }
    );
  }

loadProfile() {
    this.adminService.getUsersAdmin().subscribe(response => {
        this.users.set(response.content);
    });
}

  search() {
      const filter = this.filterForm.getRawValue();

      this.adminService.getUsersAdmin(0, 10, filter).subscribe({
          next: response => {
              this.users.set(response.content);
          },
          error: error => {
              console.error('BŁĄD:', error);
          }
      });
    }
}
