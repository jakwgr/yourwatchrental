import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchPhotoZoomComponent } from './watch-photo-zoom-component';

describe('WatchPhotoZoomComponent', () => {
  let component: WatchPhotoZoomComponent;
  let fixture: ComponentFixture<WatchPhotoZoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchPhotoZoomComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchPhotoZoomComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
