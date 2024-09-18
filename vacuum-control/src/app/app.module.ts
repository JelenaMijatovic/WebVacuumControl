import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponentComponent } from './login-component/login-component.component';
import { UserComponentComponent } from './user-component/user-component.component';
import { AddUserComponentComponent } from './add-user-component/add-user-component.component';
import { EditUserComponentComponent } from './edit-user-component/edit-user-component.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from "./auth.interceptor";
import {MatTableModule} from "@angular/material/table";
import {PasswordPipe} from "./pipes/password.pipe";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { VacuumSearchComponent } from './vacuum-search/vacuum-search.component';
import { VacuumAddComponent } from './vacuum-add/vacuum-add.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import { ErrorsComponent } from './errors/errors.component';
import {MatSortModule} from "@angular/material/sort";
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponentComponent,
    UserComponentComponent,
    AddUserComponentComponent,
    EditUserComponentComponent,
    PasswordPipe,
    VacuumSearchComponent,
    VacuumAddComponent,
    ErrorsComponent,
    HeaderComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        MatTableModule,
        MatSnackBarModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatSortModule
    ],
  providers: [
    {
      provide : HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi   : true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
