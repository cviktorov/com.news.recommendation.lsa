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
    
    constructor(
        private newsSearchService: NewsService,
        private router: Router,
        private route: ActivatedRoute
        ) { }

    ngOnInit() { }

    searchDocs(query: string) {
        this.newsSearchService.getDocuments(query)
            .then(res => this.documents = res)
            .catch(err => console.log(err));
    }

    onDocSelect(id: string) {
        this.router.navigate(['news/detail', id]);
    }
}