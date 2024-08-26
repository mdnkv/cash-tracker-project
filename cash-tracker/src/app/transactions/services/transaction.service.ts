import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

import {NotAuthenticatedError} from "../../core/utils/errors";
import {Transaction} from "../models/transactions.models";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  http: HttpClient = inject(HttpClient)
  apiRoot: string = environment.apiRoot

  getAll(): Observable<Transaction[]>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<Transaction[]>(
      `${this.apiRoot}api/transactions/list`, {headers}
    )
  }

  create(body: Transaction): Observable<Transaction>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.post<Transaction>(
      `${this.apiRoot}api/transactions/create`, body, {headers}
    )
  }

  update(body: Transaction): Observable<Transaction>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.put<Transaction>(
      `${this.apiRoot}api/transactions/update`, body, {headers}
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
      `${this.apiRoot}api/transactions/delete/${id}`, {headers}
    )
  }

  getById(id: number): Observable<Transaction> {
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<Transaction>(
      `${this.apiRoot}api/transactions/one/${id}`, {headers}
    )
  }
}
