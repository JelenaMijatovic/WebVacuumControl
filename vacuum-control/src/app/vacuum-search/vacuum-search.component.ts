import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTable} from "@angular/material/table";
import {Status, User, Vacuum} from "../model";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";
import {VacuumService} from "../services/vacuum.service";
import {AlertService} from "../services/alert.service";
import {MatDatepicker, MatDatepickerInputEvent} from "@angular/material/datepicker";

@Component({
  selector: 'app-vacuum-search',
  templateUrl: './vacuum-search.component.html',
  styleUrls: ['./vacuum-search.component.css']
})
export class VacuumSearchComponent implements OnInit{

  canSearch: boolean
  canRemove: boolean
  canStop: boolean
  canStart: boolean
  canDischarge: boolean
  permissions: Array<string>
  @ViewChild(MatTable) table!: MatTable<User>;
  vacuums: Array<Vacuum>
  displayedColumns: Array<string>
  name: string
  status: Array<Status>
  from: Date | null
  to: Date | null
  @ViewChild(MatDatepicker) fromDP!: MatDatepicker<any>
  @ViewChild(MatDatepicker) toDP!: MatDatepicker<any>
  action: string
  vacuum: string
  time: Date
  @ViewChild(MatDatepicker) scheduleDP!: MatDatepicker<any>
  minDate: Date

  constructor(private router: Router, private authService: AuthService, private vacuumService: VacuumService, private alertService : AlertService) {
    this.canSearch = false;
    this.canRemove = false;
    this.canStart = false;
    this.canStop = false;
    this.canDischarge = false;
    this.permissions = [];
    this.vacuums = [];
    this.displayedColumns = ['name', 'status', 'creationDate', 'remove', 'start', 'stop', 'discharge'];
    this.name = "";
    this.status = [];
    this.action = "";
    this.vacuum = "";
    this.from = null;
    this.to = null;
    this.time = new Date();
    this.minDate = new Date();
  }

  ngOnInit(): void {
    let user = this.authService.getUser();
    if (user) {
      let userJ = JSON.parse(user);
      for (let perm of userJ.permissions) {
        this.permissions.push(perm.permission);
      }
      //console.log(this.permissions);
      this.canSearch = this.permissions.includes("can_search_vacuum");
      this.canRemove = this.permissions.includes("can_remove_vacuum");
      this.canStart = this.permissions.includes("can_start_vacuum");
      this.canStop = this.permissions.includes("can_stop_vacuum");
      this.canDischarge = this.permissions.includes("can_discharge_vacuum")
    }
    if (this.canSearch) {
      this.updateTable();
    } else {
      this.alertService.throwAlert("You do not have permission to view vacuums!");
    }
  }

  updateTable() {
    this.vacuumService.searchVacuums(this.name, this.status, this.from, this.to).subscribe({
      next: (data) => {
        this.vacuums.length = 0;
        for (let v of data) {
          this.vacuums.push(v);
        }
        this.table.renderRows();
      },
      error: () => {
        console.log("You do not have permission to view vacuums");
      }
    });
  }

  delete(id:string) {
    this.vacuumService.deleteVacuum(id).subscribe({
      next: () => {
        this.updateTable();
        this.alertService.throwAlert("Removing vacuum " + id + "...")
      },
      error: (e) => {
        console.log(e);
      }
    })
  }

  start(id:string) {
    this.vacuumService.startVacuum(id).subscribe({
      next: () => {
        this.alertService.throwAlert("Starting vacuum " + id + "...")
      },
      error: (e) => {
        console.log(e);
      }
    })
  }

  stop(id:string) {
    this.vacuumService.stopVacuum(id).subscribe({
      next: () => {
        this.alertService.throwAlert("Stopping vacuum " + id + "...")
      },
      error: (e) => {
        console.log(e);
      }
    })
  }

  discharge(id:string) {
    this.vacuumService.dischargeVacuum(id).subscribe({
      next: () => {
        this.alertService.throwAlert("Discharging vacuum " + id + "...")
      },
      error: (e) => {
        console.log(e);
      }
    })
  }

  setDate(type: string, event: MatDatepickerInputEvent<Date>) {
    console.log(event.value);
    if (type == "time") {
      if (event.value) {
        this.time = event.value;
      }
    } else if (type == "to") {
      this.to = event.value;
    } else if (type == "from") {
      this.from = event.value;
    }
  }

  schedule() {
    this.vacuumService.schedule(this.time, this.action, this.vacuum).subscribe()
  }
}
