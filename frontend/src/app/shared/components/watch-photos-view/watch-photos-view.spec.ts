import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchPhotosView } from './watch-photos-view';

describe('WatchPhotosView', () => {
  let component: WatchPhotosView;
  let fixture: ComponentFixture<WatchPhotosView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchPhotosView],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchPhotosView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
