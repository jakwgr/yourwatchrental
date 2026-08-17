import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRentals } from './admin-rentals';

describe('AdminRentals', () => {
  let component: AdminRentals;
  let fixture: ComponentFixture<AdminRentals>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRentals],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminRentals);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
