import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortfolioProjectAlert2 } from './portfolio-project-alert-2';

describe('PortfolioProjectAlert2', () => {
  let component: PortfolioProjectAlert2;
  let fixture: ComponentFixture<PortfolioProjectAlert2>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioProjectAlert2],
    }).compileComponents();

    fixture = TestBed.createComponent(PortfolioProjectAlert2);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
