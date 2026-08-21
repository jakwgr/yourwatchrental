import { Component, inject, output, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WatchesService } from '../../core/services/watches/watches-service';
import { WatchFullInfoResponseDTO } from '../../core/models/watches/watch-full-info-response.dto';
import { WatchCalendar } from '../../shared/components/watch-calendar/watch-calendar';
import { ReactiveFormsModule } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
// import {}

@Component({
  selector: 'app-rental-create',
  imports: [WatchCalendar, ReactiveFormsModule],
  templateUrl: './rental-create.html',
  styleUrl: './rental-create.css',
})
export class RentalCreate {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private watchesService = inject(WatchesService);
 
  watchInfo = signal<WatchFullInfoResponseDTO | null>(null);
  isDatePicker: any;

  datePickerEndDate = signal<number>(-1);
  datePickerStartDate = signal<number>(-1);

  datePickerStartFinish(endDate: number)
  {
    this.datePickerStartDate.set(endDate);
  }
  datePickerEndFinish(startDate: number)
  {
    this.datePickerEndDate.set(startDate);
  }

  getWatchInfo()
  {
    const watch = this.route.snapshot.paramMap.get('id');
    if(watch != null)
    {
      console.log(watch);
      this.watchesService.getWatch(watch).subscribe(
        response => {
          this.watchInfo.set(response);
        }
      )
    }
    else
    {
      this.router.navigate(['/']);
    }
  }

  ngOnInit()
  {
    this.getWatchInfo();
  }
}
