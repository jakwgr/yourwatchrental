import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchDetails } from './watch-details';

describe('WatchDetails', () => {
  let component: WatchDetails;
  let fixture: ComponentFixture<WatchDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchDetails],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
