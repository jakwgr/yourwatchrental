import { Component, inject, input, output, signal } from '@angular/core';
import { WatchCardResponseDTO } from '../../../core/models/watches/watch-card-response.dto';
import { WatchesService } from '../../../core/services/watches/watches-service';
import { ProfileService } from '../../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { WatchFullInfoResponseDTO } from '../../../core/models/watches/watch-full-info-response.dto';
import { FormsModule } from '@angular/forms';
import { WatchCalendar } from '../watch-calendar/watch-calendar';

@Component({
  selector: 'app-watch-full-info-view',
  imports: [FormsModule, WatchCalendar],
  templateUrl: './watch-full-info-view.html',
  styleUrl: './watch-full-info-view.css',
})
export class WatchFullInfoView {
  watch = input.required<WatchCardResponseDTO>();

  private watchesService = inject(WatchesService);
  private profileService = inject(ProfileService);
  public isLogged : boolean = false;

  date: Date| null = null;

  profile = signal<UserResponseDTO | null>(null);
  watchFullInfo = signal<WatchFullInfoResponseDTO | null>(null);

  close = output<null>();

  ngOnInit()
  {
    this.profileService.getMyProfile().subscribe({
      next: response => {
        this.profile.set(response);
        this.isLogged = true;
      },
      error: erorr =>
      {
        console.log("blad");
        this.isLogged = false;
      }
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

  closeModal()
  {
      this.close.emit(null);
  }
}
