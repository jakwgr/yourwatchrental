import { Component, effect, inject, signal } from '@angular/core';
import { AuthService } from '../../../core/services/auth/auth-service';
import { Profile } from '../../../pages/profile/profile';
import { ProfileService } from '../../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-footer-view',
  imports: [RouterLink],
  templateUrl: './footer-view.html',
  styleUrl: './footer-view.css',
})
export class FooterView {
  authService = inject(AuthService);
  profileService = inject(ProfileService);

  profile = signal<UserResponseDTO | null>(null);

  isMenuOpen = false;

  constructor() {
    effect(() => {
        if (this.authService.isLoggedIn()) {
            this.profileService.getMyProfile().subscribe({
                next: response => {
                    this.profile.set(response);
                }
            });
        } else {
            this.profile.set(null);
        }
    });
}
}
