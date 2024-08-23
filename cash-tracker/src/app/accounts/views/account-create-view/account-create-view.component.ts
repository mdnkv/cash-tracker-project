import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {AccountService} from "../../services/account.service";
import {PaymentAccount} from "../../models/accounts.models";

import Swal from "sweetalert2";


@Component({
  selector: 'app-account-create-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './account-create-view.component.html',
  styleUrl: './account-create-view.component.css'
})
export class AccountCreateViewComponent{

  accountService: AccountService = inject(AccountService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  isFormLoading: boolean = false
  isError: boolean = false

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    accountType: ['Cash', [Validators.required]],
    description: ['', [Validators.maxLength(250)]]
  })


  onSubmit(){
    this.isFormLoading = true
    this.isError = false

    const ownerId = Number.parseInt(localStorage.getItem('userId') as string)

    const body:PaymentAccount = {
      name: this.form.get('name')?.value,
      ownerId: ownerId,
      description: this.form.get('description')?.value,
      accountType: this.form.get('accountType')?.value
    }

    this.accountService.create(body).subscribe({
      next: result => {
        console.log(result)
        Swal.fire({
          text: 'Account was created',
          icon: 'success'
        }).then(r => {
          this.router.navigateByUrl('/accounts')
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
          this.router.navigateByUrl('/accounts')
        }
      })
    } else {
      this.router.navigateByUrl('/accounts')
    }
  }

}
