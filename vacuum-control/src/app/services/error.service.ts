import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ErrorMessage} from "../model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  private readonly apiUrl = "http://localhost:8080"

  constructor(private httpClient: HttpClient) { }

  getErrors(): Observable<ErrorMessage[]> {
    return this.httpClient.get<ErrorMessage[]>(`${this.apiUrl}/errors`);
  }
}
