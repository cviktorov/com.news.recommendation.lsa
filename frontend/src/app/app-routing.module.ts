import { NgModule, ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

export const routes: Routes = [
  { path: 'news', loadChildren: 'app/news/news.module#NewsModule' },
  { path: '**', redirectTo: 'news', pathMatch: 'full' }
];

export const AppRoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, { useHash: true });
