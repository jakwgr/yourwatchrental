import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileSoftDeleteView } from './profile-soft-delete-view';

describe('ProfileSoftDeleteView', () => {
  let component: ProfileSoftDeleteView;
  let fixture: ComponentFixture<ProfileSoftDeleteView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileSoftDeleteView],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileSoftDeleteView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
