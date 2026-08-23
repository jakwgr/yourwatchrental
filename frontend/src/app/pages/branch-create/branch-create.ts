import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchRequestDTO } from '../../core/models/branch/branch-request.dto';
import { ProfileService } from '../../core/services/profile/profile-service';
import { Profile } from '../profile/profile';
import { role } from '../../core/models/profile/enums/role';
import { Router } from '@angular/router';

@Component({
  selector: 'app-branch-create',
  imports: [ReactiveFormsModule],
  templateUrl: './branch-create.html',
  styleUrl: './branch-create.css',
})
export class BranchCreate {

  role = role;

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private profileService = inject(ProfileService);
  branchForm = this.fb.group({
    city: ['', Validators.required],
    name: ['', Validators.required],
    address: ['', Validators.required],
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
      addres: value.address!,
      phoneNumber: value.phoneNumber!,
      email: value.email!
    };

    this.branchesService.createBranch(request).subscribe({
      next: response => {
        console.log(response);
      },
      error: error => {
        console.log(error);
      }
    });
  }
}