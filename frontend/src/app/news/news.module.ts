import { NgModule } from '@angular/core';
import { NewsRoutingModule } from "app/news/news-routing.module";
import { NewsSearchComponent } from "app/news/search/news-search.component";
import { NewsDetailComponent } from "app/news/detail/news-detail.component";
import { NewsService } from "app/news/news.service";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

@NgModule({
    imports: [
        NewsRoutingModule,
        CommonModule,
        FormsModule
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
