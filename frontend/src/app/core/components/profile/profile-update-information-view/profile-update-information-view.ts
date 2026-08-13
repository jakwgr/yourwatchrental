import { Component, inject, input, output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch-delete/user-information-update-request.dto';
import { UserResponseDTO } from '../../../models/profile/user-response.dto';

@Component({
  selector: 'app-profile-update-information-view',
  imports: [ReactiveFormsModule],
  templateUrl: './profile-update-information-view.html',
  styleUrl: './profile-update-information-view.css',
})
export class ProfileUpdateInformationView {
  fb = inject(FormBuilder);
  profile = input<UserResponseDTO>();

  close = output<void>();

  updateInformationForm = this.fb.nonNullable.group
  (
    {
      firstName: ['', []],
      lastName: ['', []],
      dateOfBirth: ['', []],
      phoneNumber: ['', []]
    }
  )

  ngOnInit()
  {
    this.updateInformationForm.patchValue({
      firstName: this.profile()?.firstName,
      lastName: this.profile()?.lastName,
      phoneNumber: this.profile()?.phoneNumber,
      dateOfBirth: this.profile()?.dateOfBirth,
    });
  }

  save = output<UserInformationUpdateRequestDTO>();

  saveUpdateInformationdModal()
  {
    const request = this.updateInformationForm.getRawValue();
    this.save.emit(request);
  }
}
