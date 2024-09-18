import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private alert: MatSnackBar) { }

  throwAlert(message: string, action?: string) {
    this.alert.open(message, action, {duration: 3000});
  }
}
