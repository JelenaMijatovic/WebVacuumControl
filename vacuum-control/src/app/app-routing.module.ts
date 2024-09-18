import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponentComponent} from "./login-component/login-component.component";
import {UserComponentComponent} from "./user-component/user-component.component";
import {EditUserComponentComponent} from "./edit-user-component/edit-user-component.component";
import {AddUserComponentComponent} from "./add-user-component/add-user-component.component";
import {AuthGuard} from "./auth.guard";
import {VacuumSearchComponent} from "./vacuum-search/vacuum-search.component";
import {VacuumAddComponent} from "./vacuum-add/vacuum-add.component";
import {ErrorsComponent} from "./errors/errors.component";


const routes: Routes = [
  {
    path: "",
    component: UserComponentComponent,
    canActivate: [AuthGuard],
    canDeactivate: [AuthGuard]
  },
  {
    path: "auth/login",
    component: LoginComponentComponent
  },
  {
    path: "users",
    component: UserComponentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "users/edit/:email",
    component: EditUserComponentComponent,
    canActivate: [AuthGuard]
  },
  {
    path:"users/add",
    component: AddUserComponentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "vacuums",
    component: VacuumSearchComponent,
    canActivate: [AuthGuard]
  },
  {
    path:"vacuums/add",
    component: VacuumAddComponent,
    canActivate: [AuthGuard]
  },
  {
    path:"errors",
    component: ErrorsComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
