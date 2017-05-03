import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms'; // <-- NgModel lives here
import { ActivatedRoute, Params, RouterModule }   from '@angular/router';
import { Location }                 from '@angular/common';
import { HttpModule, JsonpModule }    from '@angular/http';

// Imports for loading & configuring the in-memory web api

import { OfferService } from './guest/service/offer.service.js';
import { OfferVitrinComponent } from './guest/component/offer-vitrin/offer-vitrin.component.js';
import { OfferDetailComponent } from './guest/component/offer-detail/offer-detail.component.js';
import { OffersClientComponent } from './guest/component/offers-client/offers-client.component.js';
import { OfferClientService } from './guest/service/offer-client.service.js';
import { ShadbargAppComponent }  from './guest/component/shadbarg-app/shadbarg-app.component.js';
import { AppRoutingModule }     from './app-routing.module.js';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule, // <-- import the FormsModule before binding with [(ngModel)]
        HttpModule,
        JsonpModule,
        AppRoutingModule
    ],
    declarations: [
        OfferVitrinComponent,
        ShadbargAppComponent,
        OffersClientComponent,
        OfferDetailComponent
    ],
    bootstrap: [
        ShadbargAppComponent
    ],
    providers: [
        OfferService,
        OfferClientService
    ]
})
export class AppModule { }
