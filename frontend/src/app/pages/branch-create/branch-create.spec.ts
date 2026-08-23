import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchCreate } from './branch-create';

describe('BranchCreate', () => {
  let component: BranchCreate;
  let fixture: ComponentFixture<BranchCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchCreate],
    }).compileComponents();

    fixture = TestBed.createComponent(BranchCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
