import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchCard } from './watch-card-view';

describe('WatchCard', () => {
  let component: WatchCard;
  let fixture: ComponentFixture<WatchCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchCard],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
