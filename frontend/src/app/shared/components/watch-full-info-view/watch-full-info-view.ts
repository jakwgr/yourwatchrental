import { Component, effect, inject, input, output, Query, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchesService } from '../../../core/services/watches/watches-service';
import { ProfileService } from '../../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { WatchFullInfoResponseDTO } from '../../../core/models/watches/watch-full-info-response.dto';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { WatchCalendar } from '../watch-calendar/watch-calendar';
import { WatchCondition, WatchConditionLabel } from '../../../core/models/watches/enums/watch-condition';
import { WatchGender, WatchGenderLabel } from '../../../core/models/watches/enums/watch-gender';
import { WatchMovementType, WatchMovementTypeLabel } from '../../../core/models/watches/enums/watch-movement-type';
import { WatchType, WatchTypeLabel } from '../../../core/models/watches/enums/watch-type';
import { WatchStatus, WatchStatusLabel } from '../../../core/models/watches/enums/watch-status';
import { BranchesService } from '../../../core/services/branches/branches-service';
import { BranchResponseDTO } from '../../../core/models/branch/branch-response.dto';
import { WatchUpdateRequestDTO } from '../../../core/models/watches/watch-update-request.dto';
import { WatchStatusUpdateRequestDTO } from '../../../core/models/watches/watch-status-update-request.dto';
import { WatchBranchUpdateRequestDTO } from '../../../core/models/watches/watch-branch-update-request.dto';
import { WatchSerialNumberUpdateRequestDTO } from '../../../core/models/watches/watch-serial-number-update-request.dto';
import { WatchPhotosView } from '../watch-photos-view/watch-photos-view';
import { routes } from '../../../app.routes';
import { Router, RouterLink, RouterState } from '@angular/router';
import { inputIcon } from '@primeuix/themes/aura/datepicker';
import { FormError } from '../form-error/form-error';
import { SmallErrorView } from '../small-error-view/small-error-view';

@Component({
  selector: 'app-watch-full-info-view',
  imports: [FormsModule, WatchCalendar, ReactiveFormsModule, WatchPhotosView, RouterLink, FormError, SmallErrorView],
  templateUrl: './watch-full-info-view.html',
  styleUrl: './watch-full-info-view.css',
})
export class WatchFullInfoView {
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private router = inject(Router);


  watch = input.required<WatchCardResponseDTO>();
  editWatchPhotos = input.required<boolean>();

  branchOptions = signal<BranchResponseDTO[]>([]);
watchesError = signal<string | null>(null);

  private watchesService = inject(WatchesService);
  private profileService = inject(ProfileService);
  public isLogged: boolean = false;

  
  date: Date | null = null;

  profile = signal<UserResponseDTO | null>(null);
  watchFullInfo = signal<WatchFullInfoResponseDTO | null>(null);
selectedPhotoIndex = signal(0);

  close = output<null>();
  watchUpdated = output<void>();

  genderOptions = Object.values(WatchGender);
  genderLabels = WatchGenderLabel;

  movementTypeOptions = Object.values(WatchMovementType);
  movementTypeLabels = WatchMovementTypeLabel;
  
  statusOptions = Object.values(WatchStatus);
  statusLabels = WatchStatusLabel;

  watchTypeOptions = Object.values(WatchType);
  watchTypeLabels = WatchTypeLabel;

  conditionOptions = Object.values(WatchCondition);
  conditionOptionsLabels = WatchConditionLabel;

  watchStatus = WatchStatus;

  changeInfomationAdmin = signal(false);
  buttonVanish: boolean = false;
  saveType?: number | null = null;

  watchUpdate = this.fb.group({
  manufacturer: ['', [
    Validators.required
  ]],

  model: ['', [
    Validators.required
  ]],

  referenceNumber: ['', [
    Validators.required
  ]],

  movement: ['', [
    Validators.required
  ]],

  description: [''],

  yearOfProduction: [null as number | null, [
    Validators.required,
    Validators.min(0),
    Validators.max(new Date().getFullYear())
  ]],

  pricePerDay: [null as number | null, [
    Validators.required,
    Validators.min(0)
  ]],

  condition: [null as WatchCondition | null, [
    Validators.required
  ]],

  gender: [null as WatchGender | null, [
    Validators.required
  ]],

  movementType: [null as WatchMovementType | null, [
    Validators.required
  ]],

  watchType: [null as WatchType | null, [
    Validators.required
  ]],

  status: [null as WatchStatus | null, [
    Validators.required
  ]],

  branchId: ['', [
    Validators.required
  ]],

  serialNumber: ['', [
    Validators.required
  ]]
});
  
