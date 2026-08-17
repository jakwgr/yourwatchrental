import { Component, effect, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth-service';
import { ProfileService } from '../../../core/services/profile/profile-service';
import { UserResponseDTO } from '../../../core/models/profile/user-response.dto';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar-view.html',
  styleUrl: './navbar-view.css',
})
export class Navbar {
  authService = inject(AuthService);
  profileService = inject(ProfileService);

  profile = signal<UserResponseDTO | null>(null);
  
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

    logout() {
    this.authService.logout();
  }
}
