import { Injectable, Service } from '@angular/core';

@Service()

export class TokenStorageService {

    saveToken(jwt: string): void {
        localStorage.setItem('token', jwt);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    removeToken(): void {
        localStorage.removeItem('token');
    }
}