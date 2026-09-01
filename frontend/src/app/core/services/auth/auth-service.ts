import { HttpClient } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { UserRegisterRequestDTO } from '../../models/profile/register/user-register-request.dto';
import { UserResponseDTO } from '../../models/profile/user-response.dto';
import { UserLoginRequestDTO } from '../../models/profile/register/user-login-request.dto';
import { TokenStorageService } from './token-storage-service';
import { tap } from 'rxjs';

@Service()
export class AuthService {

    private http = inject(HttpClient);
    private tokenStorageService = inject(TokenStorageService);

    isLoggedIn = signal(this.checkToken());

    register(request: UserRegisterRequestDTO) {
        return this.http.post<UserResponseDTO>(
            '/api/users/signup',
            request
        );
    }

    login(request: UserLoginRequestDTO) {
        return this.http
            .post('/api/users/signin', request, { responseType: 'text' })
            .pipe(
                tap(token => {
                    this.tokenStorageService.saveToken(token);
                    this.isLoggedIn.set(true);
                })
            );
    }

    checkToken(): boolean {

        const token = this.tokenStorageService.getToken();

        if (!token) {
            return false;
        }

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));

            if (payload.exp * 1000 <= Date.now()) {
                this.tokenStorageService.removeToken();
                return false;
            }

            return true;

        } catch {
            this.tokenStorageService.removeToken();
            return false;
        }
    }

    logout(): void {
        this.tokenStorageService.removeToken();
        this.isLoggedIn.set(false);
    }

    forgotPassword(email: string) {
        return this.http.post<void>(
            '/api/auth/forgot-password',
            { email }
        );
    }

    resetPassword(
        userId: string,
        token: string,
        newPassword: string
    ) {
        return this.http.post<void>(
            '/api/auth/reset-password',
            {
                userId,
                token,
                newPassword
            }
        );
    }
}