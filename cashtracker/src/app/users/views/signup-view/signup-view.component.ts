import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {RouterLink} from "@angular/router";
import {UserService} from "../../services/user.service";
import {CreateUserRequest} from "../../models/users.models";

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

  isFormLoading: boolean = false
  isSuccess: boolean = false
  isError: boolean = false
  isShowPassword: boolean = false
  isEmailAlreadyUsed: boolean = false

  signupForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(5)]],
    name: ['', [Validators.required]],
  })

  onFormSubmit(){
    this.isFormLoading = true
    this.isError = false
    this.isSuccess = false
    const body: CreateUserRequest = {
      email: this.signupForm.get("email")?.value,
      name: this.signupForm.get("name")?.value,
      password: this.signupForm.get("password")?.value,
    }

    this.userService.createUser(body).subscribe({
      next: result => {
        console.log(result)
        this.signupForm.reset()
        this.isSuccess = true
        this.isFormLoading = false
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        if (err.status == 400){
          this.isEmailAlreadyUsed = true
          this.signupForm.reset()
        } else {
          this.isError = true
        }
        this.isFormLoading = false
      }
    })

  }

}
