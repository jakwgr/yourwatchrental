import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmallErrorView } from './small-error-view';

describe('SmallErrorView', () => {
  let component: SmallErrorView;
  let fixture: ComponentFixture<SmallErrorView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SmallErrorView],
    }).compileComponents();

    fixture = TestBed.createComponent(SmallErrorView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
