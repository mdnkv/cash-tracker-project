import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {AccountService} from "../../services/account.service";
import {PaymentAccount} from "../../models/accounts.models";

import Swal from "sweetalert2";


@Component({
  selector: 'app-account-update-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './account-update-view.component.html',
  styleUrl: './account-update-view.component.css'
})
export class AccountUpdateViewComponent implements OnInit{

  accountService: AccountService = inject(AccountService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  activatedRoute: ActivatedRoute = inject(ActivatedRoute)

  isFormLoading: boolean = false
  isError: boolean = false
  isSuccess: boolean = false

  account: PaymentAccount | undefined

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    accountType: ['Cash', [Validators.required]],
    description: ['']
  })

  ngOnInit() {
    const pathParam = this.activatedRoute.snapshot.paramMap.get('id')
    if (pathParam != null){
      const id = Number.parseInt(pathParam)
      this.accountService.getById(id).subscribe({
        next: (result) => {
          this.account = result
          this.form.get('name')?.setValue(this.account!.name)
          this.form.get('accountType')?.setValue(this.account!.accountType)
          this.form.get('description')?.setValue(this.account!.description)
        },
        error: (err: HttpErrorResponse) => {
          this.isError = true
          console.log(err)
        }
      })
    } else {
      this.isError = true
    }
  }

  onSubmit(){
    this.isFormLoading = true
    this.isError = false

    const body:PaymentAccount = {
      ...this.account!,
      name: this.form.get('name')?.value,
      description: this.form.get('description')?.value,
      accountType: this.form.get('accountType')?.value
    }

    this.accountService.update(body).subscribe({
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
          this.router.navigateByUrl('/accounts')
        }
      })
    } else {
      this.router.navigateByUrl('/accounts')
    }
  }

}
