import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminWatches } from './admin-watches';

describe('AdminWatches', () => {
  let component: AdminWatches;
  let fixture: ComponentFixture<AdminWatches>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminWatches],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminWatches);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
