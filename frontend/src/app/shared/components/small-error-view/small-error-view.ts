import { Component, input } from '@angular/core';

@Component({
  selector: 'app-small-error-view',
  imports: [],
  templateUrl: './small-error-view.html',
  styleUrl: './small-error-view.css',
})
export class SmallErrorView {
   message = input.required<string>();
}
