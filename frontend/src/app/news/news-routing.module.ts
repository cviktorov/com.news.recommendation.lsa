import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NewsSearchComponent } from "app/news/search/news-search.component";
import { NewsDetailComponent } from "app/news/detail/news-detail.component";

const routes: Routes = [
  {
    path: 'news',
    children: [
      { path: 'search', component: NewsSearchComponent },
      { path: 'detail/:id', component: NewsDetailComponent },
      { path: '**', redirectTo: 'search' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NewsRoutingModule { }
