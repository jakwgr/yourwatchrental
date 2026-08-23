import { Component, effect, inject, input, output, signal } from '@angular/core';
import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { single } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, ɵInternalFormsSharedModule } from "@angular/forms";
import { ComponentInputBindingOptions, RouterLink } from '@angular/router';
import { userStatus } from '../../../models/profile/enums/user-status';
import { role } from '../../../models/profile/enums/role';
import { AdminService } from '../../../services/admin/admin-service';

import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch/user-information-update-request.dto';
import { email } from '@angular/forms/signals';
import { UserEmailUpdateAdminRequestDTO } from '../../../models/admin/users/user-email-update-admin-request.dto';
import { UserStatusChangeRequestDTO } from '../../../models/admin/users/user-status-change-admin-request.dto';
import { UserRoleChangeRequestDTO } from '../../../models/admin/users/user-role-change-request.dto';
import { UserPasswordUpdateAdminRequestDTO } from '../../../models/admin/users/user-password-update-admin-request.dto';

@Component({
  selector: 'app-user-view',
  imports: [ɵInternalFormsSharedModule,
    ReactiveFormsModule, RouterLink],
  templateUrl: './admin-users-view.html',
  styleUrl: './admin-users-view.css',
})
export class AdminUsersView {
  profile = input.required<UserResponseDTO>();
  fb = inject(FormBuilder);
  adminService = inject(AdminService);

  profileUpdated = output<void>();

  changeInfomationAdmin = signal(false);
  buttonVanish:boolean = false;
  saveType?: number|null = null;

  roleOptions = Object.values(role)
  statusOptions = Object.values(userStatus);

  adminUserUpdate = this.fb.nonNullable.group
  (
    {
      firstName: ['', []],
      lastName: ['', []],
      dateOfBirth: ['', []],
      phoneNumber: ['', []],
      email: ['', []],
      password: ['', []],
      role : ['', []],
      status :  ['', []],
      newPassword : ['', []],
      newPassword1 : ['', []]
    }
  )


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

  updateInformation()
  {
      this.changeInfomationAdmin.set(true);
      this.buttonVanish = true;
      this.saveType = 1;
  }

  updatePassword()
  {
      this.changeInfomationAdmin.set(true);
      this.buttonVanish = true;
      this.saveType = 5;
  }

  updateEmail()
  {
      this.changeInfomationAdmin.set(true);
      this.buttonVanish = true;
      this.saveType = 2;
  }

  updateStatus()
  {
      this.changeInfomationAdmin.set(true);
      this.buttonVanish = true;
      this.saveType = 3;
  }

  updateRole()
  {
      this.changeInfomationAdmin.set(true);
      this.buttonVanish = true;
      this.saveType = 4;
  }

  cancel()
  {
    this.changeInfomationAdmin.set(false);
    this.saveType = null;
    this.buttonVanish = false;
  }

  save(id:string)
  {
    const value = this.adminUserUpdate.getRawValue();
    if(this.saveType == null){}
    else if( this.saveType == 1)
    {

      const request: UserInformationUpdateRequestDTO = {
        firstName: value.firstName!,
        lastName: value.lastName!,
        phoneNumber: value.phoneNumber!,
        dateOfBirth: value.dateOfBirth!,
      }
      this.adminService.updateUserInformation(id, request).subscribe(
      response => {
        console.log(response);
        this.profileUpdated.emit();
       }
      )
    }
    else if( this.saveType == 2){
      const request: UserEmailUpdateAdminRequestDTO = {
        email: value.email!
      }
      this.adminService.updateUserEmail(id, request).subscribe(
      response => {
        console.log(response);
        this.profileUpdated.emit();
       }
      )
    }
    else if( this.saveType == 3){
      const request: UserStatusChangeRequestDTO = {
        status: value.status!
      }
      this.adminService.updateUserStatus(id, request).subscribe(
      response => {
        console.log(response);
        this.profileUpdated.emit();
       }
      )
    }
    else if( this.saveType == 4){
      const request: UserRoleChangeRequestDTO = {
        role: value.role!
      }
      this.adminService.updateUserRole(id, request).subscribe(
      response => {
        console.log(response);
        this.profileUpdated.emit();
       }
      )
    }
    else if( this.saveType == 5){
      const request: UserPasswordUpdateAdminRequestDTO = {
        newPassword: value.newPassword!,
        newPassword1: value.newPassword1!
      }
      this.adminService.updateUserPassword(id, request).subscribe(
      response => {
        console.log(response);
        this.profileUpdated.emit();
       }
      )
    }
    this.cancel();
  }
}
