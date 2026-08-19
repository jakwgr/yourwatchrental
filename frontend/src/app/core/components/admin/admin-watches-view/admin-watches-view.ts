import { Component, inject, OnInit, signal } from '@angular/core';
import { WatchesService } from '../../../services/watches/watches-service';
import { WatchCardResponseDTO } from '../../../models/watches/watch-card-response.dto';
import { WatchCard } from '../../../../shared/components/watch-card-view/watch-card-view';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { WatchGender } from '../../../models/watches/enums/watch-gender';
import { WatchMovementType } from '../../../models/watches/enums/watch-movement-type';
import { WatchStatus } from '../../../models/watches/enums/watch-status';
import { WatchType } from '../../../models/watches/enums/watch-type';

@Component({
  selector: 'app-admin-watches-view',
  imports: [],
  templateUrl: './admin-watches-view.html',
  styleUrl: './admin-watches-view.css',
})
export class AdminWatchesView {
}
