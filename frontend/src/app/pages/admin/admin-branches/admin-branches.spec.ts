import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminBranches } from './admin-branches';

describe('AdminBranches', () => {
  let component: AdminBranches;
  let fixture: ComponentFixture<AdminBranches>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminBranches],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminBranches);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
