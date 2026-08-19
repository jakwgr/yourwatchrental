import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchesInfoView } from './branches-info-view';

describe('BranchesInfoView', () => {
  let component: BranchesInfoView;
  let fixture: ComponentFixture<BranchesInfoView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchesInfoView],
    }).compileComponents();

    fixture = TestBed.createComponent(BranchesInfoView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
