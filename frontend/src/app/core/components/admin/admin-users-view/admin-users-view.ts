import { Component, effect, inject, input, output, signal } from '@angular/core';
import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { FormBuilder, ReactiveFormsModule, Validators, ɵInternalFormsSharedModule } from "@angular/forms";
import { ComponentInputBindingOptions, RouterLink } from '@angular/router';
import { userStatus, UserStatusLabel } from '../../../models/profile/enums/user-status';
import { role, RoleLabel } from '../../../models/profile/enums/role';
import { AdminService } from '../../../services/admin/admin-service';

import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch/user-information-update-request.dto';
import { UserEmailUpdateAdminRequestDTO } from '../../../models/admin/users/user-email-update-admin-request.dto';
import { UserStatusChangeRequestDTO } from '../../../models/admin/users/user-status-change-admin-request.dto';
import { UserRoleChangeRequestDTO } from '../../../models/admin/users/user-role-change-request.dto';
import { UserPasswordUpdateAdminRequestDTO } from '../../../models/admin/users/user-password-update-admin-request.dto';
import { SmallErrorView } from '../../../../shared/components/small-error-view/small-error-view';
import { FormError } from '../../../../shared/components/form-error/form-error';
import { pastDateValidator } from '../../../../shared/util/validators/validator-past';
import { onlyNumbers } from '../../../../shared/util/form-util';



@Component({
  selector: 'app-user-view',
  imports: [
    ɵInternalFormsSharedModule,
    ReactiveFormsModule,
    RouterLink,
    FormError,
    SmallErrorView
  ],
  templateUrl: './admin-users-view.html',
  styleUrl: './admin-users-view.css',
})
export class AdminUsersView {

  profile = input.required<UserResponseDTO>();
  roleLabel = RoleLabel;
  userStatusLabel = UserStatusLabel;
  fb = inject(FormBuilder);
  adminService = inject(AdminService);

  profileUpdated = output<void>();

  changeInfomationAdmin = signal(false);
  buttonVanish: boolean = false;
  saveType?: number | null = null;


  // ERRORY -------------------------------------

  informationError = signal<string | null>(null);
  emailError = signal<string | null>(null);
  statusError = signal<string | null>(null);
  roleError = signal<string | null>(null);
  passwordError = signal<string | null>(null);


  // OPCJE -------------------------------------

  roleOptions = Object.values(role);
  statusOptions = Object.values(userStatus);


  // FORMULARZ -------------------------------------

  adminUserUpdate = this.fb.nonNullable.group({
firstName: ['', Validators.required],
lastName: ['', Validators.required],
dateOfBirth: ['', [Validators.required, pastDateValidator]],
phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
email: ['', [Validators.required, Validators.email]],
password: ['', [Validators.required, Validators.minLength(5)]],
role: ['', Validators.required],
status: ['', Validators.required],
newPassword: ['', [Validators.required, Validators.minLength(5)]],
newPassword1: ['', [Validators.required, Validators.minLength(5)]]
  });


  // KONSTRUKTOR -------------------------------------

  onlyNumbers(event: Event)
  {
    onlyNumbers(event);
  }

  formatPhoneNumber(phone: string): string {
  return `+48 ${phone.slice(0, 3)}-${phone.slice(3, 6)}-${phone.slice(6, 9)}`;
}

  constructor() {
    effect(() => {
      const profile = this.profile();

      this.adminUserUpdate.patchValue({
        firstName: profile.firstName,
        lastName: profile.lastName,
        phoneNumber: profile.phoneNumber,
        dateOfBirth: profile.dateOfBirth,
        email: profile.email,
        status: profile.status,
        role: profile.role
      });
    });
  }


  // OTWIERANIE EDYCJI -------------------------------------

  updateInformation()
  {
    this.informationError.set(null);

    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 1;
  }


  updatePassword()
  {
    this.passwordError.set(null);

    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 5;
  }


