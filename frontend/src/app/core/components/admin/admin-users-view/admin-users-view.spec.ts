import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsersView } from './admin-users-view';

describe('AdminUsersView', () => {
  let component: AdminUsersView;
  let fixture: ComponentFixture<AdminUsersView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminUsersView],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminUsersView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
