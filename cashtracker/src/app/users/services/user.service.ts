import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map, Observable} from "rxjs";

import {CreateUserRequest, LoginRequest, LoginResponse, User} from "../models/users.models";
import {AuthenticationException} from "../errors/users.errors";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  http: HttpClient = inject(HttpClient)
  root: string = environment.baseApi

  createUser(body: CreateUserRequest): Observable<User>{
    return this.http.post<User>(`${this.root}api/users/create-user`, body)
  }

  login (body: LoginRequest): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(`${this.root}api/users/login`, body)
      .pipe(map(result => {
        localStorage.setItem('userId', result.id.toString())
        localStorage.setItem('token', result.token)
        return result
      }))
  }

  getCurrentUser (): Observable<User> {
    const token = localStorage.getItem('token')
    if (token == null){
      throw new AuthenticationException()
    }
    const headers: HttpHeaders = new HttpHeaders({'Authorization': `Bearer ${token}`})
    return this.http.get<User>(`${this.root}api/users/current-user`, {headers})
  }

  updateCurrentUser (body: User): Observable<User> {
    const token = localStorage.getItem('token')
    if (token == null){
      throw new AuthenticationException()
    }
    const headers: HttpHeaders = new HttpHeaders({'Authorization': `Bearer ${token}`})
    return this.http.put<User>(`${this.root}api/users/current-user`, body,{headers})
  }

  logout(){
    localStorage.clear()
  }

}
