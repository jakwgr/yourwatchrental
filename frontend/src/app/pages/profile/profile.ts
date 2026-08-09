import { Component, inject } from '@angular/core';
import { AuthService } from '../../core/services/auth/auth';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  private authService = inject(AuthService);

logout() {
    this.authService.logout();
}
}
