import { Component } from '@angular/core';

@Component({
  selector: 'shadbarg-app',
  template: `
    <h1>{{title}}</h1>
    <nav>
     <a routerLink="/offers" routerLinkActive="">Offers</a>
     <a routerLink="/offer-vitrin" routerLinkActive="active">OfferVitrin</a>
   </nav>
   <router-outlet></router-outlet>
    `
})
export class ShadbargAppComponent {
  title = 'Tour of Offers';
}
