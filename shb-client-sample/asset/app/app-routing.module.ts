/**
 * Created by Rahmati on 4/5/2017.
 */
import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OfferVitrinComponent }      from './guest/component/offer-vitrin/offer-vitrin.component.js';
import { OffersClientComponent }      from './guest/component/offers-client/offers-client.component.js';
import { OfferDetailComponent }  from './guest/component/offer-detail/offer-detail.component.js';

const routes: Routes = [
    { path: '', redirectTo: '/offer-vitrin', pathMatch: 'full' },
    { path: 'detail/:id', component: OfferDetailComponent },
    { path: 'offers',     component: OffersClientComponent },
    { path: 'offer-vitrin',     component: OfferVitrinComponent }
];
@NgModule({
    imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}