import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

import {NotAuthenticatedError} from "../../core/utils/errors";
import {PaymentAccount} from "../models/accounts.models";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  http: HttpClient = inject(HttpClient)
  apiRoot: string = environment.apiRoot

  getAll(): Observable<PaymentAccount[]>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<PaymentAccount[]>(
      `${this.apiRoot}api/accounts/list`, {headers}
    )
  }

  create(body: PaymentAccount): Observable<PaymentAccount>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.post<PaymentAccount>(
      `${this.apiRoot}api/accounts/create`, body, {headers}
    )
  }

  update(body: PaymentAccount): Observable<PaymentAccount>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.put<PaymentAccount>(
      `${this.apiRoot}api/accounts/update`, body, {headers}
    )
  }

  delete(id: number): Observable<void>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.delete<void>(
      `${this.apiRoot}api/accounts/delete/${id}`, {headers}
    )
  }

  getById(id: number): Observable<PaymentAccount> {
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<PaymentAccount>(
      `${this.apiRoot}api/accounts/one/${id}`, {headers}
    )
  }

}
