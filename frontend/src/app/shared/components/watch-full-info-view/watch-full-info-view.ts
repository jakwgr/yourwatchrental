import { Component, effect, inject, input, output, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchesService } from '../../../core/services/watches/watches-service';
import { ProfileService } from '../../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { WatchFullInfoResponseDTO } from '../../../core/models/watches/watch-full-info-response.dto';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WatchCalendar } from '../watch-calendar/watch-calendar';
import { WatchCondition } from '../../../core/models/watches/enums/watch-condition';
import { WatchGender } from '../../../core/models/watches/enums/watch-gender';
import { WatchMovementType } from '../../../core/models/watches/enums/watch-movement-type';
import { WatchType } from '../../../core/models/watches/enums/watch-type';
import { WatchStatus } from '../../../core/models/watches/enums/watch-status';
import { BranchesService } from '../../../core/services/branches/branches-service';
import { BranchResponseDTO } from '../../../core/models/branch/branch-response.dto';
import { WatchUpdateRequestDTO } from '../../../core/models/watches/watch-update-request.dto';
import { WatchStatusUpdateRequestDTO } from '../../../core/models/watches/watch-status-update-request.dto';
import { WatchBranchUpdateRequestDTO } from '../../../core/models/watches/watch-branch-update-request.dto';
import { WatchSerialNumberUpdateRequestDTO } from '../../../core/models/watches/watch-serial-number-update-request.dto';
import { WatchPhotosView } from '../watch-photos-view/watch-photos-view';
import { routes } from '../../../app.routes';
import { Router } from '@angular/router';

@Component({
  selector: 'app-watch-full-info-view',
  imports: [FormsModule, WatchCalendar, ReactiveFormsModule, WatchPhotosView],
  templateUrl: './watch-full-info-view.html',
  styleUrl: './watch-full-info-view.css',
})
export class WatchFullInfoView {
  private fb = inject(FormBuilder);
  private branchesService = inject(BranchesService);
  private router = inject(Router);


  watch = input.required<WatchCardResponseDTO>();

  branchOptions = signal<BranchResponseDTO[]>([]);

  private watchesService = inject(WatchesService);
  private profileService = inject(ProfileService);
  public isLogged: boolean = false;

  date: Date | null = null;

  profile = signal<UserResponseDTO | null>(null);
  watchFullInfo = signal<WatchFullInfoResponseDTO | null>(null);

  close = output<null>();
  watchUpdated = output<void>();

  conditionOptions = Object.values(WatchCondition);
  genderOptions = Object.values(WatchGender);
  movementTypeOptions = Object.values(WatchMovementType);
  watchTypeOptions = Object.values(WatchType);
  watchStatusOptions = Object.values(WatchStatus);

  changeInfomationAdmin = signal(false);
  buttonVanish: boolean = false;
  saveType?: number | null = null;

  watchUpdate = this.fb.group({
    manufacturer: [''],
    model: [''],
    referenceNumber: [''],
    movement: [''],
    description: [''],
    yearOfProduction: [this.fb.control<number | null>(null)],
    pricePerDay: [this.fb.control<number | null>(null)],
    condition: [this.fb.control<WatchCondition | null>(null)],
    gender: [this.fb.control<WatchGender | null>(null)],
    movementType: [this.fb.control<WatchMovementType | null>(null)],
    watchType: [this.fb.control<WatchType | null>(null)],
    status: [this.fb.control<WatchStatus | null>(null)],
    branchId: [''],
    serialNumber: ['']
  })

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
    const value = this.watchUpdate.getRawValue();
    if (this.saveType == null) { }
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
      }

      this.watchesService.updateWatch(id, request).subscribe(
        response => {
          console.log(response);
          this.watchUpdated.emit();
          this.cancel();
          this.watchReload(id);

        }
      )
    }
    else if (this.saveType == 2) {
      const request: WatchStatusUpdateRequestDTO = {
        status: value.status!
      }

      this.watchesService.updateWatchStatus(id, request).subscribe(
        response => {
          console.log(response);
          this.watchUpdated.emit();
          this.cancel();
          this.watchReload(id);
        }
      )
    }
    else if (this.saveType == 3) {
      const request: WatchBranchUpdateRequestDTO = {
        branchId: value.branchId!
      }
      console.log(request);
      this.watchesService.updateWatchBranch(id, request).subscribe(
        response => {
          console.log(response);
          this.watchUpdated.emit();
          this.cancel();
          this.watchReload(id);

        }
      )
    }
    else if (this.saveType == 4) {
      const request: WatchSerialNumberUpdateRequestDTO = {
        serialNumber: value.serialNumber!
      }

      this.watchesService.updateWatchSerialNumber(id, request).subscribe(
        response => {
          console.log(response);
          this.watchUpdated.emit();
          this.cancel();
          this.watchReload(id);

        }
      )
    }

    this.cancel();
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
