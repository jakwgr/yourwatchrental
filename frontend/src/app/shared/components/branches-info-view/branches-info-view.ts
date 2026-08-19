import { Component, input } from '@angular/core';
import { BranchResponseDTO } from '../../../core/models/branch/branch-response.dto';
import { RouterLink } from "@angular/router";
@Component({
  selector: 'app-branches-info-view',
  imports: [RouterLink],
  templateUrl: './branches-info-view.html',
  styleUrl: './branches-info-view.css',
})
export class BranchesInfoView {
  branch = input.required<BranchResponseDTO>();

  formatPhoneNumber(phone: string): string {
    return phone.replace(/\B(?=(\d{3})+(?!\d))/g, '-');
}
}
