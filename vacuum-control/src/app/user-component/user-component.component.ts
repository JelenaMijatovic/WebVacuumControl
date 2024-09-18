import {Component, ViewChild, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {User} from "../model";
import {AlertService} from "../services/alert.service";
import {MatTable} from "@angular/material/table";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-user-component',
  templateUrl: './user-component.component.html',
  styleUrls: ['./user-component.component.css']
})
export class UserComponentComponent implements OnInit {

  hasPerms: boolean
  canRead: boolean
  canUpdate: boolean
  canDelete: boolean
  permissions: Array<string>
  @ViewChild(MatTable) table!: MatTable<User>;
  users: Array<User>
  displayedColumns: Array<string>

  constructor(private router: Router, private authService: AuthService, private userService: UserService, private alertService : AlertService) {
    this.hasPerms = false;
    this.canRead = false;
    this.canUpdate = false;
    this.canDelete = false;
    this.permissions = [];
    this.users = [];
    this.displayedColumns = ['email', 'name', 'surname', 'permissions', 'delete'];
  }

  ngOnInit(): void {
    let user = this.authService.getUser();
    if (user) {
      let userJ = JSON.parse(user);
      for (let perm of userJ.permissions) {
        this.permissions.push(perm.permission);
      }
      //console.log(this.permissions);
      this.hasPerms = !(this.permissions.length == 0);
      this.canRead = this.permissions.includes("can_read_users");
      this.canUpdate = this.permissions.includes("can_update_users");
      this.canDelete = this.permissions.includes("can_delete_users")
    }
    if (this.hasPerms) {
      if (this.canRead) {
        this.updateTable();
      }
    } else {
      this.alertService.throwAlert("You do not have any permissions!");
    }
  }

  updateTable() {
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users.length = 0;
        for (let u of data) {
          this.users.push(u);
        }
        this.table.renderRows();
      },
      error: () => {
        console.log("You do not have permission to view users!");
      }
    });
  }

  delete(email:string) {
    this.userService.deleteUser(email).subscribe({
      next: () => {
        this.updateTable();
        this.alertService.throwAlert("User " + email + " successfully deleted!")
      },
      error: (e) => {
        console.log(e);
      }
    })
  }

}
