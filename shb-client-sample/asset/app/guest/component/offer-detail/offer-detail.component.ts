/**
 * Created by ASUS on 4/4/2017.
 */
import { Component, Input, OnInit  } from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';
import { Location }                 from '@angular/common';
import 'rxjs/add/operator/switchMap';

import { OfferClient } from '../../model/offer-client.js';
import { OfferClientService } from '../../service/offer-client.service.js';

@Component({
    selector: 'offer-detail',
    templateUrl: './offer-detail.component.html',
    // styleUrls: ['./offer-detail.component.css']
})
export class OfferDetailComponent implements OnInit {
    constructor(
        private offerClientService: OfferClientService,
        private route: ActivatedRoute,
        private location: Location
    ) {}

    @Input() offerClient: OfferClient;

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.offerClientService.getOffer(+params['id']))
            .subscribe(offerClient => this.offerClient = offerClient);
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.offerClientService.update(this.offerClient)
            .then(() => this.goBack());
    }
}