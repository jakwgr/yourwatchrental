import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

ngOnInit()
  {
    if(this.authService.isLoggedIn())
    {
      this.router.navigate(['/']);
    }
  }

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(5)]]
  });

  login() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    const login = this.loginForm.getRawValue();

    this.authService.login(login).subscribe({
      next: response => {
        this.router.navigate(['/profile']);
      },
      error: error => {
        console.error('BŁĄD:', error);
      }
    }
    );

  }
}
