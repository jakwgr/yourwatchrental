import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorageService } from '../services/auth/token-storage-service';
import { Token } from '@angular/compiler';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenStorageService = inject(TokenStorageService);
  const token =  tokenStorageService.getToken();

  if(token)
  {
    const authRequest = req.clone({
      setHeaders : {
        Authorization: `Bearer ${token}`
      }
    });

    return next(authRequest);
  }
  return next(req);
};
