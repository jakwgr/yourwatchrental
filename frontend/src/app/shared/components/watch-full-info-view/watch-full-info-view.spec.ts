import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchFullInfoView } from './watch-full-info-view';

describe('WatchFullInfoView', () => {
  let component: WatchFullInfoView;
  let fixture: ComponentFixture<WatchFullInfoView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchFullInfoView],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchFullInfoView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
