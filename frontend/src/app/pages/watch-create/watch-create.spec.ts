import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchCreate } from './watch-create';

describe('WatchCreate', () => {
  let component: WatchCreate;
  let fixture: ComponentFixture<WatchCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchCreate],
    }).compileComponents();

    fixture = TestBed.createComponent(WatchCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
