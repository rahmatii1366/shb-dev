
/**
 * Created by ASUS on 4/4/2017.
 */
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { OfferClient } from '../model/offer-client.js';

@Injectable()
export class OfferClientService {
    private offersUrl = 'http://localhost:9000/offer-clients ';  // URL to web api
    private headers = new Headers({'Content-Type': 'application/json'});

    constructor(private http: Http) { }

    create(name: string): Promise<OfferClient> {
        return this.http
            .post(this.offersUrl, JSON.stringify({name: name}), {headers: this.headers})
            .toPromise()
            .then(res => res.json().data as OfferClient)
            .catch(this.handleError);
    }

    update(offerClient: OfferClient): Promise<OfferClient> {
        const url = `${this.offersUrl}/${offerClient.id}`;
        return this.http
            .put(url, JSON.stringify(offerClient), {headers: this.headers})
            .toPromise()
            .then(() => offerClient)
            .catch(this.handleError);
    }

    delete(id: number): Promise<void> {
        const url = `${this.offersUrl}/${id}`;
        return this.http.delete(url, {headers: this.headers})
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    getOffers(): Promise<OfferClient[]> {
        return this.http.get(this.offersUrl)
            .toPromise()
            .then(response => {
                console.log(response);
                console.log(response.json());
                return response.json() as OfferClient[]
            })
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    getOffersSlowly(): Promise<OfferClient[]> {
        return new Promise(resolve => {
            // Simulate server latency with 2 second delay
            setTimeout(() => resolve(this.getOffers()), 2000);
        });
    }

    getOffer(id: number): Promise<OfferClient> {
        const url = `${this.offersUrl}/${id}`;
        return this.http.get(url)
            .toPromise()
            .then(response => response.json().data as OfferClient)
            .catch(this.handleError);
    }
}