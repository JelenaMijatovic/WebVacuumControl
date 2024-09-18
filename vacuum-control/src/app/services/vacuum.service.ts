import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Status, Vacuum} from "../model";

@Injectable({
  providedIn: 'root'
})
export class VacuumService {

  private readonly apiUrl = "http://localhost:8080"

  constructor(private httpClient: HttpClient) { }

  searchVacuums(name:string, status:Array<Status>, to:Date | null, from:Date | null): Observable<Vacuum[]> {
    return this.httpClient.get<Vacuum[]>(`${this.apiUrl}/vacuums/search?name=${name}&status=${status}&datefrom=${to == null ? "": to.toISOString()}&dateto=${from == null ? "": from.toISOString()}`);
  }

  saveVacuum(name: string): Observable<Vacuum> {
    return this.httpClient.post<Vacuum>(`${this.apiUrl}/vacuums`, {
      name: name
    })
  }

  deleteVacuum(id: string): Observable<Vacuum[]> {
    return this.httpClient.delete<Vacuum[]>(`${this.apiUrl}/vacuums/${id}`);
  }

  startVacuum(id: string): Observable<Vacuum> {
    return this.httpClient.get<Vacuum>(`${this.apiUrl}/vacuums/${id}/start`);
  }

  stopVacuum(id: string): Observable<Vacuum> {
    return this.httpClient.get<Vacuum>(`${this.apiUrl}/vacuums/${id}/stop`);
  }

  dischargeVacuum(id: string): Observable<Vacuum> {
    return this.httpClient.get<Vacuum>(`${this.apiUrl}/vacuums/${id}/discharge`);
  }

  schedule(time: Date, action: string, vacuumId: string): Observable<Vacuum> {
    return this.httpClient.post<Vacuum>(`${this.apiUrl}/vacuums/schedule`, {
      time: time.toISOString(),
      action: action,
      vacuumId: vacuumId
    });
  }
}
