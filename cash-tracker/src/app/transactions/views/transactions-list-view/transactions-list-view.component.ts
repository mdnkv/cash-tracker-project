import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {TransactionService} from "../../services/transaction.service";
import {Transaction} from "../../models/transactions.models";

import Swal from "sweetalert2";

@Component({
  selector: 'app-transactions-list-view',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './transactions-list-view.component.html',
  styleUrl: './transactions-list-view.component.css'
})
export class TransactionsListViewComponent implements OnInit{

  transactionService: TransactionService = inject(TransactionService)
  transactionsList: Transaction[] = []

  router: Router = inject(Router)

  isLoaded: boolean = false
  isError: boolean = false

  ngOnInit() {
    this.transactionService.getAll().subscribe({
      next: result => {
        console.log(result)
        this.transactionsList = result
        this.isLoaded = true
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.isLoaded = true
        this.isError = true
      }
    })
  }

  deleteTransaction(id: number){
    Swal.fire({
      title: 'Delete transaction',
      text: 'Do you want to delete this item? This cannot be undone',
      icon: 'warning',
      showCancelButton: true,
      showConfirmButton: true
    }).then(confirm => {
      if (confirm.isConfirmed) {
        this.transactionService.delete(id).subscribe({
          next: r => {
            this.transactionsList = this.transactionsList.filter(e => e.id != id)
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
          }
        })
      }
    })
  }

}
