import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';

import { OfferClient } from '../../model/offer-client.js';
import { OfferClientService } from '../../service/offer-client.service.js';

@Component({
  selector: 'my-offers',
  templateUrl: './offers-client.component.html'
  // styleUrls: ['./offers.component.css'],
})
export class OffersClientComponent implements OnInit {
  selectedOffer : OfferClient;
  offers : OfferClient[];
  constructor(private router: Router,
              private offerClientService: OfferClientService) { }
  getOffers(): void {
    this.offerClientService.getOffersSlowly().then(offers => this.offers = offers);
  }
  onSelect(offerClient: OfferClient): void {
    this.selectedOffer = offerClient;
  }
  ngOnInit(): void {
    this.getOffers();
  }

  gotoDetail(): void {
    this.router.navigate(['/detail', this.selectedOffer.id]);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.offerClientService.create(name)
        .then(hero => {
          this.offers.push(hero);
          this.selectedOffer = null;
        });
  }

  delete(offerClient: OfferClient): void {
    this.offerClientService
        .delete(offerClient.id)
        .then(() => {
          this.offers = this.offers.filter(h => h !== offerClient);
          if (this.selectedOffer === offerClient) { this.selectedOffer = null; }
        });
  }
}
