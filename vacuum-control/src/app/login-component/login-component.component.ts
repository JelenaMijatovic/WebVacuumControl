import {Component} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {
  Router
} from '@angular/router';
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-login-component',
  templateUrl: './login-component.component.html',
  styleUrls: ['./login-component.component.css']
})
export class LoginComponentComponent {

  email: string;
  password: string;

  constructor(private router: Router, private authService: AuthService, private userService: UserService) {
    this.email = "";
    this.password = "";
  }


  login() {
    this.authService.login(this.email, this.password)
      .subscribe( {
        next: (data) => {
          this.authService.setToken(JSON.stringify(data.jwt));
          this.userService.getMe().subscribe({
            next: (user) => {
              this.authService.setUser(JSON.stringify(user));
              this.authService.loggedIn();
              this.router.navigateByUrl('/users');
            },
            error: (e) => {
              console.log(e);
            }})
        }, error: (e) => {
          console.log(e);
        }});
  }
}
