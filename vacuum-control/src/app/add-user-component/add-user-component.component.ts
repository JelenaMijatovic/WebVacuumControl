import { Component } from '@angular/core';
import {Permission} from "../model";
import {UserService} from "../services/user.service";
import {AlertService} from "../services/alert.service";

@Component({
  selector: 'app-add-user-component',
  templateUrl: './add-user-component.component.html',
  styleUrls: ['./add-user-component.component.css']
})
export class AddUserComponentComponent {

  name: string
  surname: string
  email: string
  permissions: Array<Permission>

  constructor(private userService: UserService, private alertService : AlertService) {
    this.name = "";
    this.surname = "";
    this.email = "";
    this.permissions = [];
  }

  save() {
    this.userService.saveUser(this.name, this.surname, this.email, this.permissions).subscribe({
      next: (data) => {
        this.alertService.throwAlert("Successfully created user " + data.email + "!");
      },
      error: (e) => {
        this.alertService.throwAlert("Error while creating user " + this.email + "!");
        console.log(e);
      }
    })
  }
}