  updateEmail()
  {
    this.emailError.set(null);

    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 2;
  }


  updateStatus()
  {
    this.statusError.set(null);

    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 3;
  }


  updateRole()
  {
    this.roleError.set(null);

    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 4;
  }


  // ANULOWANIE -------------------------------------

cancel()
{
  const profile = this.profile();

  this.adminUserUpdate.patchValue({
    firstName: profile.firstName,
    lastName: profile.lastName,
    phoneNumber: profile.phoneNumber,
    dateOfBirth: profile.dateOfBirth,
    email: profile.email,
    status: profile.status,
    role: profile.role,
    newPassword: '',
    newPassword1: ''
  });

  this.changeInfomationAdmin.set(false);
  this.saveType = null;
  this.buttonVanish = false;

  this.informationError.set(null);
  this.emailError.set(null);
  this.statusError.set(null);
  this.roleError.set(null);
  this.passwordError.set(null);
}

  // ZAPIS -------------------------------------

  save(id: string)
  {
    const value = this.adminUserUpdate.getRawValue();

    if (this.saveType == null) {
      return;
    }


    // INFORMACJE -------------------------------------

    else if (this.saveType == 1)
    {
      this.informationError.set(null);

      const request: UserInformationUpdateRequestDTO = {
        firstName: value.firstName!,
        lastName: value.lastName!,
        phoneNumber: value.phoneNumber!,
        dateOfBirth: value.dateOfBirth!,
      };

      this.adminService.updateUserInformation(id, request).subscribe({
        next: response => {
          console.log('sukces', response);

          this.profileUpdated.emit();
          this.cancel();
        },

        error: err => {
          if (err.status === 409 || err.status === 404) {
            this.informationError.set(err.error.message);
          }
        }
      });
    }


    // EMAIL -------------------------------------

    else if (this.saveType == 2)
    {
      this.emailError.set(null);

      const request: UserEmailUpdateAdminRequestDTO = {
        email: value.email!
      };

      this.adminService.updateUserEmail(id, request).subscribe({
        next: response => {
          console.log('sukces', response);

          this.profileUpdated.emit();
          this.cancel();
        },

        error: err => {
          if (err.status === 409 || err.status === 404) {
            this.emailError.set(err.error.message);
          }
        }
      });
    }


    // STATUS -------------------------------------

    else if (this.saveType == 3)
    {
      this.statusError.set(null);

      const request: UserStatusChangeRequestDTO = {
        status: value.status!
      };

      this.adminService.updateUserStatus(id, request).subscribe({
        next: response => {
          console.log('sukces', response);

          this.profileUpdated.emit();
          this.cancel();
        },

        error: err => {
          if (err.status === 409 || err.status === 404) {
            this.statusError.set(err.error.message);
          }
        }
      });
    }


    // ROLA -------------------------------------

    else if (this.saveType == 4)
    {
      this.roleError.set(null);

      const request: UserRoleChangeRequestDTO = {
        role: value.role!
      };

      this.adminService.updateUserRole(id, request).subscribe({
        next: response => {
          console.log('sukces', response);

          this.profileUpdated.emit();
          this.cancel();
        },

        error: err => {
          if (err.status === 409 || err.status === 404) {
            this.roleError.set(err.error.message);
          }
        }
      });
    }


    // HASŁO -------------------------------------

    else if (this.saveType == 5)
    {
      this.passwordError.set(null);

      const request: UserPasswordUpdateAdminRequestDTO = {
        newPassword: value.newPassword!,
        newPassword1: value.newPassword1!
      };

      this.adminService.updateUserPassword(id, request).subscribe({
        next: response => {
          console.log('sukces', response);

          this.profileUpdated.emit();
          this.cancel();
        },

        error: err => {
          if (err.status === 409 || err.status === 404) {
            this.passwordError.set(err.error.message);
          }
        }
      });
    }
  }
}