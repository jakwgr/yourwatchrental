import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, NavigationError, Router, RouterOutlet } from '@angular/router';
import { Navbar } from './shared/components/navbar-view/navbar-view';
import { FooterView } from './shared/components/footer-view/footer-view';
import { WelcomeCookie } from './shared/components/welcome-cookie/welcome-cookie';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, FooterView, WelcomeCookie],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  private router = inject(Router);

constructor() {
  this.router.events.subscribe(event => {

    if (event instanceof NavigationError) {

      console.error('Błąd routingu:', event.error);

      this.router.navigate([
        '/error-something-went-wrong'
      ]);

    }

      this.router.events
    .pipe(
      filter(event => event instanceof NavigationEnd)
    )
    .subscribe(() => {

      if (history.state?.modal) {
        return;
      }
      window.scrollTo(0, 0);
    });

  });
}
}
