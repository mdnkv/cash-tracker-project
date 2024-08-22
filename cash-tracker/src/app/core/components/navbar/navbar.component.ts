import {Component, inject} from '@angular/core';
import {Router, RouterLink} from "@angular/router";

import {UserService} from "../../../users/services/user.service";

import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  userService: UserService = inject(UserService)
  router: Router = inject(Router)

  isAuthenticated: boolean = false
  isShowMenu: boolean = false

  constructor() {
    this.userService.isAuthenticated.subscribe({
      next: result => this.isAuthenticated = result
    })
  }

  toggleMenu(){
    this.isShowMenu = !this.isShowMenu
  }

  logout(){
    Swal.fire({
      icon: 'question',
      text: 'Do you want to logout?',
      showConfirmButton: true,
      confirmButtonText: 'Log out',
      showCancelButton: true,
      cancelButtonText: 'Stay logged in'
    }).then(result => {
      if (result.isConfirmed) {
        this.userService.logout()
        this.router.navigateByUrl('/users/login')
      }
    })
  }

}
