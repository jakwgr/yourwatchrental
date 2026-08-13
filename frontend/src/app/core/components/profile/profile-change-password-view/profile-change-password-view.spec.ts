import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileChangePasswordView } from './profile-change-password-view';

describe('ProfileChangePasswordView', () => {
  let component: ProfileChangePasswordView;
  let fixture: ComponentFixture<ProfileChangePasswordView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileChangePasswordView],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileChangePasswordView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
