import { Component } from '@angular/core';
import {AlertService} from "../services/alert.service";
import {VacuumService} from "../services/vacuum.service";

@Component({
  selector: 'app-vacuum-add',
  templateUrl: './vacuum-add.component.html',
  styleUrls: ['./vacuum-add.component.css']
})
export class VacuumAddComponent {
  name: string

  constructor(private vacuumService: VacuumService, private alertService : AlertService) {
    this.name = "";
  }

  save() {
    this.vacuumService.saveVacuum(this.name).subscribe({
      next: (data) => {
        this.alertService.throwAlert("Successfully created vacuum " + data.name + "!");
      },
      error: (e) => {
        this.alertService.throwAlert("Error while creating vacuum " + this.name + "!");
        console.log(e);
      }
    })
  }
}
