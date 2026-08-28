import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchStatus } from '../../core/models/branch/enums/branch-status';
import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { BranchesInfoView } from '../../shared/components/branches-info-view/branches-info-view';
import { ProfileService } from '../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../core/models/profile/user-response.dto';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { onlyNumbers } from '../../shared/util/form-util';

@Component({
  selector: 'app-branches',
  imports: [ReactiveFormsModule
    , BranchesInfoView, RouterLink],
  templateUrl: './branches.html',
  styleUrl: './branches.css',
})
export class Branches {
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private profileService = inject(ProfileService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  branchOptions = Object.values(BranchStatus);

  branches = signal<BranchResponseDTO[]>([]);

  profile = signal<UserResponseDTO | null>(null);

  criteriaForm = this.fb.group({
    city : [''],
    name : [''],
    phoneNumber : [''],
    address : [''],
    email : [''],
    status : [BranchStatus.ACTIVE as BranchStatus | null]
  })

    onlyNumbers(event: Event)
  {
    onlyNumbers(event);
  }
  
  save()
  {
    const filter = this.criteriaForm.getRawValue();


    const queryParams: any = {};

    Object.entries(filter).forEach(([key, value]) => {

      if (value !== null && value !== '') {
        queryParams[key] = value;
      }

    });

    this.router.navigate([], {
    relativeTo: this.route,
    queryParams: queryParams
  });

    this.branchesService.getBranches(this.criteriaForm.getRawValue()).subscribe(
        response => {
          this.branches.set(response);
          console.log(response)
        }
    )
  }
  
    ngOnInit()
    {this.route.queryParams.subscribe(params => {

      this.criteriaForm.patchValue({

        city: params['city'] ?? '',
        name: params['model'] ?? '',
        phoneNumber: params['phoneNumber'] ?? null,
        address: params['address'] ?? null,
        email: params['email'] ?? null,
        // status: params['status'] ?? null

      });

      this.profileService.getMyProfile().subscribe(
        response => {
          this.profile.set(response);
        }
      )

    this.branchesService.getBranches(this.criteriaForm.getRawValue()).subscribe(
        response => {
          this.branches.set(response);
          console.log(response)
        }
    )
  })
}

  resetFilters()
  {
    this.criteriaForm.reset({
    city : '',
    name : '',
    phoneNumber : '',
    address : '',
    email : '',
    status : null
    })
  }

  reloadBranches()
  {
    this.save();
  }
}
