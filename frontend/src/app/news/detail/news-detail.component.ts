import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { Article } from "app/domain/article";
import { NewsService } from "app/news/news.service";
import 'rxjs/add/operator/switchMap';


@Component({
    selector: 'news-detail',
    templateUrl: 'news-detail.component.html'
})

export class NewsDetailComponent implements OnInit {

    document: Article;

    constructor(
        private newsService: NewsService,
        private router: Router,
        private route: ActivatedRoute
        ) { }

    ngOnInit() {
        this.route.paramMap
            .switchMap((params: ParamMap) => this.newsService.getDocumentById(params.get('id')))
            .subscribe(doc => this.document = doc);
    }

    toSearch() {
        this.router.navigate(['search'], { relativeTo: this.route });
    }
}