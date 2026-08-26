import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth/auth-service';
import { Router } from '@angular/router';
import { SmallErrorView } from '../../shared/components/small-error-view/small-error-view';
import { FormError } from '../../shared/components/form-error/form-error';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, SmallErrorView, FormError],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);


  loginError = signal<string | null>(null);

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
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
      error: err => {
        const error = JSON.parse(err.error);

        this.loginError.set(error.message);
      }
    });

  }
}
