import { HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  return next(req).pipe(

    catchError(error => {

      console.error('Błąd HTTP:', error);

      if (error.status >= 500 && error.status < 600) {
    router.navigate(['/error-something-went-wrong']);
}

      switch (error.status) {

        case 400:
          console.error('Bad Request');
          break;
          
        case 0:
          console.error('Brak połączenia z serwerem');
          break;

      }

      return throwError(() => error);

    })

  );

};