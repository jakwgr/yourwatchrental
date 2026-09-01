import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth-service';
import { single } from 'rxjs';


@Component({
  selector: 'app-reset-password',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
})
export class ResetPassword {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  router = inject(Router);

  userId = '';
  token = '';

  loading = false;
  success = false;
  error = false;

  resetSuccess = signal<boolean>(false);

  resetPasswordForm = this.fb.nonNullable.group({
    newPassword: ['', [Validators.required, Validators.minLength(5)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(5)]]
  });

  constructor() {
    this.userId = this.route.snapshot.queryParamMap.get('userId') ?? '';
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
  }
passwordMatchValidator(form: AbstractControl) {
  const password = form.get('password')?.value;
  const confirmPassword = form.get('confirmPassword')?.value;

  return password === confirmPassword
    ? null
    : { passwordMismatch: true };
}
  submit(): void {

    if (this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }

    
    const {
      newPassword,
      confirmPassword
    } = this.resetPasswordForm.getRawValue();

    if (newPassword !== confirmPassword) {
      this.error = true;
      return;
    }

    this.loading = true;
    this.error = false;

    this.authService.resetPassword(
      this.userId,
      this.token,
      newPassword
    ).subscribe({
      next: () => {
        this.resetSuccess.set(true);
      },
      error: () => {
          this.router.navigate(['error-something-went-wrong'])
      }
    });
  }
}