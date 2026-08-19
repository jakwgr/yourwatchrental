import { Component, effect, inject, input, output, signal } from '@angular/core';
import { BranchResponseDTO } from '../../../core/models/branch/branch-response.dto';
import { RouterLink } from "@angular/router";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { BranchesService } from '../../../core/services/branches/branches-service';
import { BranchStatus } from '../../../core/models/branch/enums/branch-status';

@Component({
  selector: 'app-branches-info-view',
  imports: [RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './branches-info-view.html',
  styleUrl: './branches-info-view.css',
})
export class BranchesInfoView {
  branch = input.required<BranchResponseDTO>();
  role = input.required<string | undefined | null>();

  branchesService = inject(BranchesService);

  buttonVanish: boolean = false;
  saveType?: number | null = null;

  branchUpdated = output<void>();

  branchOptions = Object.values(BranchStatus);
  formatPhoneNumber(phone: string): string {
    return phone.replace(/\B(?=(\d{3})+(?!\d))/g, '-');
  }

  private fb = inject(FormBuilder);

  branchUpdate = this.fb.group({
    name: [''],
    city: [''],
    address: [''],
    phoneNumber: [''],
    email: ['']
  })

  branchUpdateStatus = this.fb.group({
    status: ['']
  })

  constructor() {
    effect(() => {
      const branch = this.branch();

      this.branchUpdate.patchValue({
        name: branch.name,
        city: branch.city,
        address: branch.address,
        phoneNumber: branch.phoneNumber,
        email: branch.email
      });

      this.branchUpdateStatus.patchValue({
        status: branch.status
      });
    });
  }

  showUpdate = signal(false);
  closeUpdate() {
    this.showUpdate.set(false);
  }
  openUpdate() {
    this.showUpdate.set(true);
    this.saveType = 1;
    this.buttonVanish = true;
  }

  showUpdateStatus = signal(false);
  closeUpdateStatus() {
    this.showUpdateStatus.set(false);
  }
  openUpdateStatus() {
    this.showUpdateStatus.set(true);
    this.saveType = 2;
    this.buttonVanish = true;
  }



  save(id: string) {
    if (this.saveType && this.saveType === 1) {
      this.branchesService.updateBranch(id, this.branchUpdate.getRawValue()).subscribe(
        response => {
          console.log(response);
          this.branchUpdated.emit();

          this.cancel();
        }
      )
    }
    else if (this.saveType && this.saveType === 2) {
      this.branchesService.updateBranchStatus(id, this.branchUpdateStatus.getRawValue()).subscribe(
        response => {
          console.log(response);
          this.branchUpdated.emit();

          this.cancel();
        }
      )
    }
  }


  cancel() {
    this.saveType = null;
    this.buttonVanish = false;
    this.closeUpdate();
    this.closeUpdateStatus();
  }
}
