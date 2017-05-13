/**
 * Created by ASUS on 4/7/2017.
 */
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';

import { Offer } from '../../model/offer.js';
import { OfferService } from '../../service/offer.service.js';

@Component({
    selector: 'offer-vitrin',
    templateUrl: 'asset/app/guest/component/offer-vitrin/offer-vitrin.component.html'
    // styleUrls: ['./offers.component.css'],
})
export class OfferVitrinComponent implements OnInit {
    selectedOffer : Offer;
    offers : Offer[];
    constructor(private router: Router, private offerService: OfferService) { }

    ngOnInit(): void {
        this.getOffers();
    }

    getOffers(): void {
        this.offerService.getOffers().then(offers => this.offers = offers);
    }

    onSelect(offer: Offer): void {
        this.selectedOffer = offer;
    }

    gotoDetail(): void {
        this.router.navigate(['/detail', this.selectedOffer.id]);
    }

    add(name: string): void {
        name = name.trim();
        if (!name) { return; }
        this.offerService.create(name)
            .then(hero => {
                this.offers.push(hero);
                this.selectedOffer = null;
            });
    }

    delete(offer: Offer): void {
        this.offerService
            .delete(offer.id)
            .then(() => {
                this.offers = this.offers.filter(h => h !== offer);
                if (this.selectedOffer === offer) { this.selectedOffer = null; }
            });
    }
}
