import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map, Observable} from "rxjs";

import {environment} from "../../../environments/environment";

import {
  LoginRequest,
  LoginResponse,
  SignupRequest,
  SignupResponse
} from "../models/users.models";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  http: HttpClient = inject(HttpClient)
  apiRoot: String = environment.apiRoot

  isAuthenticated: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false)

  constructor() {
    if (localStorage.getItem('token') != null){
      this.isAuthenticated.next(true)
    } else {
      this.isAuthenticated.next(false)
    }
  }

  signup(body: SignupRequest): Observable<SignupResponse> {
    return this.http.post<SignupResponse>(`${this.apiRoot}api/users/create-user`, body)
  }

  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiRoot}api/users/login`, body)
      .pipe(map(result => {
        localStorage.setItem('token', result.token)
        localStorage.setItem('userId', result.id.toString())
        this.isAuthenticated.next(true)
        return result
      }))
  }

  logout(){
    localStorage.clear()
    this.isAuthenticated.next(false)
  }

}
