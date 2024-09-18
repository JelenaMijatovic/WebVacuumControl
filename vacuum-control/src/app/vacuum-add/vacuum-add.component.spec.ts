import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacuumAddComponent } from './vacuum-add.component';

describe('VacuumAddComponent', () => {
  let component: VacuumAddComponent;
  let fixture: ComponentFixture<VacuumAddComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VacuumAddComponent]
    });
    fixture = TestBed.createComponent(VacuumAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
