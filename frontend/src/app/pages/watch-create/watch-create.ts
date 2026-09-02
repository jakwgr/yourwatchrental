import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { WatchesService } from '../../core/services/watches/watches-service';
import { BranchesService } from '../../core/services/branches/branches-service';

import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { WatchRequestDTO } from '../../core/models/watches/watch-request.dto';

import { WatchCondition, WatchConditionLabel } from '../../core/models/watches/enums/watch-condition';
import { WatchGender, WatchGenderLabel } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType, WatchMovementTypeLabel } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus, WatchStatusLabel } from '../../core/models/watches/enums/watch-status';
import { WatchType, WatchTypeLabel } from '../../core/models/watches/enums/watch-type';
import { ProfileService } from '../../core/services/profile/profile-service';
import { role } from '../../core/models/profile/enums/role';
import { Router } from '@angular/router';
import { SmallErrorView } from '../../shared/components/small-error-view/small-error-view';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';
import { FormError } from '../../shared/components/form-error/form-error';
import { onlyNumbers } from '../../shared/util/form-util';
import { pastDateValidator } from '../../shared/util/validators/validator-past';

@Component({
  selector: 'app-watch-create',
  imports: [ReactiveFormsModule, SmallErrorView, FormError],
  templateUrl: './watch-create.html',
  styleUrl: './watch-create.css',
})
export class WatchCreate {
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private watchesService = inject(WatchesService);
  private branchesService = inject(BranchesService);
  private profileService = inject(ProfileService);

  branchOptions = signal<BranchResponseDTO[]>([]);
  successModal = signal<boolean>(false);
  watchesError = signal<string | null>(null);
  watchCreated = signal<WatchFullInfoResponseDTO|null>(null);
  year = signal<number|null>(null);
  role = role;

  conditionOptions = Object.values(WatchCondition);
  conditionLabels = WatchConditionLabel;

  genderOptions = Object.values(WatchGender);
  genderLabels = WatchGenderLabel;

  movementTypeOptions = Object.values(WatchMovementType);
  movementTypeLabels = WatchMovementTypeLabel;
  
  statusOptions = Object.values(WatchStatus);
  statusLabels = WatchStatusLabel;

  watchTypeOptions = Object.values(WatchType);
  watchTypeLabels = WatchTypeLabel;

  watchForm = this.fb.group({
    manufacturer: ['', Validators.required],
    model: ['', Validators.required],
    referenceNumber: ['', Validators.required],
    serialNumber: ['', Validators.required],
    movement: ['', Validators.required],
    description: [''],

    yearOfProduction: [
      null as number | null,
      [Validators.required, Validators.min(0), Validators.max(new Date().getFullYear())]
    ],

    pricePerDay: [
      null as number | null,
      [Validators.required, Validators.min(0)]
    ],

    condition: [
      null as WatchCondition | null,
      Validators.required
    ],

    gender: [
      null as WatchGender | null,
      Validators.required
    ],

    movementType: [
      null as WatchMovementType | null,
      Validators.required
    ],

    status: [
      null as WatchStatus | null,
      Validators.required
    ],

    watchType: [
      null as WatchType | null,
      Validators.required
    ],

    branchId: [
      '',
      Validators.required
    ]
  });

  ngOnInit() {
    this.branchesService.getBranches().subscribe({
      next: branches => {
        this.branchOptions.set(branches);
      }
    });

    this.year.set(new Date().getFullYear());
    
    this.profileService.getMyProfile().subscribe(
      response => {
        if(response.role != role.ADMIN)
        {
          this.router.navigate(["/"]);
        }
      }
    )


  }

  createWatch() {

    if (this.watchForm.invalid) {
      this.watchForm.markAllAsTouched();
      return;
    }

    const value = this.watchForm.getRawValue();

    const request: WatchRequestDTO = {
      manufacturer: value.manufacturer!,
      model: value.model!,
      referenceNumber: value.referenceNumber!,
      serialNumber: value.serialNumber!,
      movement: value.movement!,
      description: value.description ?? '',

      yearOfProduction: value.yearOfProduction!,
      pricePerDay: value.pricePerDay!,

      condition: value.condition!,
      gender: value.gender!,
      movementType: value.movementType!,
      status: value.status!,
      watchType: value.watchType!,

      branchId: value.branchId!
    };

    this.watchesService.createWatch(request).subscribe({
      next: response => {
        this.watchForm.reset();
        this.successModal.set(true);
        this.watchCreated.set(response);
      },

      error: error => {
        this.watchesError.set(error.error.message);
      }
    });
  }

  onConfirmAddAnother(acceptaction: number)
  {
    if(acceptaction === 1)
    {
      window.location.reload();
    }
    else if(acceptaction === 2)
    {
      this.router.navigate(['/watches']);
    }
    else if(acceptaction === 3)
    {
      this.router.navigate(['/watches'], {
        state: {editWatchPhotos: true,
          watchId : this.watchCreated()?.id,
          watchSerial : this.watchCreated()?.serialNumber
        }
      })
    }
  }

  onClose()
  {
    this.router.navigate(['/watches']);
  }
}