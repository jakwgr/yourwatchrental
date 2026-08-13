import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileChangeEmailView } from './profile-change-email-view';

describe('ProfileChangeEmailView', () => {
  let component: ProfileChangeEmailView;
  let fixture: ComponentFixture<ProfileChangeEmailView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileChangeEmailView],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileChangeEmailView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
