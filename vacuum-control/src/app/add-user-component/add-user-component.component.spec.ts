import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUserComponentComponent } from './add-user-component.component';

describe('AddUserComponentComponent', () => {
  let component: AddUserComponentComponent;
  let fixture: ComponentFixture<AddUserComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddUserComponentComponent]
    });
    fixture = TestBed.createComponent(AddUserComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
