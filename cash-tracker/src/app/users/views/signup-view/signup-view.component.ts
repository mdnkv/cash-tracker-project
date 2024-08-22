import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {UserService} from "../../services/user.service";
import {SignupRequest} from "../../models/users.models";

@Component({
  selector: 'app-signup-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup-view.component.html',
  styleUrl: './signup-view.component.css'
})
export class SignupViewComponent {

  userService: UserService = inject(UserService)
  formBuilder: FormBuilder = inject(FormBuilder)

  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    name: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  })

  isFormLoading: boolean = false
  isError: boolean = false
  isUserExist: boolean = false
  isSuccess: boolean = false

  isShowPassword: boolean = false

  togglePassword(){
    this.isShowPassword = !this.isShowPassword
  }

  onSubmit(){
    this.isFormLoading = true
    this.isError = false
    this.isUserExist = false
    this.isSuccess = false

    // prepare payload
    const payload: SignupRequest = {
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value,
      name: this.form.get('name')?.value,
    }

    // execute request
    this.userService.signup(payload).subscribe({
      next: (result) => {
        console.log(result)
        this.form.reset()
        this.isSuccess = true
        this.isFormLoading = false
      },
      error: (err: HttpErrorResponse) => {
        // check error type
        if (err.status == 400) {
          // user email is already registered
          this.form.reset()
          this.isUserExist = true
        } else {
          // any other generic error
          this.isError = true
        }
        this.isFormLoading = false
      }
    })
  }

}
