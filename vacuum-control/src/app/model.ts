export interface User {
  email: string,
  name: string,
  surname: string,
  password: string,
  permissions: Array<Permission>
}

export interface Permission {
  permission: string
}

export interface Vacuum {
  vacuumId: string,
  name: string,
  status: string,
  creationDate: Date
}

export interface Status {
  status: string
}

export interface ErrorMessage {
  date: Date,
  vacuumId: string,
  action: string,
  errorText: string
}
