import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

import {TransactionService} from "../../services/transaction.service";
import {CategoryService} from "../../../categories/services/category.service";
import {AccountService} from "../../../accounts/services/account.service";
import {PaymentAccount} from "../../../accounts/models/accounts.models";
import {Category} from "../../../categories/models/categories.models";
import {Transaction} from "../../models/transactions.models";

import Swal from "sweetalert2";


@Component({
  selector: 'app-transaction-update-view',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './transaction-update-view.component.html',
  styleUrl: './transaction-update-view.component.css'
})
export class TransactionUpdateViewComponent implements OnInit{

  transactionService: TransactionService = inject(TransactionService)
  categoryService: CategoryService = inject(CategoryService)
  accountService: AccountService = inject(AccountService)

  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  route: ActivatedRoute = inject(ActivatedRoute)

  isFormLoading: boolean = false
  isError: boolean = false
  isSuccess: boolean = false

  transaction: Transaction | undefined
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
    const pathParam = this.route.snapshot.paramMap.get('id')
    if (pathParam != null) {
      const id = Number.parseInt(pathParam)
      this.transactionService.getById(id).subscribe({
        next: result => {
          console.log(result)
          this.transaction = result
          this.form.get('description')?.setValue(result.description)
          this.form.get('type')?.setValue(result.type)
          this.form.get('transactionDate')?.setValue(result.transactionDate)
          this.form.get('amount')?.setValue(result.amount)
          this.form.get('categoryId')?.setValue(result.categoryId)
          this.form.get('paymentAccountId')?.setValue(result.paymentAccountId)
        },
        error: (err: HttpErrorResponse) => {
          console.log(err)
          this.isError = true
        }
      })
      this.categoryService.getAll().subscribe({
        next: result => this.categoriesList = result
      })
      this.accountService.getAll().subscribe({
        next: result => this.accountsList = result
      })


    }
  }

  onSubmit() {
    this.isFormLoading = true
    this.isError = false
    this.isSuccess = false

    const body: Transaction = {
      ...this.transaction!,
      description: this.form.get('description')?.value,
      type: this.form.get('type')?.value,
      transactionDate: this.form.get('transactionDate')?.value,
      amount: this.form.get('amount')?.value,
      categoryId: this.form.get('categoryId')?.value,
      paymentAccountId: this.form.get('paymentAccountId')?.value
    }

    this.transactionService.update(body).subscribe({
      next: result => {
        console.log(result)
        this.isSuccess = true
        this.isFormLoading = false
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
