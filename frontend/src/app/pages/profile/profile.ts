import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { UserResponseDTO } from '../../core/models/profile/user-response.dto';

import { AuthService } from '../../core/services/auth/auth-service';
import { ProfileService } from '../../core/services/profile/profile-service';
import { ProfileView } from '../../core/components/profile/profile-view/profile-view';
@Component({
  selector: 'app-profile',
  imports: [ProfileView, RouterLink],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})

export class Profile {
  private authService = inject(AuthService);
  private profileService = inject(ProfileService);

  profile = signal<UserResponseDTO | null>(null);

  ngOnInit()
  {
    this.profileService.getMyProfile().subscribe(
      response => {
        this.profile.set(response);
      }
    )
  }

  logout() {
    this.authService.logout();
  }
}
