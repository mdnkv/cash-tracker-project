import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

import {Category} from "../models/categories.models";
import {NotAuthenticatedError} from "../../core/utils/errors";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  http: HttpClient = inject(HttpClient)
  apiRoot: string = environment.apiRoot

  getAll(): Observable<Category[]>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<Category[]>(
      `${this.apiRoot}api/categories/list`, {headers}
    )
  }

  create(body: Category): Observable<Category>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.post<Category>(
      `${this.apiRoot}api/categories/create`, body, {headers}
    )
  }

  update(body: Category): Observable<Category>{
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.put<Category>(
      `${this.apiRoot}api/categories/update`, body, {headers}
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
      `${this.apiRoot}api/categories/delete/${id}`, {headers}
    )
  }

  getById(id: number): Observable<Category> {
    const token = localStorage.getItem('token')
    if (token == null){
      throw new NotAuthenticatedError()
    }
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    return this.http.get<Category>(
      `${this.apiRoot}api/categories/one/${id}`, {headers}
    )
  }


}
