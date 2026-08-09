import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { UserRegisterRequestDTO } from '../../models/register/user-register-request.dto';
import { UserResponseDTO } from '../../models/register/user-response.dto';
import { UserLoginRequestDTO } from '../../models/register/user-login-request.dto';
import { TokenStorageService } from './token-storage';
import { tap } from 'rxjs';
import { Token } from '@angular/compiler';

@Service()

export class AuthService {
    private http = inject(HttpClient);
    private tokenStorageService = inject(TokenStorageService)
    register(request: UserRegisterRequestDTO)
    {
        return this.http.post<UserResponseDTO>('/api/users/signup',request);
    }

    login(request: UserLoginRequestDTO)
    {
        return this.http.post('/api/users/signin', request, {responseType: 'text'}).pipe(
            tap(token => this.tokenStorageService.saveToken(token))
        )
    }

    isLoggedIn() : boolean {
        return this.tokenStorageService.getToken() !== null;
    }

    logout() : void{
        this.tokenStorageService.removeToken();
    }
}
