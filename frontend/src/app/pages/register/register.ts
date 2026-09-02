import { Component, inject, input, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { first } from 'rxjs';
import { AuthService } from '../../core/services/auth/auth-service';
import { pastDateValidator } from '../../shared/util/validators/validator-past'
import { Router, RouterLink } from '@angular/router';
import { SmallErrorView } from '../../shared/components/small-error-view/small-error-view';
import { FormError } from '../../shared/components/form-error/form-error';
import { onlyNumbers } from '../../shared/util/form-util';
import { PortfolioProjectAlert2 } from '../../shared/components/portfolio-project-alert-2/portfolio-project-alert-2';


@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, SmallErrorView, FormError, RouterLink, PortfolioProjectAlert2],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

registerError = signal<string | null>(null);

  ngOnInit()
  {
    if(this.authService.isLoggedIn())
    {
      this.router.navigate(['/']);
    }
  }

  onlyNumber(event: Event)
  {
    onlyNumbers(event);
  }

  registerForm = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    dateOfBirth: ['', [Validators.required, pastDateValidator]],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(5)]]
  })

  register() {
    if(this.registerForm.invalid)
    {
      this.registerForm.markAllAsTouched();
      return;
    }

    const register = this.registerForm.getRawValue();

    this.authService.register(register).subscribe({
      next: response => {
        this.router.navigate(['/login']);
      },
      error: err => {
        if (err.status === 409) {
        this.registerError.set(err.error.message);
      }
      }}
    );
  }
}
