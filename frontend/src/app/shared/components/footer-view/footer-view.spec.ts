import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FooterView } from './footer-view';

describe('FooterView', () => {
  let component: FooterView;
  let fixture: ComponentFixture<FooterView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterView],
    }).compileComponents();

    fixture = TestBed.createComponent(FooterView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
