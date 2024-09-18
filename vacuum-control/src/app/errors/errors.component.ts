import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTable} from "@angular/material/table";
import {ErrorMessage, User} from "../model";
import {Router} from "@angular/router";
import {ErrorService} from "../services/error.service";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-errors',
  templateUrl: './errors.component.html',
  styleUrls: ['./errors.component.css']
})
export class ErrorsComponent implements OnInit {

  @ViewChild(MatTable) table!: MatTable<User>;
  @ViewChild(MatSort) matSort!: MatSort;
  errors: Array<ErrorMessage>
  sortedData: Array<ErrorMessage>
  displayedColumns: Array<string>

  constructor(private router: Router, private errorService: ErrorService) {
    this.errors = [];
    this.sortedData = [];
    this.displayedColumns = ['date', 'vacuumId', 'action', 'errorText'];
  }

  ngOnInit() {
    this.errorService.getErrors().subscribe({
      next: (data) => {
        this.errors.length = 0;
        for (let e of data) {
          this.errors.push(e);
        }
        this.sortedData = data.sort((a, b) => {
          return this.compare(a.date, b.date, true);
        });
        this.table.renderRows();
      },
      error: (e) => {
        console.log(e);
      }
    });
  }

  compare(a: Date, b: Date, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

}
