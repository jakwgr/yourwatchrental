import { Component, inject, input, output, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { UserSoftDeleteRequestDTO } from '../../../models/profile/put-patch/user-soft-delete-request.dto';
import { UserResponseDTO } from '../../../models/profile/user-response.dto';
import { single } from 'rxjs';

@Component({
  selector: 'app-profile-soft-delete-view',
  imports: [ReactiveFormsModule],
  templateUrl: './profile-soft-delete-view.html',
  styleUrl: './profile-soft-delete-view.css',
})
export class ProfileSoftDeleteView {
    fb = inject(FormBuilder);
  
    close = output<void>();
    
    softDeleteForm = this.fb.nonNullable.group(
      {
        password: ['', []]
      }
    )
  
    save = output<UserSoftDeleteRequestDTO>();
    saveSoftDeleteModal()
    {
      const request = this.softDeleteForm.getRawValue();
      this.save.emit(request);
    }

    formOpen = signal(false);
    openForm()
    {
      this.formOpen.set(true);
    }
}
