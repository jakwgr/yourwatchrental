import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminWatchesView } from './admin-watches-view';

describe('AdminWatchesView', () => {
  let component: AdminWatchesView;
  let fixture: ComponentFixture<AdminWatchesView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminWatchesView],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminWatchesView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
