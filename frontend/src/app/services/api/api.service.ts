import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { PaymentService } from '../payment/payment.service';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private url = 'http://localhost:8080/v1';

  constructor(private http: HttpClient, private paymentService: PaymentService) {}

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

  // Warning alert
  private showWarningMessage(message: string) {
    Swal.fire({
      icon: 'warning',
      title: 'Warning',
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

  // POST /api/customers
  postCustomer(customerJson: any) {
    const url = `${this.url}/api/customers`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, customerJson, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while submitting customer:', error);
          this.showErrorMessage(
            'Failed to submit the customer. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Customer submitted successfully!');
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

  // POST api/vehicles
  postVehicle(vehicleJson: any) {
    const url = `${this.url}/api/vehicles`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, vehicleJson, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while submitting vehicle:', error);
          this.showErrorMessage(
            'Failed to submit the vehicle. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Vehicle submitted successfully!');
      });
  }

  // POST /api/gps
  postGpsLocation(gpsObject: any) {
    const url = `${this.url}/api/gps`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, gpsObject, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while submitting GPS location:', error);
          this.showErrorMessage(
            'Failed to submit the GPS location. Please try again.'
          );
          throw error;
        })
      )
      .subscribe(() => {
        console.log('GPS location posted successfully!');
      });
  }

  // POST /api/bills
  postBill(billData: any, selectedCustomer: any, paid: boolean) {
    const url = `${this.url}/api/bills`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .post(url, billData, { headers })
      .pipe(
        catchError((error) => {
          this.showErrorMessage('Failed to submit the bill record. Please try again.');
          throw error;
        })
      )
      .subscribe((response: any) => {
        console.log('Bill created ✔️');
        this.showSuccessMessage(response.message);
        // Call payment service to create payment link
        if(!paid){
          this.paymentService.createPaymentLink(selectedCustomer.email, billData.totalAmount, billData.allMethods, response.data.id).subscribe(() => {
            console.log('Payment link created ✔️');
          });
        }
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

  // PUT /api/vehicles/{vehicleId}/add-fuel?addFuel={fuelAmount}
  addFuel(fuelData: any) {
    const url = `${this.url}/api/vehicles/${fuelData.vehicleId}/add-fuel?addFuel=${fuelData.fuelAmount}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http
      .put(url, null, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while adding fuel:', error);
          this.showErrorMessage('Failed to add fuel. Please try again.');
          throw error;
        })
      )
      .subscribe(() => {
        this.showSuccessMessage('Fuel added successfully!');
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

  // GET /api/driver/{driverId}
  getDriverById(driverId: string): Observable<any> {
    const url = `${this.url}/api/drivers/${driverId}`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching driver:', error);
        this.showErrorMessage('Failed to fetch driver. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/vehicles/owner/{ownerId}
  getVehiclesByOwnerId(ownerId: string): Observable<any> {
    const url = `${this.url}/api/vehicles/owner/${ownerId}`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching vehicles:', error);
        this.showErrorMessage('Failed to fetch vehicles. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/rental-records/owner/{ownerId}/total-outstanding-amount
  getOutstandingBillAmount(ownerId: string): Observable<any> {
    const url = `${this.url}/api/rental-records/owner/${ownerId}/total-outstanding-amount`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error(
          'Error occurred while fetching outstanding bill amount:',
          error
        );
        this.showErrorMessage(
          'Failed to fetch outstanding bill amount. Please try again.'
        );
        throw error;
      })
    );
  }

  // GET /api/rental-records/owner/{ownerId}/customers-count
  getCustomersCount(ownerId: string): Observable<any> {
    const url = `${this.url}/api/customers/owner/${ownerId}/customers-count`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching customers count:', error);
        this.showErrorMessage(
          'Failed to fetch customers count. Please try again.'
        );
        throw error;
      })
    );
  }

  // getUnpaidRecordsByCustomerId -> GET /api/rental-records/customer/{customerId}/unpaid-records
  getUnpaidRecordsByCustomerId(customerId: string): Observable<any>{
    const url = `${this.url}/api/rental-records/customer/${customerId}/unpaid-records`;
    const response = this.http.get(url);
    response.subscribe((data: any) => {
      if(data.success === false){
        this.showWarningMessage(data.message);
        return null;
      }
      return response;
    });
    return response;
  }

  // getPricingRules -> GET /api/owner/{ownerId}/distance-pricing-rules
  getPricingRules(ownerId: string): Observable<any> {
    const url = `${this.url}/api/owners/${ownerId}/distance-pricing-rules`;
    return this.http.get(url).pipe(
      catchError((error) => {
        console.error('Error occurred while fetching pricing rules:', error);
        this.showErrorMessage('Failed to fetch pricing rules. Please try again.');
        throw error;
      })
    );
  }

  // setPricingRules -> PUT /api/owner/{ownerId)/distance-pricing-rules
  setPricingRules(pricingRules: any[], ownerId: string): Observable<any> {
    const url = `${this.url}/api/owners/${ownerId}/distance-pricing-rules`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http
      .put(url, pricingRules, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error occurred while setting pricing rules:', error);
          this.showErrorMessage('Failed to set pricing rules. Please try again.');
          throw error;
        })
      );
  }

  // GET /api/rental-records/owner/{ownerId}/paginated?page={page}&size={size}&search={searchTerm}
  getRentalRecordsByOwnerIdPaginated(ownerId: string, page: number, size: number, searchTerm: string): Observable<any> {
    const url = `${this.url}/api/rental-records/owner/${ownerId}/paginated?page=${page}&size=${size}&search=${searchTerm}`;
    return this.http.get(url)
    .pipe(
      catchError((error) => {
        console.error('Error occurred while fetching rental records:', error);
        this.showErrorMessage('Failed to fetch rental records. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/customers/owner/{ownerId}/paginated?page={page}&size={size}&search={searchTerm}
  getCustomersByOwnerIdPaginated(ownerId: string, page: number, size: number, searchTerm: string): Observable<any> {
    const url = `${this.url}/api/customers/owner/${ownerId}/paginated?page=${page}&size=${size}&search=${searchTerm}`;
    return this.http.get(url)
    .pipe(
      catchError((error) => {
        console.error('Error occurred while fetching customers:', error);
        this.showErrorMessage('Failed to fetch customers. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/drivers/owner/{ownerId}/paginated?page={page}&size={size}&search={searchTerm}
  getDriversByOwnerIdPaginated(ownerId: string, page: number, size: number, searchTerm: string): Observable<any> {
    const url = `${this.url}/api/drivers/owner/${ownerId}/paginated?page=${page}&size=${size}&search=${searchTerm}`;
    return this.http.get(url)
    .pipe(
      catchError((error) => {
        console.error('Error occurred while fetching drivers:', error);
        this.showErrorMessage('Failed to fetch drivers. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/equipments/owner/{ownerId}/paginated?page={page}&size={size}&search={searchTerm}
  getEquipmentsByOwnerIdPaginated(ownerId: string, page: number, size: number, searchTerm: string): Observable<any> {
    const url = `${this.url}/api/equipments/owner/${ownerId}/paginated?page=${page}&size=${size}&search=${searchTerm}`;
    return this.http.get(url)
    .pipe(
      catchError((error) => {
        console.error('Error occurred while fetching equipments:', error);
        this.showErrorMessage('Failed to fetch equipments. Please try again.');
        throw error;
      })
    );
  }

  // GET /api/vehicles/owner/{ownerId}/paginated?page={page}&size={size}&search={searchTerm}
  getVehiclesByOwnerIdPaginated(ownerId: string, page: number, size: number, searchTerm: string): Observable<any> {
    const url = `${this.url}/api/vehicles/owner/${ownerId}/paginated?page=${page}&size=${size}&search=${searchTerm}`;
    return this.http.get(url)
    .pipe(
      catchError((error) => {
        console.error('Error occurred while fetching vehicles:', error);
        this.showErrorMessage('Failed to fetch vehicles. Please try again.');
        throw error;
      })
    );
  }



}
