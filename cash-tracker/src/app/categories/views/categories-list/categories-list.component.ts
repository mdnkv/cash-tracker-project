import {Component, inject, OnInit} from '@angular/core';

import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {CategoryService} from "../../services/category.service";
import {Category} from "../../models/categories.models";

import Swal from "sweetalert2";

@Component({
  selector: 'app-categories-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './categories-list.component.html',
  styleUrl: './categories-list.component.css'
})
export class CategoriesListComponent implements OnInit{

  categoryService: CategoryService = inject(CategoryService)
  router: Router = inject(Router)

  isLoaded: boolean = false
  isError: boolean = false

  categoriesList: Category[] = []

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: result => {
        this.categoriesList = result
        this.isLoaded = true
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.isLoaded = true
        this.isError = true
      }
    })
  }

  deleteCategory(id: number){
    Swal.fire({
      title: 'Delete category',
      text: 'Do you want to delete this item? This cannot be undone',
      icon: 'warning',
      showCancelButton: true,
      showConfirmButton: true
    }).then(confirm => {
      if (confirm.isConfirmed) {
        this.categoryService.delete(id).subscribe({
          next: r => {
            this.categoriesList = this.categoriesList.filter(e => e.id != id)
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
          }
        })
      }
    })
  }


}
