import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { BranchesService } from '../../core/services/branches/branches-service';
import { BranchStatus } from '../../core/models/branch/enums/branch-status';
import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { BranchesInfoView } from '../../shared/components/branches-info-view/branches-info-view';
import { ProfileService } from '../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../core/models/profile/user-response.dto';
@Component({
  selector: 'app-branches',
  imports: [ReactiveFormsModule
    , BranchesInfoView],
  templateUrl: './branches.html',
  styleUrl: './branches.css',
})
export class Branches {
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private profileService = inject(ProfileService);

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

  save()
  {
    this.branchesService.getBranches(this.criteriaForm.getRawValue()).subscribe(
        response => {
          this.branches.set(response);
          console.log(response)
        }
    )
  }
  
    ngOnInit()
    {
      this.profileService.getMyProfile().subscribe(
        response => {
          this.profile.set(response);
        }
      )

      this.criteriaForm.patchValue({
        status: BranchStatus.ACTIVE 
      });


    this.branchesService.getBranches(this.criteriaForm.getRawValue()).subscribe(
        response => {
          this.branches.set(response);
          console.log(response)
        }
    )
  }

  reloadBranches()
  {
    this.save();
  }
}
