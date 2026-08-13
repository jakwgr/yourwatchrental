import { Component, inject, output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { UserPasswordUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-password-update-request.dto';
@Component({
  selector: 'app-profile-change-password-view',
  imports: [ReactiveFormsModule],
  templateUrl: './profile-change-password-view.html',
  styleUrl: './profile-change-password-view.css',
})
export class ProfileChangePasswordView {
  fb = inject(FormBuilder);

  close = output<void>();
  
  changePasswordForm = this.fb.nonNullable.group(
    {
      newPassword: ['', []],
      newPassword1: ['', []],
      password: ['', []]
    }
  )

  save = output<UserPasswordUpdateRequestDTO>();
  saveChangePasswordModal()
  {
    const request = this.changePasswordForm.getRawValue();
    this.save.emit(request);
  }
}
