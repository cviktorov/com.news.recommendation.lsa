import { Component, OnInit } from '@angular/core';
import { Article } from "app/domain/article";
import { NewsService } from "app/news/news.service";
import { Router, ActivatedRoute } from "@angular/router";

@Component({
    selector: 'news-search',
    templateUrl: 'news-search.component.html'
})

export class NewsSearchComponent implements OnInit {

    documents: Article[];

    query: string;

    constructor(
        private newsSearchService: NewsService,
        private router: Router,
        private route: ActivatedRoute
    ) { }

    ngOnInit() {
        this.documents = this.newsSearchService.documentsCache;
        this.query = this.newsSearchService.lastSearch;
    }

    searchDocs() {
        this.newsSearchService.getDocuments(this.query)
            .then(res => this.documents = res)
            .catch(err => console.log(err));
    }

    onDocSelect(id: string) {
        this.router.navigate(['news/detail', id]);
    }
}