import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './shared/components/navbar-view/navbar-view';
import { FooterView } from './shared/components/footer-view/footer-view';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, FooterView],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
