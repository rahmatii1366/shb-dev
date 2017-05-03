/**
 * Created by ASUS on 4/4/2017.
 */
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Offer } from '../model/offer.js';

@Injectable()
export class OfferService {
    private offersUrl = 'http://localhost:8000/offer/all ';  // URL to web api
    private headers = new Headers({'Content-Type': 'application/json'});

    constructor(private http: Http) {
    }

    create(name: string): Promise<Offer> {
        return this.http
            .post(
                this.offersUrl,
                JSON.stringify({name: name}),
                {headers: this.headers})
            .toPromise()
            .then(res => res.json().data as Offer)
            .catch(this.handleError);
    }

    update(offer: Offer): Promise<Offer> {
        const url = `${this.offersUrl}/${offer.id}`;
        return this.http
            .put(url, JSON.stringify(offer), {headers: this.headers})
            .toPromise()
            .then(() => offer)
            .catch(this.handleError);
    }

    delete(id: number): Promise<void> {
        const url = `${this.offersUrl}/${id}`;
        return this.http.delete(url, {headers: this.headers})
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    getOffers(): Promise<Offer[]> {
        return this.http.get(this.offersUrl, {withCredentials: true})
            .toPromise()
            .then(response => {
                console.log(response);
                console.log(response.json());
                return response.json() as Offer[]
            })
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }

    getOffer(id: number): Promise<Offer> {
        const url = `${this.offersUrl}/${id}`;
        return this.http.get(url)
            .toPromise()
            .then(response => response.json().data as Offer)
            .catch(this.handleError);
    }
}