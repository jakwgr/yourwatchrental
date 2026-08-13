import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { first } from 'rxjs';
import { AuthService } from '../../core/services/auth/auth-service';
import { pastDateValidator } from '../../shared/util/validators/validator-past'
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
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

  registerForm = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    dateOfBirth: ['', [Validators.required, pastDateValidator]],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(5)], ]
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
        console.log('suckes', response);
        this.router.navigate(['/login']);
      },
      error: error => {
        console.error('BŁĄD:', error);
      }
    }
    );
  }
}
