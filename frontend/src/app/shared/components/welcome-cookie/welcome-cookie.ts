import { Component, signal } from '@angular/core';
signal
@Component({
  selector: 'app-welcome-cookie',
  imports: [],
  templateUrl: './welcome-cookie.html',
  styleUrl: './welcome-cookie.css',
})
export class WelcomeCookie {
  showModal = signal(false);

  private readonly cookieName = 'welcomeModalShown';
  private readonly cookieLifetimeDays = 2;

  language = signal<'PL' | 'EN'>('PL');

  setLanguage(language: 'PL' | 'EN') {
    this.language.set(language);
  }


  
  ngOnInit() {

    const cookieExists = this.getCookie(this.cookieName);


    if (!cookieExists) {
      this.showModal.set(true);
    document.body.style.overflow = "hidden";

    }

  }


  closeModal() {
    document.body.style.overflow = "";
    this.showModal.set(false);

    this.setCookie(
      this.cookieName,
      'true',
      this.cookieLifetimeDays
    );



  }


  private getCookie(name: string): string | null {

    const cookies = document.cookie.split(';');

    for (const cookie of cookies) {

      const [key, value] = cookie.trim().split('=');

      if (key === name) {
        return value;
      }

    }

    return null;

  }


  private setCookie(
    name: string,
    value: string,
    days: number
  ) {

    const date = new Date();

    date.setTime(
      date.getTime() +
      days * 24 * 60 * 60 * 1000
    );

    document.cookie =
      `${name}=${value};expires=${date.toUTCString()};path=/;SameSite=Lax`;

  }

}
