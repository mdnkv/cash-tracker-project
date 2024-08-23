import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {AccountService} from "../../services/account.service";
import {PaymentAccount} from "../../models/accounts.models";

import Swal from "sweetalert2";


@Component({
  selector: 'app-accounts-list-view',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './accounts-list-view.component.html',
  styleUrl: './accounts-list-view.component.css'
})
export class AccountsListViewComponent implements OnInit{

  accountService: AccountService = inject(AccountService)
  router: Router = inject(Router)

  isLoaded: boolean = false
  isError: boolean = false

  accountsList: PaymentAccount[] = []

  ngOnInit() {
    this.accountService.getAll().subscribe({
      next: result => {
        this.accountsList = result
        this.isLoaded = true
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.isLoaded = true
        this.isError = true
      }
    })
  }

  deleteAccount(id: number){
    Swal.fire({
      title: 'Delete account',
      text: 'Do you want to delete this item? This cannot be undone',
      icon: 'warning',
      showCancelButton: true,
      showConfirmButton: true
    }).then(confirm => {
      if (confirm.isConfirmed) {
        this.accountService.delete(id).subscribe({
          next: r => {
            this.accountsList = this.accountsList.filter(e => e.id != id)
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
          }
        })
      }
    })
  }
}
