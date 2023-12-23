import {APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GraphQLModule } from './graphql.module';
import { HttpClientModule } from '@angular/common/http';
import {NavbarComponent} from "./navbar/navbar.component";
import {CurrenciesComponent} from "./currencies/currencies.component";
import {ContinentsComponent} from "./continents/continents.component";
import {WalletsComponent} from "./wallets/wallets.component";
import {WalletTransactionsComponent} from "./wallet-transactions/wallet-transactions.component";
import {CurrencyDepositComponent} from "./currency-deposit/currency-deposit.component";
import {ReactiveFormsModule} from "@angular/forms";
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';

//la securite de Angular-->KeycloakService
export function KcFactory(KcService : KeycloakService){
  return ()=>{
    KcService.init({
      config :{
        realm :"wallet-realm",
        clientId :"wallet-client",
        url :"http://localhost:8080"
      },
      initOptions : {
        //onLoad :"login-required", //des que je demarre Angular, l'authentification est obligatoire, tu n'accedes pas a l'interface
        onLoad :"check-sso",//tu accedes a l'app mais apres c moi qui va proteger les routes, cette parte la necissite une authentification..
        checkLoginIframe: true
      }
    })
  }
}

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CurrenciesComponent,
    ContinentsComponent,
    WalletsComponent,
    WalletTransactionsComponent,
    CurrencyDepositComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    GraphQLModule,
    HttpClientModule,
    ReactiveFormsModule,
    KeycloakAngularModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER, deps :[KeycloakService],useFactory: KcFactory,multi:true

    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
