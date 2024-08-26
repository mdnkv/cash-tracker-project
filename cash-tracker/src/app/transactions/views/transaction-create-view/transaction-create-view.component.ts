import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

import {TransactionService} from "../../services/transaction.service";
import {CategoryService} from "../../../categories/services/category.service";
import {AccountService} from "../../../accounts/services/account.service";
import {PaymentAccount} from "../../../accounts/models/accounts.models";
import {Category} from "../../../categories/models/categories.models";
import {Transaction} from "../../models/transactions.models";

import Swal from "sweetalert2";

@Component({
  selector: 'app-transaction-create-view',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './transaction-create-view.component.html',
  styleUrl: './transaction-create-view.component.css'
})
export class TransactionCreateViewComponent implements OnInit{

  transactionService: TransactionService = inject(TransactionService)
  categoryService: CategoryService = inject(CategoryService)
  accountService: AccountService = inject(AccountService)

  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  isFormLoading: boolean = false
  isError: boolean = false

  accountsList: PaymentAccount[] | undefined
  categoriesList: Category[] | undefined

  form: FormGroup = this.formBuilder.group({
    description: ['', [Validators.required, Validators.maxLength(250)]],
    type: ['Expense', [Validators.required]],
    transactionDate: [null, [Validators.required]],
    amount: [0, [Validators.required, Validators.min(0)]],
    categoryId: [null],
    paymentAccountId: [null]
  })

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: result => {
        this.categoriesList = result
        if (result.length == 0){
          this.form.get('categoryId')?.disable()
        } else {
          this.form.get('categoryId')?.enable()
        }
      },
      error: (err) => {
        console.log(err)
        this.form.get('categoryId')?.disable()
      }
    })
    this.accountService.getAll().subscribe({
      next: result => {
        this.accountsList = result
        if (result.length == 0){
          this.form.get('paymentAccountId')?.disable()
        } else {
          this.form.get('paymentAccountId')?.enable()
        }
      },
      error: (err) => {
        console.log(err)
        this.form.get('paymentAccountId')?.disable()
      }
    })
  }

  onSubmit() {
    this.isFormLoading = true
    this.isError = false

    const ownerId = Number.parseInt(localStorage.getItem('userId') as string)

    const body: Transaction = {
      ownerId: ownerId,
      description: this.form.get('description')?.value,
      type: this.form.get('type')?.value,
      transactionDate: this.form.get('transactionDate')?.value,
      amount: this.form.get('amount')?.value,
      currency: 'EUR',
      categoryId: this.form.get('categoryId')?.value,
      paymentAccountId: this.form.get('paymentAccountId')?.value
    }

    this.transactionService.create(body).subscribe({
      next: result => {
        console.log(result)
        Swal.fire({
          text: 'Transaction was created',
          icon: 'success'
        }).then(r => {
          this.router.navigateByUrl('/transactions')
        })
      },
      error: (err:HttpErrorResponse) => {
        console.log(err)
        this.isError = true
        this.isFormLoading = false
        this.form.reset()
      }
    })
  }

  onCancelClicked(){
    const isTouched = this.form.touched
    if (isTouched){
      Swal.fire({
        text: 'Do you want to quit without saving?',
        icon: 'question',
        showCancelButton: true,
        cancelButtonText: 'No, stay here',
        showConfirmButton: true,
        confirmButtonText: 'Yes, go back'
      }).then(r => {
        if (r.isConfirmed){
          this.router.navigateByUrl('/transactions')
        }
      })
    } else {
      this.router.navigateByUrl('/transactions')
    }
  }

  onCreateCategoryClicked(){
    const isTouched = this.form.touched
    if (isTouched){
      Swal.fire({
        text: 'Do you want to quit without saving?',
        icon: 'question',
        showCancelButton: true,
        cancelButtonText: 'No, stay here',
        showConfirmButton: true,
        confirmButtonText: 'Yes, go and create a category now'
      }).then(r => {
        if (r.isConfirmed){
          this.router.navigateByUrl('/categories/create')
        }
      })
    } else {
      this.router.navigateByUrl('/categories/create')
    }
  }

  onCreatePaymentAccountClicked(){
    const isTouched = this.form.touched
    if (isTouched){
      Swal.fire({
        text: 'Do you want to quit without saving?',
        icon: 'question',
        showCancelButton: true,
        cancelButtonText: 'No, stay here',
        showConfirmButton: true,
        confirmButtonText: 'Yes, go and create a payment account now'
      }).then(r => {
        if (r.isConfirmed){
          this.router.navigateByUrl('/accounts/create')
        }
      })
    } else {
      this.router.navigateByUrl('/accounts/create')
    }
  }


}
