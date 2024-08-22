import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";

import {Category} from "../../models/categories.models";
import {CategoryService} from "../../services/category.service";

import Swal from "sweetalert2";

@Component({
  selector: 'app-category-update-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './category-update-view.component.html',
  styleUrl: './category-update-view.component.css'
})
export class CategoryUpdateViewComponent implements OnInit{

  categoryService: CategoryService = inject(CategoryService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  activatedRoute: ActivatedRoute = inject(ActivatedRoute)

  isFormLoading: boolean = false
  isError: boolean = false
  isSuccess: boolean = false

  category: Category | undefined

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    color: ['', [Validators.required]],
    description: ['']
  })

  ngOnInit() {
    const pathParam = this.activatedRoute.snapshot.paramMap.get('id')
    if (pathParam != null){
      const id = Number.parseInt(pathParam)
      this.categoryService.getById(id).subscribe({
        next: (result) => {
          this.category = result
          this.form.get('name')?.setValue(this.category!.name)
          this.form.get('color')?.setValue(this.category!.color)
          this.form.get('description')?.setValue(this.category!.description)
        },
        error: (err: HttpErrorResponse) => {
          this.isError = true
          console.log(err)
        }
      })
    } else {
      this.isError = true
    }
  }

  onSubmit(){
    this.isFormLoading = true
    this.isError = false

    const body:Category = {
      ...this.category!,
      name: this.form.get('name')?.value,
      description: this.form.get('description')?.value,
      color: this.form.get('color')?.value
    }

    this.categoryService.update(body).subscribe({
      next: result => {
        console.log(result)
        this.isSuccess = true
        this.isFormLoading = false

      },
      error: (err:HttpErrorResponse) => {
        console.log(err)
        this.isError = true
        this.isFormLoading = false
        this.form.reset()
      }
    })

  }

  onCancelClicked(){
    const isTouched = this.form.touched
    if (isTouched){
      Swal.fire({
        text: 'Do you want to quit without saving?',
        icon: 'question',
        showCancelButton: true,
        cancelButtonText: 'No, stay here',
        showConfirmButton: true,
        confirmButtonText: 'Yes, go back'
      }).then(r => {
        if (r.isConfirmed){
          this.router.navigateByUrl('/categories')
        }
      })
    } else {
      this.router.navigateByUrl('/categories')
    }
  }

}
