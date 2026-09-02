import { Component, inject, input, output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserInformationUpdateRequestDTO } from '../../../models/profile/put-patch/user-information-update-request.dto';
import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { pastDateValidator } from '../../../../shared/util/validators/validator-past';
import { FormError } from '../../../../shared/components/form-error/form-error';
import { SmallErrorView } from '../../../../shared/components/small-error-view/small-error-view';
import { onlyNumbers } from '../../../../shared/util/form-util';

@Component({
  selector: 'app-profile-update-information-view',
  imports: [ReactiveFormsModule, SmallErrorView, FormError],
  templateUrl: './profile-update-information-view.html',
  styleUrl: './profile-update-information-view.css',
})
export class ProfileUpdateInformationView {
  fb = inject(FormBuilder);
  profile = input<UserResponseDTO>();

  close = output<void>();

  error = input<string | null>(null);

  updateInformationForm = this.fb.nonNullable.group
    (
      {
        firstName: ['', [Validators.required]],
        lastName: ['', [Validators.required]],
        dateOfBirth: ['', [Validators.required, pastDateValidator]],
        phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]]
      }
    )
  onlyNumbers(event: Event) {
    onlyNumbers(event);
  }
  ngOnInit() {
    this.updateInformationForm.patchValue({
      firstName: this.profile()?.firstName,
      lastName: this.profile()?.lastName,
      phoneNumber: this.profile()?.phoneNumber,
      dateOfBirth: this.profile()?.dateOfBirth,
    });
  }

  save = output<UserInformationUpdateRequestDTO>();

  saveUpdateInformationdModal() {
    const request = this.updateInformationForm.getRawValue();
    this.save.emit(request);
  }
}
