export interface SignupRequest {
  email: string
  name: string
  password: string
}

export interface SignupResponse {

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
  email: string
  name: string
}