  constructor() {
    effect(() => {
      const watch = this.watchFullInfo();

      this.watchUpdate.patchValue({
        manufacturer: watch?.manufacturer,
        model: watch?.model,
        referenceNumber: watch?.referenceNumber,
        movement: watch?.movement,
        description: watch?.description,
        yearOfProduction: watch?.yearOfProduction,
        pricePerDay: watch?.pricePerDay,
        condition: watch?.condition,
        gender: watch?.gender,
        movementType: watch?.movementType,
        watchType: watch?.watchType,

        status: watch?.status,

        branchId: watch?.branch.id,

        serialNumber: watch?.serialNumber
      });
    })
  };

  ngOnInit() {

    document.body.style.overflow = 'hidden';
    this.profileService.getMyProfile().subscribe({
      next: response => {
        this.profile.set(response);
        this.isLogged = true;
      },
      error: erorr => {
        console.log("blad");
        this.isLogged = false;
      }
    });

    this.branchesService.getBranches().subscribe(branches => {
      this.branchOptions.set(branches);
    });

    this.watchesService.getWatch(this.watch().id).subscribe({
      next: response => {
        this.watchFullInfo.set(response)
      },
      error: error => {
        console.log("blad");
      }
    })

    if(this.editWatchPhotos() != undefined && this.editWatchPhotos() === true)
    {
        this.openPhotosModal();
    }
  }

  ngOnDestroy() {
  document.body.style.overflow = '';
}

  updateInformation() {
    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 1;
  }
  updateStatus() {
    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 2;
  }
  updateBranch() {
    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 3;
  }
  updateSerialNumber() {
    this.changeInfomationAdmin.set(true);
    this.buttonVanish = true;
    this.saveType = 4;
  }
  cancel() {
    this.changeInfomationAdmin.set(false);
    this.saveType = null;
    this.buttonVanish = false;
  this.watchesError.set(null);

  }

  watchReload(id:string)
  {
    this.watchesService.getWatch(id).subscribe({
    next: response => {
    this.watchFullInfo.set(response);
  }
});
  }

  save(id: string) {
  this.watchesError.set(null);

  const value = this.watchUpdate.getRawValue();

  if (this.saveType == null) {
    return;
  }

  else if (this.saveType == 1) {
    const request: WatchUpdateRequestDTO = {
      manufacturer: value.manufacturer!,
      model: value.model!,
      referenceNumber: value.referenceNumber!,
      movement: value.movement!,
      description: value.description!,
      yearOfProduction: value.yearOfProduction!,
      pricePerDay: value.pricePerDay!,
      condition: value.condition!,
      gender: value.gender!,
      movementType: value.movementType!,
      watchType: value.watchType!
    };

    this.watchesService.updateWatch(id, request).subscribe({
      next: response => {
        console.log(response);
        this.watchUpdated.emit();
        this.cancel();
        this.watchReload(id);
      },
      error: err => {
        const error = JSON.parse(err.error);
        this.watchesError.set(error.message);
      }
    });
  }

  else if (this.saveType == 2) {
    const request: WatchStatusUpdateRequestDTO = {
      status: value.status!
    };

    this.watchesService.updateWatchStatus(id, request).subscribe({
      next: response => {
        console.log(response);
        this.watchUpdated.emit();
        this.cancel();
        this.watchReload(id);
      },
      error: err => {
        const error = JSON.parse(err.error);
        this.watchesError.set(error.message);
      }
    });
  }

  else if (this.saveType == 3) {
    const request: WatchBranchUpdateRequestDTO = {
      branchId: value.branchId!
    };

    console.log(request);

    this.watchesService.updateWatchBranch(id, request).subscribe({
      next: response => {
        console.log(response);
        this.watchUpdated.emit();
        this.cancel();
        this.watchReload(id);
      },
      error: err => {
        const error = JSON.parse(err.error);
        this.watchesError.set(error.message);
      }
    });
  }

  else if (this.saveType == 4) {
    const request: WatchSerialNumberUpdateRequestDTO = {
      serialNumber: value.serialNumber!
    };

    this.watchesService.updateWatchSerialNumber(id, request).subscribe({
      next: response => {
        console.log(response);
        this.watchUpdated.emit();
        this.cancel();
        this.watchReload(id);
      },
      error: err => {
        this.watchesError.set(err.error.message);
      }
    });
  }
}

  closeModal() {
    this.close.emit(null);
  }

  showPhotosModal = signal(false);

  openPhotosModal() {
    this.showPhotosModal.set(true);
  }

  closePhotosModal() {
    this.showPhotosModal.set(false);
  }

  newRental()
  {
    this.router.navigate(['/rentals/create', this.watch().id]);
  }
}
