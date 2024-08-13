export interface CreateUserRequest {
  email: string
  password: string
  name: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  id: number
  token: string
}

export interface User {
  id: number
  name: string
  email: string
  superuser: boolean
}
