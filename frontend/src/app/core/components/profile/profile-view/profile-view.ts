import { Component, EventEmitter, inject, input, Output, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FormBuilder, FormControl, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule } from '@angular/forms';

import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { UserEmailUpdateRequestDTO } from '../../../models/profile/put-patch/user-email-update-request.dto';
import { UserPasswordUpdateRequestDTO } from '../../../models/profile/put-patch/user-password-update-request.dto';
import { UserSoftDeleteRequestDTO } from '../../../models/profile/put-patch/user-soft-delete-request.dto';

import { ProfileSoftDeleteView } from '../profile-soft-delete-view/profile-soft-delete-view';
import { ProfileChangeEmailView } from '../profile-change-email-view/profile-change-email-view';
import { ProfileChangePasswordView } from '../profile-change-password-view/profile-change-password-view';
import { ProfileUpdateInformationView } from '../profile-update-information-view/profile-update-information-view';
import { ProfileService } from '../../../services/profile/profile-service';
import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch/user-information-update-request.dto';
import { UserStatusLabel } from '../../../models/profile/enums/user-status';
import { RoleLabel } from '../../../models/profile/enums/role';
import { FormError } from '../../../../shared/components/form-error/form-error';
import { SmallErrorView } from '../../../../shared/components/small-error-view/small-error-view';

@Component({
  selector: 'app-profile-view',
  imports: [
    ɵInternalFormsSharedModule,
    ReactiveFormsModule,
    ProfileChangeEmailView,
    ProfileChangePasswordView,
    ProfileUpdateInformationView,
    ProfileSoftDeleteView,
    RouterLink
  ],
  templateUrl: './profile-view.html',
  styleUrl: './profile-view.css',
})
export class ProfileView {

  profile = input.required<UserResponseDTO>();
  profileService = inject(ProfileService);

  roleLabel = RoleLabel;
  userStatusLabel = UserStatusLabel;

  @Output() changeEmail = new EventEmitter<UserEmailUpdateRequestDTO>();

  emailError = signal<string | null>(null);
  passwordError = signal<string | null>(null);
  informationError = signal<string | null>(null);
  softDeleteError = signal<string | null>(null);

  showChangeEmailModal = signal(false);

  openChangeEmailModal() {
    this.emailError.set('');
    this.showChangeEmailModal.set(true);
    document.body.style.overflow = 'hidden';
  }

  closeChangeEmailModal() {
    this.emailError.set('');
    this.showChangeEmailModal.set(false);
    document.body.style.overflow = '';
  }
  formatPhoneNumber(phone: string): string {
    return `+48 ${phone.slice(0, 3)}-${phone.slice(3, 6)}-${phone.slice(6, 9)}`;
  }
  saveEmail(request: UserEmailUpdateRequestDTO) {
    this.emailError.set('');

    this.profileService.updateMyEmail(request).subscribe({
      next: response => {
        this.emailError.set('');
        this.closeChangeEmailModal();
      },

      error: err => {
        if (err.status === 409 || err.status === 404) {
          this.emailError.set(err.error.message);
        }
      }
    });
  }

  showChangePasswordModal = signal(false);

  openChangePasswordModal() {
    this.passwordError.set('');
    document.body.style.overflow = 'hidden';
    this.showChangePasswordModal.set(true);
  }

  closeChangePasswordModal() {
    this.passwordError.set('');
    document.body.style.overflow = '';
    this.showChangePasswordModal.set(false);
  }

  savePassword(request: UserPasswordUpdateRequestDTO) {
    this.passwordError.set('');

    this.profileService.updateMyPassword(request).subscribe({
      next: response => {
        this.passwordError.set('');
        this.closeChangePasswordModal();
      },

      error: err => {
        if (err.status === 409 || err.status === 404) {
          this.passwordError.set(err.error.message);
        }
      }
    });
  }

  showUpdateInformationModal = signal(false);

  openUpdateInformationModal() {
    this.informationError.set('');
    document.body.style.overflow = 'hidden';
    this.showUpdateInformationModal.set(true);
  }

  closeUpdateInformationModal() {
    this.informationError.set('');
    document.body.style.overflow = '';
    this.showUpdateInformationModal.set(false);
  }

  updateInformation(request: UserInformationUpdateRequestDTO) {
    this.informationError.set('');

    this.profileService.updateMyInformation(request).subscribe({
      next: response => {
        this.informationError.set('');
        this.closeUpdateInformationModal();
      },

      error: err => {
        if (err.status === 409) {
          this.informationError.set(err.error.message);
        }
      }
    });
  }

  showSoftDeleteAccountModal = signal(false);

  openSoftDeleteInformationModal() {
    document.body.style.overflow = 'hidden';
    this.softDeleteError.set('');
    this.showSoftDeleteAccountModal.set(true);
  }

  closeSoftDeleteInformationModal() {
    document.body.style.overflow = '';
    this.softDeleteError.set('');
    this.showSoftDeleteAccountModal.set(false);
  }

  softDelete(request: UserSoftDeleteRequestDTO) {
    this.softDeleteError.set('');

    this.profileService.softDeleteMyAccount(request).subscribe({
      next: response => {
        this.softDeleteError.set('');
      },

      error: err => {
        if (err.status === 409 || err.status === 404) {
          this.softDeleteError.set(err.error.message);
        }
      }
    });
  }
}