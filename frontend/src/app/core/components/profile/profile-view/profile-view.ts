import { Component, EventEmitter, inject, input, Output, output, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FormBuilder, FormControl, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule } from '@angular/forms';

import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { UserEmailUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-email-update-request.dto';
import { UserPasswordUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-password-update-request.dto';
import { UserSoftDeleteRequestDTO } from '../../../models/profile/put-patch-delete/user-soft-delete-request.dto';

import { ProfileSoftDeleteView } from '../profile-soft-delete-view/profile-soft-delete-view';
import { ProfileChangeEmailView } from '../profile-change-email-view/profile-change-email-view';
import { ProfileChangePasswordView } from '../profile-change-password-view/profile-change-password-view';
import { ProfileUpdateInformationView } from '../profile-update-information-view/profile-update-information-view';
import { ProfileService } from '../../../services/profile/profile-service';
import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-information-update-request.dto';

@Component({
  selector: 'app-profile-view',
  imports: [
    ɵInternalFormsSharedModule,
    ReactiveFormsModule,
    ProfileChangeEmailView,
    ProfileChangePasswordView,
    ProfileUpdateInformationView,
    ProfileSoftDeleteView
  ],
  templateUrl: './profile-view.html',
  styleUrl: './profile-view.css',
})
export class ProfileView {
  profile = input.required<UserResponseDTO>();
  profileService = inject(ProfileService);
    
    @Output() changeEmail = new EventEmitter<UserEmailUpdateRequestDTO>

  //email  -------------------------------------
    showChangeEmailModal = signal(false);
  closeChangeEmailModal()
  {
    this.showChangeEmailModal.set(false);
  }
  openChangeEmailModal()
  {
    this.showChangeEmailModal.set(true);
  }

  saveEmail(request:UserEmailUpdateRequestDTO)
  {
      console.log(request);
      this.profileService.updateMyEmail(request).subscribe(
      response => {
        console.log(response)
      }
    )
  }

  //haslo ----------------------------------------
  showChangePasswordModal = signal(false);
  openChangePasswordModal()
  {
    this.showChangePasswordModal.set(true);
  }
  closeChangePasswordModal()
  {
    this.showChangePasswordModal.set(false);
  }

  savePassword(request:UserPasswordUpdateRequestDTO)
  {
    console.log(request);
    this.profileService.updateMyPassword(request).subscribe
    (
      response => {
        console.log(response);
      }
    )
  }

  //informacje ogolnie ------------------------------
  showUpdateInformationModal = signal(false)
  openUpdateInformationModal()
  {
    this.showUpdateInformationModal.set(true);
  }
  closeUpdateInformationModal()
  {
    this.showUpdateInformationModal.set(false);
  }
  updateInformation(request: UserInformationUpdateRequestDTO)
  {
    console.log(request);
    this.profileService.updateMyInformation(request).subscribe(
      response => {
        console.log(response);
      }
    )
  }

  //usuwanie konta softdelete -------------------------
  showSoftDeleteAccountModal = signal(false)
  openSoftDeleteInformationModal()
  {
    this.showSoftDeleteAccountModal.set(true);
  }
  closeSoftDeleteInformationModal()
  {
    this.showSoftDeleteAccountModal.set(false);
  }
  softDelete(request: UserSoftDeleteRequestDTO)
  {
    console.log(request);
    this.profileService.softDeleteMyAccount(request).subscribe(
      response => {
        console.log(response);
      }
    )
  }
}
