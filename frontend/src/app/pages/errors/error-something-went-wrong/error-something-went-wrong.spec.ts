import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorSomethingWentWrong } from './error-something-went-wrong';

describe('ErrorSomethingWentWrong', () => {
  let component: ErrorSomethingWentWrong;
  let fixture: ComponentFixture<ErrorSomethingWentWrong>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ErrorSomethingWentWrong],
    }).compileComponents();

    fixture = TestBed.createComponent(ErrorSomethingWentWrong);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
