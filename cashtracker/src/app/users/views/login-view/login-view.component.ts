import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {UserService} from "../../services/user.service";
import {LoginRequest} from "../../models/users.models";
import {HttpErrorResponse} from "@angular/common/http";

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

  isFormLoading: boolean = false
  isError: boolean = false
  isShowPassword: boolean = false
  areBadCredentials: boolean = false

  loginForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(5)]]
  })

  onFormSubmit(){
    this.isFormLoading = true
    this.areBadCredentials = false
    this.isError = false

    const body: LoginRequest = {
      email: this.loginForm.get('email')?.value,
      password: this.loginForm.get('password')?.value
    }

    this.userService.login(body).subscribe({
      next: result => {
        this.isFormLoading = false
        this.loginForm.reset()
        this.router.navigateByUrl("/users/current")
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        if (err.status == 400){
          this.loginForm.reset()
          this.areBadCredentials = true
        } else {
          this.isError = true
        }

        this.isFormLoading = false
      }
    })
  }

}
