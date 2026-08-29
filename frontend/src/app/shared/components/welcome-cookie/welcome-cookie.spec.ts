import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeCookie } from './welcome-cookie';

describe('WelcomeCookie', () => {
  let component: WelcomeCookie;
  let fixture: ComponentFixture<WelcomeCookie>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WelcomeCookie],
    }).compileComponents();

    fixture = TestBed.createComponent(WelcomeCookie);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
