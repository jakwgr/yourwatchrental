import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentalView } from './rental-view';

describe('RentalView', () => {
  let component: RentalView;
  let fixture: ComponentFixture<RentalView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RentalView],
    }).compileComponents();

    fixture = TestBed.createComponent(RentalView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
