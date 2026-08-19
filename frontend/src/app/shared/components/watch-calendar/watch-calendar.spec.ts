import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchCalendar } from './watch-calendar';

describe('WatchCalendar', () => {
  let component: WatchCalendar;
  let fixture: ComponentFixture<WatchCalendar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchCalendar],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchCalendar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
