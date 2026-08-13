import { Component, EventEmitter, inject, input, output, signal } from '@angular/core';
import { FormBuilder, FormControl, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule  } from '@angular/forms';

import { UserEmailUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-email-update-request.dto';

@Component({
  selector: 'app-profile-change-email-view',
  imports: [ReactiveFormsModule],
  templateUrl: 'profile-change-email-view.html',
  styleUrl: 'profile-change-email-view.css',
})
export class ProfileChangeEmailView {
  
  fb = inject(FormBuilder);

  close = output<void>();
  

  changeEmailForm = this.fb.nonNullable.group(
    {
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)], ]
    }
  )

  save = output<UserEmailUpdateRequestDTO>();
  saveChangeEmailModal()
  {
    const request = this.changeEmailForm.getRawValue();
    this.save.emit(request);
  }
}
