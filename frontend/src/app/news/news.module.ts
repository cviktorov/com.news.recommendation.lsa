import { NgModule } from '@angular/core';
import { NewsRoutingModule } from "app/news/news-routing.module";
import { NewsSearchComponent } from "app/news/search/news-search.component";
import { NewsDetailComponent } from "app/news/detail/news-detail.component";
import { NewsService } from "app/news/news.service";
import { CommonModule } from "@angular/common";

@NgModule({
    imports: [
        NewsRoutingModule,
        CommonModule
    ],
    exports: [],
    declarations: [
      NewsSearchComponent,
      NewsDetailComponent
    ],
    providers: [
        NewsService
    ],
})
export class NewsModule { }
