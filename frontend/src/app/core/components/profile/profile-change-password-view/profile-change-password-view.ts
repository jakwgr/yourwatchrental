import { Component, inject, input, output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { UserPasswordUpdateRequestDTO } from '../../../models/profile/put-patch/user-password-update-request.dto';
import { FormError } from '../../../../shared/components/form-error/form-error';
import { SmallErrorView } from '../../../../shared/components/small-error-view/small-error-view';


@Component({
  selector: 'app-profile-change-password-view',
  imports: [ReactiveFormsModule, FormError, SmallErrorView],
  templateUrl: './profile-change-password-view.html',
  styleUrl: './profile-change-password-view.css',
})
export class ProfileChangePasswordView {
  fb = inject(FormBuilder);
  error = input<string|null>(null);

  close = output<void>();
  
  changePasswordForm = this.fb.nonNullable.group(
    {
      newPassword: ['', [Validators.required, Validators.minLength(5)]],
      newPassword1: ['', [Validators.required, Validators.minLength(5)]],
      password: ['', [Validators.required, Validators.minLength(5)]]
    }
  )

  save = output<UserPasswordUpdateRequestDTO>();
  saveChangePasswordModal()
  {
    const request = this.changePasswordForm.getRawValue();
    this.save.emit(request);
  }
}
