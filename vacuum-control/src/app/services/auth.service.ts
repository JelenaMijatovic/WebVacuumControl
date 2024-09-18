import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, ReplaySubject, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private readonly apiUrl = "http://localhost:8080"
  private loginValue: Subject<boolean> = new ReplaySubject<boolean>(1);

  constructor(private httpClient: HttpClient) {

  }

  login(email: String, password: String) {
    return this.httpClient.post<any>(`${this.apiUrl}/auth/login`, { email: email, password: password });
  }

  loggedIn() {
    this.loginValue.next(true);
  }

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    this.loginValue.next(false)
  }


  loginStatusChange(): Observable<boolean> {
    return this.loginValue.asObservable();
  }

  getToken() {
    return localStorage.getItem('token');
  }

  setToken(jwt: string) {
    localStorage.setItem('token', jwt);
  }

  getUser() {
    return localStorage.getItem('user');
  }

  setUser(user: string) {
    localStorage.setItem('user', user);
  }
}
