import { Component } from '@angular/core';
import { Rentals } from '../../rentals/rentals';
@Component({
  selector: 'app-admin-rentals',
  imports: [Rentals],
  templateUrl: './admin-rentals.html',
  styleUrl: './admin-rentals.css',
})
export class AdminRentals {}
