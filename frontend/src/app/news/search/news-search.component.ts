import { Component, OnInit } from '@angular/core';
import { Article } from "app/domain/article";
import { NewsService } from "app/news/news.service";
import { Router, ActivatedRoute } from "@angular/router";

@Component({
    selector: 'news-search',
    templateUrl: 'news-search.component.html'
})

export class NewsSearchComponent implements OnInit {

    documentsLSA: Article[];

    documentsWV: Article[];

    query: string;

    constructor(
        private newsSearchService: NewsService,
        private router: Router,
        private route: ActivatedRoute
    ) { }

    ngOnInit() {
        this.documentsLSA = this.newsSearchService.documentsCacheLSA;
        this.documentsWV = this.newsSearchService.documentsCacheWV;
        this.query = this.newsSearchService.lastSearch;
    }

    searchDocs() {
        if (!this.query) {
            return;
        }
        this.newsSearchService.getDocumentsLSA(this.query)
            .then(res => this.documentsLSA = res)
            .catch(err => console.log(err));
        this.newsSearchService.getDocumentsWV(this.query)
            .then(res => this.documentsWV = res)
            .catch(err => console.log(err));
    }

    onDocSelect(id: string, algo: string) {
        this.newsSearchService.detailCurrentAlgo = algo;
        this.router.navigate(['news/detail', id]);
    }
}