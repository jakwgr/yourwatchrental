import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth/auth-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  router = inject(Router);

  resetSuccess = signal<boolean>(false);

  loading = false;
  success = false;
  error = false;

  forgotPasswordForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]]
  });

  submit(): void {

    if (this.forgotPasswordForm.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.success = false;
    this.error = false;

    const email = this.forgotPasswordForm.getRawValue().email;

    this.authService.forgotPassword(email).subscribe({
      next: () => {
        this.resetSuccess.set(true);
      },
      error: () => {
          this.router.navigate(['error-something-went-wrong'])
      }
    });
  }
}