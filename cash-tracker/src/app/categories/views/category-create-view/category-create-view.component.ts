import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

import {Category} from "../../models/categories.models";
import {CategoryService} from "../../services/category.service";

import Swal from "sweetalert2";

@Component({
  selector: 'app-category-create-view',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './category-create-view.component.html',
  styleUrl: './category-create-view.component.css'
})
export class CategoryCreateViewComponent implements OnInit{

  colors: string[] = [
    '#1ABC9C',
    '#3498DB',
    '#9B59B6',
    '#E67E22',
    '#F1C40F',
    '#E74C3C',
    '#7F8C8D',
    '#D35400'
  ]

  categoryService: CategoryService = inject(CategoryService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  isFormLoading: boolean = false
  isError: boolean = false

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    color: ['', [Validators.required]],
    description: ['']
  })

  ngOnInit() {
    const randomColor = this.colors[(Math.floor(Math.random() * this.colors.length))]
    this.form.get('color')?.setValue(randomColor)
  }

  onSubmit(){
    this.isFormLoading = true
    this.isError = false

    const ownerId = Number.parseInt(localStorage.getItem('userId') as string)

    const body:Category = {
      name: this.form.get('name')?.value,
      ownerId: ownerId,
      description: this.form.get('description')?.value,
      color: this.form.get('color')?.value
    }

    this.categoryService.create(body).subscribe({
      next: result => {
        console.log(result)
        Swal.fire({
          text: 'Category was created',
          icon: 'success'
        }).then(r => {
          this.router.navigateByUrl('/categories')
        })
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
