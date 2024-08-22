import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {UserService} from "../../services/user.service";
import {LoginRequest} from "../../models/users.models";

@Component({
  selector: 'app-login-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login-view.component.html',
  styleUrl: './login-view.component.css'
})
export class LoginViewComponent {

  userService: UserService = inject(UserService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  })

  isFormLoading: boolean = false
  isError: boolean = false

  isShowPassword: boolean = false

  togglePassword(){
    this.isShowPassword = !this.isShowPassword
  }

  onSubmit(){
    this.isFormLoading = true
    this.isError = false

    // prepare payload
    const payload: LoginRequest = {
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value,
    }

    // execute request
    this.userService.login(payload).subscribe({
      next: (result) => {
        // go to dashboard
        this.router.navigateByUrl('/dashboard')
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.isError = true
        this.isFormLoading = false
      }
    })
  }

}
