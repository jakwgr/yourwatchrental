import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortfolioProjectAlert1 } from './portfolio-project-alert-1';

describe('PortfolioProjectAlert1', () => {
  let component: PortfolioProjectAlert1;
  let fixture: ComponentFixture<PortfolioProjectAlert1>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioProjectAlert1],
    }).compileComponents();

    fixture = TestBed.createComponent(PortfolioProjectAlert1);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
