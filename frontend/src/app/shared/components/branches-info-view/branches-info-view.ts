import { Component, effect, inject, input, output, signal } from '@angular/core';
import { BranchResponseDTO } from '../../../core/models/branch/branch-response.dto';
import { RouterLink } from "@angular/router";
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BranchesService } from '../../../core/services/branches/branches-service';
import { BranchStatus, BranchStatusLabel } from '../../../core/models/branch/enums/branch-status';
import { onlyNumbers } from '../../util/form-util';
import { SmallErrorView } from '../small-error-view/small-error-view';
import { FormError } from '../form-error/form-error';

@Component({
  selector: 'app-branches-info-view',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    FormError,
    SmallErrorView
  ],
  templateUrl: './branches-info-view.html',
  styleUrl: './branches-info-view.css',
})
export class BranchesInfoView {

  branch = input.required<BranchResponseDTO>();
  role = input.required<string | undefined | null>();

  branchesService = inject(BranchesService);

  error = signal<string | null>(null);

  buttonVanish: boolean = false;
  saveType?: number | null = null;

  branchUpdated = output<void>();

  branchOptions = Object.values(BranchStatus);
  branchLabel = BranchStatusLabel;


  // FORMATOWANIE TELEFONU

  formatPhoneNumber(phone: string): string {
    return phone.replace(/\B(?=(\d{3})+(?!\d))/g, '-');
  }


  // FORMULARZE

  private fb = inject(FormBuilder);

  branchUpdate = this.fb.group({
    name: ['', Validators.required],
    city: ['', Validators.required],
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

  branchUpdateStatus = this.fb.group({
    status: ['', Validators.required]
  });


  // TYLKO CYFRY

  onlyNumbers(event: Event) {
    onlyNumbers(event);
  }


  // USTAWIENIE DANYCH W FORMULARZU

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


  // RESET FORMULARZY

  resetForms() {
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

    this.branchUpdate.markAsPristine();
    this.branchUpdate.markAsUntouched();

    this.branchUpdateStatus.markAsPristine();
    this.branchUpdateStatus.markAsUntouched();
  }


  // EDYCJA INFORMACJI

  showUpdate = signal(false);

  closeUpdate() {
    this.showUpdate.set(false);
  }

  openUpdate() {
    this.resetForms();

    this.error.set(null);

    this.showUpdate.set(true);
    this.saveType = 1;
    this.buttonVanish = true;
  }


  // EDYCJA STATUSU

  showUpdateStatus = signal(false);

  closeUpdateStatus() {
    this.showUpdateStatus.set(false);
  }

  openUpdateStatus() {
    this.resetForms();

    this.error.set(null);

    this.showUpdateStatus.set(true);
    this.saveType = 2;
    this.buttonVanish = true;
  }


  // ZAPIS

  save(id: string) {

    this.error.set(null);

    if (this.saveType === 1) {

      this.branchesService
        .updateBranch(id, this.branchUpdate.getRawValue())
        .subscribe({

          next: response => {
            console.log('sukces', response);

            this.branchUpdated.emit();
            this.cancel();
          },

          error: err => {
            if (err.status === 409 || err.status === 404) {
              this.error.set(err.error.message);
            }
          }

        });

    } else if (this.saveType === 2) {

      this.branchesService
        .updateBranchStatus(id, this.branchUpdateStatus.getRawValue())
        .subscribe({

          next: response => {
            console.log('sukces', response);

            this.branchUpdated.emit();
            this.cancel();
          },

          error: err => {
            if (err.status === 409 || err.status === 404) {
              this.error.set(err.error.message);
            }
          }

        });
    }
  }


  // ANULOWANIE

  cancel() {

    // PRZYWRACA ORYGINALNE DANE
    this.resetForms();

    this.saveType = null;
    this.buttonVanish = false;

    this.error.set(null);

    this.closeUpdate();
    this.closeUpdateStatus();
  }
}
