import { Injectable } from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "./confirmation-dialog.component";

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(private dialog:MatDialog) { }

  onConfirmDialog(message: string){
    return this.dialog.open(ConfirmationDialogComponent, {
      width: '390px',
      height: '100px',
      panelClass: 'confirm-dialog-container',
      disableClose: true,
      position: {top: "10px"},
      data: {
        message: message
      }
    })
  }
}
