import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchRequestDTO } from '../../core/models/branch/branch-request.dto';
import { ProfileService } from '../../core/services/profile/profile-service';
import { Profile } from '../profile/profile';
import { role } from '../../core/models/profile/enums/role';
import { Router } from '@angular/router';
import { SmallErrorView } from '../../shared/components/small-error-view/small-error-view';
import { single } from 'rxjs';

@Component({
  selector: 'app-branch-create',
  imports: [ReactiveFormsModule, SmallErrorView],
  templateUrl: './branch-create.html',
  styleUrl: './branch-create.css',
})
export class BranchCreate {

  role = role;

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private profileService = inject(ProfileService);

  successModal = signal<boolean>(false);
  branchesError = signal<string | null>(null);

  branchForm = this.fb.group({
    city: ['', Validators.required],
    name: ['', Validators.required],
    addres: ['', Validators.required],
    phoneNumber: ['', [
      Validators.required,
      Validators.pattern(/^[0-9]{9}$/)
    ]],
    email: ['', [
      Validators.required,
      Validators.email
    ]]
  });

  ngOnInit()
  {
    this.profileService.getMyProfile().subscribe(
          response => {
            if(response.role != role.ADMIN)
            {
              this.router.navigate(["/"]);
            }
          }
        )
  }

  createBranch() {

    if (this.branchForm.invalid) {
      this.branchForm.markAllAsTouched();
      return;
    }

    const value = this.branchForm.getRawValue();

    const request: BranchRequestDTO = {
      city: value.city!,
      name: value.name!,
      address: value.addres!,
      phoneNumber: value.phoneNumber!,
      email: value.email!
    };

    this.branchesService.createBranch(request).subscribe({
      next: response => {
        this.successModal.set(true);
      },
      error: err => {
        const error = err.error;

        this.branchesError.set(error.message);
      }
    });
  }

  onConfirmAddAnother(acceptaction: boolean)
  {
    if(acceptaction === true)
    {
      window.location.reload();
    }
    else
    {
      this.router.navigate(['/branches']);
    }
  }

  onClose()
  {
    this.router.navigate(['/branches']);
  }
}