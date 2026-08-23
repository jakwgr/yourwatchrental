import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { WatchesService } from '../../core/services/watches/watches-service';
import { BranchesService } from '../../core/services/branches/branches-service';

import { BranchResponseDTO } from '../../core/models/branch/branch-response.dto';
import { WatchRequestDTO } from '../../core/models/watches/watch-request.dto';

import { WatchCondition } from '../../core/models/watches/enums/watch-condition';
import { WatchGender } from '../../core/models/watches/enums/watch-gender';
import { WatchMovementType } from '../../core/models/watches/enums/watch-movement-type';
import { WatchStatus } from '../../core/models/watches/enums/watch-status';
import { WatchType } from '../../core/models/watches/enums/watch-type';
import { ProfileService } from '../../core/services/profile/profile-service';
import { role } from '../../core/models/profile/enums/role';
import { Router } from '@angular/router';

@Component({
  selector: 'app-watch-create',
  imports: [ReactiveFormsModule],
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

  role = role;

  conditionOptions = Object.values(WatchCondition);
  genderOptions = Object.values(WatchGender);
  movementTypeOptions = Object.values(WatchMovementType);
  statusOptions = Object.values(WatchStatus);
  watchTypeOptions = Object.values(WatchType);

  watchForm = this.fb.group({
    manufacturer: ['', Validators.required],
    model: ['', Validators.required],
    referenceNumber: ['', Validators.required],
    serialNumber: ['', Validators.required],
    movement: ['', Validators.required],
    description: [''],

    yearOfProduction: [
      null as number | null,
      [Validators.required, Validators.min(0)]
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
        console.log('Utworzono zegarek:', response);

        this.watchForm.reset();
      },

      error: error => {
        console.log('Błąd podczas tworzenia zegarka:', error);
      }
    });
  }
}