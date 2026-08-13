import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileUpdateInformationView } from './profile-update-information-view';

describe('ProfileUpdateInformationView', () => {
  let component: ProfileUpdateInformationView;
  let fixture: ComponentFixture<ProfileUpdateInformationView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileUpdateInformationView],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileUpdateInformationView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
