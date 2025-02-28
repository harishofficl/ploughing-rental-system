import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private url = 'http://localhost:8080/v1';

  constructor(private http: HttpClient) {}

  // Success alert
  private showSuccessMessage(message: string) {
    Swal.fire({
      icon: 'success',
      title: 'Success',
      text: message,
    });
  }

  // Error alert
  private showErrorMessage(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
    });
  }

  // POST /api/drivers
  postDriver(driverJson: any) {
    const url = `${this.url}/api/drivers`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, driverJson, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while submitting driver:', error);
          this.showErrorMessage(
            'Failed to submit the driver. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Driver submitted successfully!');
      });
  }

  // POST /api/equipment
  postEquipment(equipmentJson: any) {
    const url = `${this.url}/api/equipments`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, equipmentJson, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while submitting equipment:', error);
          this.showErrorMessage(
            'Failed to submit the equipment. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Equipment submitted successfully!');
      });
  }

  // GET /api/drivers
  getDriversByOwnerId(ownerId: string): Observable<any> {
    const url = `${this.url}/api/owners/${ownerId}/drivers`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching drivers:', error);
        this.showErrorMessage('Failed to fetch drivers. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/equipment/owner/{ownerId}
  getEquipmentsByOwnerId(ownerId: string): Observable<any> {
    const url = `${this.url}/api/owners/${ownerId}/equipments`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching equipments:', error);
        this.showErrorMessage('Failed to fetch equipments. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/equipments/{id}/
  getEquipmentById(equipmentId: string): Observable<any> {
    const url = `${this.url}/api/equipments/${equipmentId}`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching equipment rate:', error);
        this.showErrorMessage(
          'Failed to fetch equipment rate. Please try again.'
        );
        throw error;
      })
    );
  }

  // POST /api/rental-records
  postRentalRecord(rentalData: any) {
    const url = `${this.url}/api/rental-records`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, rentalData, { headers })
      .pipe(
        catchError((error) => {
          console.error(
            'Error occurred while submitting rental record:',
            error
          );
          this.showErrorMessage(
            'Failed to submit the rental record. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Rental record submitted successfully!');
      });
  }

  // GET /api/customers
  searchCustomersByOwnerId(ownerId: string, term: string): Observable<any> {
    const url = `${this.url}/api/customers/owner/${ownerId}?search=${term}`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while searching customers:', error);
        this.showErrorMessage('Failed to search customers. Please try again.');
        throw error;
      })  
    );
  }
}