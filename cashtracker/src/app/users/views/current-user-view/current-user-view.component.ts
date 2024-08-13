import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {UserService} from "../../services/user.service";
import {User} from "../../models/users.models";

@Component({
  selector: 'app-current-user-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './current-user-view.component.html',
  styleUrl: './current-user-view.component.css'
})
export class CurrentUserViewComponent implements OnInit{

  userService: UserService = inject(UserService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  user: User | undefined

  isSuccess: boolean = false
  isError: boolean = false
  isFormLoading: boolean = false

  userForm: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]]
  })

  ngOnInit() {
    try {
      this.userService.getCurrentUser().subscribe({
        next: result => {
          this.user = result

          this.userForm.get('name')?.setValue(result.name)
          this.userForm.get('email')?.setValue(result.email)
        },
        error: (err: HttpErrorResponse) => {
          this.isError = true
        }
      })
    } catch (err){
      const router: Router = inject(Router)
      router.navigateByUrl('/users/login')
    }
  }

  onFormSubmit(){
    this.isFormLoading = true
    this.isError = false
    this.isSuccess = false
    const body: User = {
      ...this.user!,
      name: this.userForm.get('name')?.value,
      email: this.userForm.get('email')?.value
    }

    try {
      this.userService.updateCurrentUser(body).subscribe({
        next: result => {
          this.user = result
          this.isSuccess = true
          this.isFormLoading = false
        },
        error: (err: HttpErrorResponse) => {
          console.log(err)
          this.isFormLoading = false
          this.isError = true
        }
      })
    } catch (err){
      const router: Router = inject(Router)
      router.navigateByUrl('/users/login')
    }
  }

  doLogout() {
    if (confirm("Do you want to logout?")){
      this.userService.logout()
      this.router.navigateByUrl("/users/login")
    }
  }

}
